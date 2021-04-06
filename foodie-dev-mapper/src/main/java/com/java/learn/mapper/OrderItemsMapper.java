package com.java.learn.mapper;

import com.java.learn.my.MyMapper;
import com.java.learn.pojo.ItemsComments;
import com.java.learn.pojo.OrderItems;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemsMapper extends MyMapper<OrderItems> {
}