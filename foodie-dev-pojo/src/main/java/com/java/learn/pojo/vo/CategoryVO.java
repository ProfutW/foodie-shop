package com.java.learn.pojo.vo;

import lombok.Data;

import java.util.List;

// 二级分类VO
@Data
public class CategoryVO {
    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;

    // 三级分类VO
    private List<SubCategoryVO> subCatList;
}
