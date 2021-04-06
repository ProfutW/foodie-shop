package com.java.learn.pojo.vo;

import lombok.Data;

import java.util.List;

// 一级分类
@Data
public class RecommendCategoryVO {
    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;

    // 推荐商品
    private List<RecommendItemVO> simpleItemList;
}
