package com.java.learn.service.center;

import com.java.learn.pojo.OrderItems;
import com.java.learn.pojo.Users;
import com.java.learn.pojo.bo.center.CenterUserBO;
import com.java.learn.pojo.bo.center.OrderItemsCommentBO;
import com.java.learn.utils.PagedGridResult;

import java.util.List;

public interface CenterUserService {
    Users queryUserInfo(String userId);

    Integer updateUserInfo(String userId, CenterUserBO userBO);

    Integer updateUserFace(String userId, String userFace);

    PagedGridResult queryCommentsByUserId(String userId, Integer page, Integer pageSize);

    List<OrderItems> queryItemsByOrderId(String orderId);

    void saveComments(String userId, String orderId, List<OrderItemsCommentBO> list);
}
