package com.java.learn.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@ApiModel(value = "用户地址对象",description = "用户地址")
@Data
public class AddressBO {
    @ApiModelProperty(value = "用户地址ID",
            name = "addressId",
            required = true)
    @NotEmpty(groups = WithAddressId.class)
    private String addressId;

    @ApiModelProperty(value = "用户ID",
            name = "userId",
            required = true)
    @NotEmpty
    private String userId;

    @ApiModelProperty(value = "收件人",
            name = "receiver",
            required = true)
    @NotEmpty
    private String receiver;

    @ApiModelProperty(value = "手机号",
            name = "mobile",
            required = true)
    @NotEmpty
    private String mobile;

    @ApiModelProperty(value = "省份",
            name = "province",
            required = true)
    @NotEmpty
    private String province;

    @ApiModelProperty(value = "城市",
            name = "city",
            required = true)
    @NotEmpty
    private String city;

    @ApiModelProperty(value = "区县",
            name = "district",
            required = true)
    @NotEmpty
    private String district;

    @ApiModelProperty(value = "详细地址",
            name = "detail",
            required = true)
    @NotEmpty
    private String detail;

    // 分组校验
    public interface WithAddressId {}
}
