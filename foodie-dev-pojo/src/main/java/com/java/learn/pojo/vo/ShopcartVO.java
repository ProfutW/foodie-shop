package com.java.learn.pojo.vo;

import lombok.Data;

// 商品详情VO
@Data
public class ShopcartVO {
    private String itemId;
    private String itemName;
    private String itemImgUrl;
    private String specId;
    private String specName;
    private Integer priceNormal;
    private Integer priceDiscount;
}
