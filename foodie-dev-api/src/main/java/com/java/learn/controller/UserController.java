package com.java.learn.controller;

import com.java.learn.pojo.Users;
import com.java.learn.pojo.bo.UserBO;
import com.java.learn.utils.JsonResp;
import com.java.learn.service.UserService;
import com.java.learn.utils.CookieUtils;
import com.java.learn.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Api(tags = {"用户相关接口"})
@RestController
@RequestMapping("passport")
@Validated
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value = "检测用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JsonResp usernameIsExist(@RequestParam @NotBlank String username) {
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JsonResp.error(409, "用户名已存在");
        }

        return JsonResp.success();
    }

    @ApiOperation(value = "用户注册", httpMethod = "POST")
    @PostMapping("/register")
    public JsonResp signUp(@Validated(UserBO.WithConfirmPwd.class) @RequestBody UserBO userBO,
                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean isExist = userService.queryUsernameIsExist(userBO.getUsername());
        if (isExist) {
            return JsonResp.error(409, "用户名已存在");
        }

        if (!userBO.getPassword().equals(userBO.getConfirmPassword())) {
            return JsonResp.error(422, "两次密码输入不一致");
        }
        Users user = userService.createUser(userBO);

        user = setNullProperty(user);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(user), true);

        logger.info("用户 {} 注册成功", user.getUsername());
        return JsonResp.success(user);
    }

    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JsonResp signIn(@Valid @RequestBody UserBO userBO,
                           HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        Users user = userService.checkPasswordByUsername(userBO.getUsername(), userBO.getPassword());

        if (user == null) {
            return JsonResp.error("用户名或密码不正确");
        }

        user = setNullProperty(user);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(user), true);

        logger.info("用户 {} 登录成功", user.getUsername());
        return JsonResp.success(user);
    }

    @ApiOperation(value = "退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JsonResp logOut(HttpServletRequest request, HttpServletResponse response) {
        String userInfo = CookieUtils.getCookieValue(request, "user", true);
        Users user = JsonUtils.jsonToPojo(userInfo, Users.class);
        CookieUtils.deleteCookie(request, response, "user");

        logger.info("用户 {} 退出登录", user.getUsername());
        return JsonResp.success();
    }

    private Users setNullProperty(Users user) {
        user.setPassword(null);
        user.setRealname(null);
        user.setsimpleItemList(null);
        user.setMobile(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);

        return user;
    }
}
