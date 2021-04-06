package com.java.learn.mapper;

import com.java.learn.my.MyMapper;
import com.java.learn.pojo.OrderStatus;
import com.java.learn.pojo.Orders;
import com.java.learn.pojo.vo.OrderListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

public interface OrdersMapper extends MyMapper<Orders> {

    List<OrderListVO> queryOrdersByUserId(@Param("paramsMap") Map<String, Object> paramsMap);

    public int getMyOrderStatusCounts(@Param("paramsMap") Map<String, Object> map);

    public List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String, Object> map);
}