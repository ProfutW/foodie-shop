package com.java.learn.service;

import com.java.learn.pojo.ItemsComments;
import com.java.learn.pojo.OrderStatus;
import com.java.learn.pojo.Orders;
import com.java.learn.pojo.bo.OrderBO;
import com.java.learn.pojo.vo.OrderListVO;
import com.java.learn.pojo.vo.OrderStatusCountsVO;
import com.java.learn.pojo.vo.OrderVO;
import com.java.learn.utils.PagedGridResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderVO createOrder(OrderBO orderBO);

    void updateOrderStatus(String orderId, Integer orderStatus);

    void closeOrder();

    PagedGridResult queryOrdersByUserId(String userId, Integer orderStatus,
                                        Integer page, Integer pageSize);

    OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 查询用户订单数
     * @param userId
     */
    public OrderStatusCountsVO getOrderStatusCounts(String userId);

    /**
     * 获得分页的订单动向
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult getOrdersTrend(String userId,
                                          Integer page,
                                          Integer pageSize);
}
