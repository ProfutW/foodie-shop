package com.java.learn.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ApiModel(value = "购物车商品对象",description = "添加进购物车的商品对象")
@Data
public class ShopcartBO {
    @ApiModelProperty(value = "商品ID",
            name = "itemId",
            example = "cake-1001",
            required = true)
    @NotEmpty
    private String itemId;

    @ApiModelProperty(value = "商品图片url",
            name = "itemImgUrl",
            required = true)
    @NotEmpty
    private String itemImgUrl;

    @ApiModelProperty(value = "商品名称",
            name = "itemName",
            required = true)
    @NotEmpty
    private String itemName;

    @ApiModelProperty(value = "商品规格ID",
            name = "specId",
            required = true)
    @NotEmpty
    private String specId;

    @ApiModelProperty(value = "商品规格名称",
            name = "specName",
            required = true)
    @NotEmpty
    private String specName;

    @ApiModelProperty(value = "购买数量",
            name = "buyCounts",
            required = true)
    @Min(1)
    private Integer buyCounts;

    @ApiModelProperty(value = "商品优惠价",
            name = "priceDiscount",
            required = true)
    private Integer priceDiscount;

    @ApiModelProperty(value = "商品原价",
            name = "priceNormal",
            required = true)
    private Integer priceNormal;
}
