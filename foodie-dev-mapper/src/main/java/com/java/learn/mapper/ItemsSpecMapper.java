package com.java.learn.mapper;

import com.java.learn.my.MyMapper;
import com.java.learn.pojo.ItemsSpec;
import com.java.learn.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemsSpecMapper extends MyMapper<ItemsSpec> {

    List<ShopcartVO> getItemsBySpecIds(@Param("paramsList")List<String> specIds);

    Integer decreaseItemSpecStock(@Param("specId") String specId, @Param("buyCounts") Integer buyCounts);
}