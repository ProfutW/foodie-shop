package com.java.learn.controller;

import com.java.learn.pojo.bo.ShopcartBO;
import com.java.learn.service.ItemService;
import com.java.learn.utils.JsonResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Api(tags = {"购物车相关接口"})
@RestController
@RequestMapping("shopcart")
@Validated
public class ShopcartController {
    private static final Logger logger = LoggerFactory.getLogger(ShopcartController.class);

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JsonResp addItemToCart(
            @ApiParam(name = "userId", value = "用户ID", required = true)
                @RequestParam @NotBlank String userId,
            @ApiParam(name = "shopcartBO", value = "商品对象", required = true)
                @Valid @RequestBody ShopcartBO shopcartBO) {

        System.out.println(shopcartBO);
        return JsonResp.success();
    }

    @ApiOperation(value = "删除购物车商品", httpMethod = "POST")
    @PostMapping("/del")
    public JsonResp delItemToCart(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam @NotBlank String userId,
            @ApiParam(name = "itemSpecId", value = "商品规格ID", required = true)
            @RequestParam @NotBlank String itemSpecId) {

        System.out.println(userId + "+" + itemSpecId);
        return JsonResp.success();
    }
}
