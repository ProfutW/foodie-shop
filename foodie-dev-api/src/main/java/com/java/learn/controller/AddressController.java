package com.java.learn.controller;

import com.java.learn.pojo.UserAddress;
import com.java.learn.pojo.bo.AddressBO;
import com.java.learn.service.UserAddressService;
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
import java.util.List;

@Api(tags = {"用户地址相关接口"})
@RestController
@RequestMapping("address")
@Validated
public class AddressController {
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    UserAddressService userAddressService;

    @ApiOperation(value = "获取用户地址", httpMethod = "GET")
    @GetMapping("/list")
    public JsonResp getUserAddress(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam @NotBlank String userId) {

        List<UserAddress> addressList = userAddressService.getAddressByUserId(userId);
        return JsonResp.success(addressList);
    }

    @ApiOperation(value = "新增用户地址", httpMethod = "POST")
    @PostMapping("/add")
    public JsonResp addUserAddress(
            @ApiParam(name = "addressBO", value = "用户地址对象", required = true)
            @RequestBody @Valid AddressBO addressBO) {

        userAddressService.addUserAddress(addressBO);
        return JsonResp.success();
    }

    @ApiOperation(value = "修改用户地址", httpMethod = "POST")
    @PostMapping("/update")
    public JsonResp updateUserAddress(
            @ApiParam(name = "addressBO", value = "用户地址对象", required = true)
            @RequestBody @Validated(AddressBO.WithAddressId.class) AddressBO addressBO) {

        userAddressService.updateUserAddress(addressBO);
        return JsonResp.success();
    }

    @ApiOperation(value = "删除用户地址", httpMethod = "POST")
    @PostMapping("/delete")
    public JsonResp deleteUserAddress(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam @NotBlank String userId,
            @ApiParam(name = "addressId", value = "用户地址ID", required = true)
            @RequestParam @NotBlank String addressId) {

        userAddressService.deleteUserAddress(userId, addressId);
        return JsonResp.success();
    }

    @ApiOperation(value = "更新地址为默认", httpMethod = "POST")
    @PostMapping("/setDefault")
    public JsonResp setDefaultAddress(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam @NotBlank String userId,
            @ApiParam(name = "addressId", value = "用户地址ID", required = true)
            @RequestParam @NotBlank String addressId) {

        userAddressService.setDefaultAddress(userId, addressId);
        return JsonResp.success();
    }
}
