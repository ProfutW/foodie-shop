package com.java.learn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.learn.enums.OrderStatusEnum;
import com.java.learn.enums.YesOrNo;
import com.java.learn.mapper.ItemsCommentsMapper;
import com.java.learn.mapper.OrderItemsMapper;
import com.java.learn.mapper.OrderStatusMapper;
import com.java.learn.mapper.OrdersMapper;
import com.java.learn.pojo.*;
import com.java.learn.pojo.bo.OrderBO;
import com.java.learn.pojo.vo.MerchantOrdersVO;
import com.java.learn.pojo.vo.OrderListVO;
import com.java.learn.pojo.vo.OrderStatusCountsVO;
import com.java.learn.pojo.vo.OrderVO;
import com.java.learn.service.ItemService;
import com.java.learn.service.OrderService;
import com.java.learn.service.UserAddressService;
import com.java.learn.utils.DateUtil;
import com.java.learn.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends BaseService implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private ItemService itemService;


    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(OrderBO orderBO) {

        Integer postAmount = 0;
        String userId = orderBO.getUserId();
        String addressId = orderBO.getAddressId();
        String orderId = sid.nextShort();
        Integer payMethod = orderBO.getPayMethod();

        Orders order = new Orders();
        order.setId(orderId);
        order.setUserId(userId);

        order.setPayMethod(payMethod);
        order.setLeftMsg(orderBO.getLeftMsg());
        // 收件人地址快照
        UserAddress userAddress = userAddressService.getUserAddress(userId, addressId);
        order.setReceiverName(userAddress.getReceiver());
        String detailAddress = userAddress.getProvince() + " "
                + userAddress.getCity() + " "
                + userAddress.getDistrict() + " "
                + userAddress.getDetail();
        order.setReceiverAddress(detailAddress);
        order.setReceiverMobile(userAddress.getMobile());

        order.setPostAmount(postAmount);
        order.setIsComment(YesOrNo.NO.type);
        order.setIsDelete(YesOrNo.NO.type);
        order.setCreatedTime(new Date());
        order.setUpdatedTime(new Date());

        // order_items插入
        String[] itemSpecIds = orderBO.getItemSpecIds().split(",");
        Integer totalAmount = 0;
        Integer realPayAmount = 0;
        for (String specId : itemSpecIds) {
            ItemsSpec itemsSpec = itemService.getItemSpecById(specId);
            // TODO 整合redis后，商品数量从redis中获取
            Integer buyCounts = 1;
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            // 根据商品ID，获取商品信息和商品图片
            String itemId = itemsSpec.getItemId();
            Items item = itemService.getItem(itemId);
            String itemImg = itemService.getItemUrlById(itemId);

            String orderItemId = sid.nextShort();
            OrderItems orderItem = new OrderItems();
            orderItem.setOrderId(orderId);
            orderItem.setId(orderItemId);
            orderItem.setItemImg(itemImg);
            orderItem.setItemId(itemId);
            orderItem.setItemName(item.getItemName());
            orderItem.setBuyCounts(buyCounts);
            orderItem.setItemSpecId(specId);
            orderItem.setItemSpecName(itemsSpec.getName());
            orderItem.setPrice(itemsSpec.getPriceDiscount());

            // 扣减库存
            itemService.decreaseItemSpecStock(specId, buyCounts);
            logger.info("===== 库存扣减成功--{}:{} =====", specId, buyCounts);

            orderItemsMapper.insert(orderItem);
            logger.info("===== 订单商品记录成功:{} =====", orderItemId);
        }

        order.setTotalAmount(totalAmount);
        order.setRealPayAmount(realPayAmount);

        // 保存订单主信息
        ordersMapper.insert(order);
        logger.info("===== 主订单保存成功:{} =====", orderId);

        // 保存订单状态信息
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_TO_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());

        orderStatusMapper.insert(waitPayOrderStatus);
        logger.info("===== 订单状态更新成功:{} =====", orderId);

        // 构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);

        return orderVO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus status = new OrderStatus();
        status.setOrderId(orderId);
        status.setOrderStatus(orderStatus);

        orderStatusMapper.updateByPrimaryKeySelective(status);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {

        Example example = new Example(OrderStatus.class);
        example.and()
                .andLessThanOrEqualTo("createdTime", DateUtil.dateIncreaseByDay(new Date(), -1))
                .andEqualTo("orderStatus", OrderStatusEnum.WAIT_TO_PAY.type);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);

        orderStatusMapper.updateByExampleSelective(orderStatus, example);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedGridResult queryOrdersByUserId(String userId, Integer orderStatus,
                                               Integer page, Integer pageSize) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        if (orderStatus != null) {
            paramsMap.put("orderStatus", orderStatus);
        }

        PageHelper.startPage(page, pageSize);
        List<OrderListVO> orderList = ordersMapper.queryOrdersByUserId(paramsMap);

        return setPageGrid(orderList, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        map.put("orderStatus", OrderStatusEnum.WAIT_TO_PAY.type);
        int waitPayCounts = ordersMapper.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_TO_DELIVER.type);
        int waitDeliverCounts = ordersMapper.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_TO_RECEIVE.type);
        int waitReceiveCounts = ordersMapper.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment", YesOrNo.NO.type);
        int waitCommentCounts = ordersMapper.getMyOrderStatusCounts(map);

        OrderStatusCountsVO countsVO = new OrderStatusCountsVO(waitPayCounts,
                waitDeliverCounts,
                waitReceiveCounts,
                waitCommentCounts);
        return countsVO;
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    @Override
    public PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<OrderStatus> list = ordersMapper.getMyOrderTrend(map);

        return setPageGrid(list, page);
    }
}
