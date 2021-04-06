package com.java.learn.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ApiModel(value = "用户对象",description = "客户端传入的用户对象参数")
@Data
public class UserBO {
    @ApiModelProperty(value = "用户名",
            name = "username",
            example = "lumos",
            required = true)
    @NotEmpty
    @Size(min = 2, message = "长度最小2位")
    private String username;

    @ApiModelProperty(value = "密码",
            name = "password",
            example = "123456",
            required = true)
    @NotEmpty
    @Size(min = 6, message = "长度最小6位")
    private String password;

    @ApiModelProperty(value = "确认密码",
            name = "password",
            example = "123456",
            required = false)
    @NotEmpty(groups = WithConfirmPwd.class)
    @Size(min = 6, message = "长度最小6位", groups = WithConfirmPwd.class)
    private String confirmPassword;

    // 分组校验
    public interface WithConfirmPwd {}
}
