package com.java.learn.controller.center;

import com.java.learn.config.CustomConfig;
import com.java.learn.pojo.Users;
import com.java.learn.pojo.bo.center.CenterUserBO;
import com.java.learn.service.center.CenterUserService;
import com.java.learn.utils.CookieUtils;
import com.java.learn.utils.DateUtil;
import com.java.learn.utils.JsonResp;
import com.java.learn.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

@Api(value = "用户信息接口", tags = {"用户信息接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private CustomConfig config;

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public JsonResp getUserInfo(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "centerUserBO", value = "用户信息对象", required = true)
            @RequestBody @Valid CenterUserBO centerUserBO,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Integer result = centerUserService.updateUserInfo(userId, centerUserBO);
        if (result != 1) {
            return JsonResp.error("更新失败，未找到符合条件的信息");
        }
        Users user = centerUserService.queryUserInfo(userId);
        user = setNullProperty(user);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(user), true);

        return JsonResp.success(user);
    }


    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("uploadFace")
    @Transactional(propagation = Propagation.REQUIRED)
    public JsonResp update(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "centerUserBO", value = "用户信息对象", required = true)
                    MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String uploadFacePath = CustomConfig.IMAGE_USER_FACE_LOCATION + File.separator + userId;
        if (file == null) {
            return JsonResp.error("头像文件不能为空");
        }
        if (!Pattern.matches(CustomConfig.FILE_NAME_REGEX, file.getOriginalFilename())) {
            return JsonResp.error("文件名异常或格式不正确");
        }
        String[] fileNames = file.getOriginalFilename().split("\\.");
        String newFileName = "face-" + userId + "." + fileNames[fileNames.length - 1];

        String finalPath = uploadFacePath + File.separator + newFileName;

        File outFile = new File(finalPath);
        if (outFile.getParentFile() != null) {
            outFile.getParentFile().mkdirs();
        }
        file.transferTo(outFile);

        centerUserService.updateUserFace(userId,
                config.getFaceServerUrl() + "/"
                        + userId + "/"
                        + newFileName
                        + "?t="
                        + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));

        Users user = centerUserService.queryUserInfo(userId);
        user = setNullProperty(user);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(user), true);

        return JsonResp.success();
    }

    private Users setNullProperty(Users user) {
        user.setPassword(null);
        user.setRealname(null);
        user.setBirthday(null);
        user.setMobile(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);

        return user;
    }
}
