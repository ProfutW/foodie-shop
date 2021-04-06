package com.java.learn.service.impl;

import com.java.learn.enums.CategoryType;
import com.java.learn.mapper.CategoryMapper;
import com.java.learn.pojo.Category;
import com.java.learn.pojo.vo.CategoryVO;
import com.java.learn.pojo.vo.RecommendCategoryVO;
import com.java.learn.service.CategorylService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategorylService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> getAllRootCat() {
        Example example = new Example(Category.class);
        example.and().andEqualTo("type", CategoryType.FIRST.type);

        return categoryMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryMapper.getSubCatList(rootCatId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<RecommendCategoryVO> getRecommendItem(Integer rootCatId) {
        Map<String, Object> catMap = new HashMap<>();
        catMap.put("rootCatId", rootCatId);

        return categoryMapper.getRecommendItem(catMap);
    }
}
