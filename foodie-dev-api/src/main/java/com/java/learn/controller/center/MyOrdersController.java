package com.java.learn.controller.center;

import com.java.learn.config.CustomConfig;
import com.java.learn.enums.OrderStatusEnum;
import com.java.learn.enums.YesOrNo;
import com.java.learn.mapper.OrdersMapper;
import com.java.learn.pojo.Orders;
import com.java.learn.pojo.bo.OrderBO;
import com.java.learn.pojo.vo.MerchantOrdersVO;
import com.java.learn.pojo.vo.OrderStatusCountsVO;
import com.java.learn.pojo.vo.OrderVO;
import com.java.learn.service.OrderService;
import com.java.learn.utils.JsonResp;
import com.java.learn.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Api(tags = {"订单管理"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController {
    private static final Logger logger = LoggerFactory.getLogger(MyOrdersController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    OrdersMapper ordersMapper;

    @ApiOperation(value = "查询用户订单", httpMethod = "GET")
    @PostMapping("/query")
    public JsonResp notifyMerchantOrderPaid(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam @NotBlank String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = false)
            @RequestParam(required = false) Integer orderStatus,
            @ApiParam(name = "page", value = "查询第几页", required = false)
            @RequestParam(defaultValue = CustomConfig.PAGE) Integer page,
            @ApiParam(name = "pageSize", value = "每一页显示的数目", required = false)
            @RequestParam(defaultValue = CustomConfig.PAGE_SIZE) Integer pageSize) {

        PagedGridResult result = orderService.queryOrdersByUserId(userId, orderStatus, page, pageSize);
        return JsonResp.success(result);
    }

    @ApiOperation(value = "确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public JsonResp confirmReceive(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam @NotBlank String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId) {

        Integer result = checkUserOrder(userId, orderId);
        if (result != HttpStatus.OK.value()) {
            return JsonResp.error("用户与订单不匹配");
        }

        orderService.updateOrderStatus(orderId, OrderStatusEnum.SUCCESS.type);
        return JsonResp.success();
    }

    @ApiOperation(value = "删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public JsonResp deleteOrder(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam @NotBlank String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam String orderId) {

        Integer result = checkUserOrder(userId, orderId);
        if (result != HttpStatus.OK.value()) {
            return JsonResp.error("用户与订单不匹配");
        }

        Orders order = new Orders();
        order.setId(orderId);
        order.setIsDelete(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(order);
        return JsonResp.success();
    }

    private Integer checkUserOrder(String userId, String orderId) {
        Orders order = new Orders();
        order.setUserId(userId);
        order.setId(orderId);
        Orders myOrder = ordersMapper.selectOne(order);
        if (myOrder == null) {
            return HttpStatus.NO_CONTENT.value();
        }
        return HttpStatus.OK.value();
    }

    @ApiOperation(value = "获得订单状态数概况", notes = "获得订单状态数概况", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public JsonResp statusCounts(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam @NotBlank String userId) {

        OrderStatusCountsVO result = orderService.getOrderStatusCounts(userId);

        return JsonResp.success(result);
    }

    @ApiOperation(value = "查询订单动向", notes = "查询订单动向", httpMethod = "POST")
    @PostMapping("/trend")
    public JsonResp trend(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam @NotBlank String userId,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam(defaultValue = CustomConfig.PAGE) Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam(defaultValue = CustomConfig.PAGE_SIZE) Integer pageSize) {

        PagedGridResult grid = orderService.getOrdersTrend(userId,
                page,
                pageSize);

        return JsonResp.success(grid);
    }
}
