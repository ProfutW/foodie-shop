package com.java.learn.controller;

import com.java.learn.config.CustomConfig;
import com.java.learn.enums.OrderStatusEnum;
import com.java.learn.pojo.OrderStatus;
import com.java.learn.pojo.bo.OrderBO;
import com.java.learn.pojo.vo.MerchantOrdersVO;
import com.java.learn.pojo.vo.OrderVO;
import com.java.learn.service.OrderService;
import com.java.learn.utils.CookieUtils;
import com.java.learn.utils.JsonResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

@Api(tags = {"订单详情"})
@RestController
@RequestMapping("orders")
@Validated
public class OrdersController {
    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    OrderService orderService;

    @ApiOperation(value = "创建订单", httpMethod = "POST")
    @PostMapping("/create")
    public JsonResp createOrder(
            @ApiParam(name = "orderBO", value = "订单参数", required = true)
            @RequestBody @Valid OrderBO orderBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        // 创建订单
        OrderVO orderVO = orderService.createOrder(orderBO);
        String orderId = orderVO.getOrderId();
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(CustomConfig.payReturnUrl);

        // 移除购物车内的商品
        // TODO: 整合redis后，移除购物车商品
        CookieUtils.setCookie(request, response, "shopcart", "");

        // 向支付中心发送订单，生成订单信息
        // RestTemplate restTemplate = new RestTemplate();
        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);
        // headers.add("imoocUserId", "imooc");
        // headers.add("password", "imooc");
        //
        // HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);
        // ResponseEntity<JsonResp> responseEntity =
        //         restTemplate.postForEntity(CustomConfig.paymentUrl, entity, JsonResp.class);
        // JsonResp paymentResult = responseEntity.getBody();
        // if (paymentResult.getStatus() != 200) {
        //     return JsonResp.error("支付中心订单创建失败，请联系管理员！");
        // }

        return JsonResp.success(orderId);
    }

    @ApiOperation(value = "支付中心回调接口，用于更新平台订单状态", httpMethod = "POST")
    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(
            @ApiParam(name = "orderId", value = "订单号", required = true)
            @RequestParam @NotBlank String orderId) {
        orderService.updateOrderStatus(orderId, OrderStatusEnum.WAIT_TO_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public JsonResp getPaidOrderInfo(String orderId) {
        // 以下为了测试，直接修改订单状态为已支付
        orderService.updateOrderStatus(orderId, OrderStatusEnum.WAIT_TO_DELIVER.type);


        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return JsonResp.success(orderStatus);
    }
}
