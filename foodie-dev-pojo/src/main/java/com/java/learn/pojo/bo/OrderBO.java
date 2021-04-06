package com.java.learn.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@ApiModel(value = "订单对象",description = "订单参数")
@Data
public class OrderBO {
    @ApiModelProperty(value = "用户地址ID",
            name = "addressId",
            required = true)
    @NotEmpty
    private String addressId;

    @ApiModelProperty(value = "订单中商品规格ID串",
            name = "itemSpecIds",
            required = true)
    @NotEmpty
    private String itemSpecIds;

    @ApiModelProperty(value = "订单留言",
            name = "leftMsg",
            required = true)
    private String leftMsg;

    @ApiModelProperty(value = "支付方式(1:微信;2:支付宝)",
            name = "payMethod",
            required = true)
    @Min(1)
    @Max(2)
    private Integer payMethod;

    @ApiModelProperty(value = "用户ID",
            name = "userId",
            required = true)
    @NotEmpty
    private String userId;
}
