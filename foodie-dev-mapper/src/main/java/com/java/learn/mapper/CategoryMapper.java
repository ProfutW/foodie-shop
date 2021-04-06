package com.java.learn.mapper;

import com.java.learn.my.MyMapper;
import com.java.learn.pojo.Category;
import com.java.learn.pojo.vo.CategoryVO;
import com.java.learn.pojo.vo.RecommendCategoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapper extends MyMapper<Category> {

    List<CategoryVO> getSubCatList(Integer rootCatId);

    List<RecommendCategoryVO> getRecommendItem(@Param("catMap") Map<String, Object> catMap);
}