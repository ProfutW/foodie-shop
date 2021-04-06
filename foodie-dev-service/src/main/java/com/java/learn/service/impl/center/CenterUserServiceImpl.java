package com.java.learn.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.java.learn.enums.OrderStatusEnum;
import com.java.learn.enums.YesOrNo;
import com.java.learn.mapper.*;
import com.java.learn.pojo.*;
import com.java.learn.pojo.bo.center.CenterUserBO;
import com.java.learn.pojo.bo.center.OrderItemsCommentBO;
import com.java.learn.pojo.vo.MyCommentVO;
import com.java.learn.service.center.CenterUserService;
import com.java.learn.service.impl.BaseService;
import com.java.learn.utils.MD5Utils;
import com.java.learn.utils.PagedGridResult;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CenterUserServiceImpl extends BaseService implements CenterUserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users user = usersMapper.selectByPrimaryKey(userId);
        user.setPassword("");
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer updateUserInfo(String userId, CenterUserBO userBO) {

        Users updateUser = new Users();
        BeanUtils.copyProperties(userBO, updateUser);
        updateUser.setId(userId);
        updateUser.setUpdatedTime(new Date());
        updateUser.setPassword(null);
        return usersMapper.updateByPrimaryKeySelective(updateUser);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer updateUserFace(String userId, String userFace) {
        Users updateUser = new Users();
        updateUser.setId(userId);
        updateUser.setUpdatedTime(new Date());
        updateUser.setFace(userFace);
        return usersMapper.updateByPrimaryKeySelective(updateUser);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryCommentsByUserId(String userId, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> comments = itemsCommentsMapper.queryMyComments(userId);
        return setPageGrid(comments, page);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<OrderItems> queryItemsByOrderId(String orderId) {
        OrderItems orderItem = new OrderItems();
        orderItem.setOrderId(orderId);
        return orderItemsMapper.select(orderItem);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String userId, String orderId, List<OrderItemsCommentBO> list) {

        // 保存评价列表
        for (OrderItemsCommentBO comment : list) {
            comment.setCommentId(sid.nextShort());
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("commentList", list);

        itemsCommentsMapper.saveComments(paramsMap);

        // 修改订单为已评价
        Orders order = new Orders();
        order.setId(orderId);
        order.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(order);

        // 修改订单状态表的留言时间
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }
}
