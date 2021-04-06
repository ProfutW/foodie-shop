package com.java.learn.service;

import com.java.learn.pojo.Category;
import com.java.learn.pojo.vo.CategoryVO;
import com.java.learn.pojo.vo.RecommendCategoryVO;

import java.util.List;

public interface CategorylService {
    List<Category> getAllRootCat();

    // 根据一级分类id查询子分类信息
    List<CategoryVO> getSubCatList(Integer rootCatId);

    // 根据一级分类id查询最新的6个商品作为推荐商品
    List<RecommendCategoryVO> getRecommendItem(Integer rootCatId);
}
