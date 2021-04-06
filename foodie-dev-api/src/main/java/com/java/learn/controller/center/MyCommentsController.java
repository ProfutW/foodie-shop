package com.java.learn.controller.center;

import com.java.learn.config.CustomConfig;
import com.java.learn.enums.YesOrNo;
import com.java.learn.mapper.ItemsCommentsMapper;
import com.java.learn.mapper.OrderStatusMapper;
import com.java.learn.mapper.OrdersMapper;
import com.java.learn.pojo.ItemsComments;
import com.java.learn.pojo.OrderItems;
import com.java.learn.pojo.OrderStatus;
import com.java.learn.pojo.Orders;
import com.java.learn.pojo.bo.center.OrderItemsCommentBO;
import com.java.learn.service.OrderService;
import com.java.learn.service.center.CenterUserService;
import com.java.learn.utils.JsonResp;
import com.java.learn.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.aspectj.weaver.ast.Or;
import org.n3r.idworker.Sid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Api(tags = {"订单评价管理"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController {
    private static final Logger logger = LoggerFactory.getLogger(MyCommentsController.class);
    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private Sid sid;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @ApiOperation(value = "查询商品评价", httpMethod = "POST")
    @PostMapping("/query")
    public JsonResp queryItemComments(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam @NotBlank String userId,
            @ApiParam(name = "page", value = "查询第几页", required = false)
            @RequestParam(defaultValue = CustomConfig.PAGE) Integer page,
            @ApiParam(name = "pageSize", value = "每一页显示的数目", required = false)
            @RequestParam(defaultValue = CustomConfig.PAGE_SIZE) Integer pageSize) {

        PagedGridResult result = centerUserService.queryCommentsByUserId(userId, page, pageSize);
        return JsonResp.success(result);
    }

    @ApiOperation(value = "查询订单商品详情", httpMethod = "POST")
    @PostMapping("/pending")
    public JsonResp queryOrderItems(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam @NotBlank String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam @NotBlank String orderId) {

        List<OrderItems> items = centerUserService.queryItemsByOrderId(orderId);
        return JsonResp.success(items);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @ApiOperation(value = "新增订单评价", httpMethod = "POST")
    @PostMapping("/saveList")
    public JsonResp saveComment(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam @NotBlank String userId,
            @ApiParam(name = "orderId", value = "订单ID", required = true)
            @RequestParam @NotBlank String orderId,
            @ApiParam(name = "commentBOList", value = "订单商品评价对象列表", required = true)
            @RequestBody @Valid List<OrderItemsCommentBO> commentBOList) {

        centerUserService.saveComments(userId, orderId, commentBOList);


        return JsonResp.success();
    }
}
