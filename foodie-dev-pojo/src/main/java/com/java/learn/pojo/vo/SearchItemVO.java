package com.java.learn.pojo.vo;

import lombok.Data;

import java.util.Date;

// 展示商品搜索列表VO
@Data
public class SearchItemVO {
    private String itemId;
    private String itemName;
    private Integer sellCounts;
    private String imgUrl;
    private Integer itemPrice;
}
