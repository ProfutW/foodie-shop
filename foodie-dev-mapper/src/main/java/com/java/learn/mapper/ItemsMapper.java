package com.java.learn.mapper;

import com.java.learn.my.MyMapper;
import com.java.learn.pojo.Items;
import com.java.learn.pojo.vo.SearchItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapper extends MyMapper<Items> {

    List<SearchItemVO> searchItem(@Param("searchMap") Map<String, Object> searchMap);

    List<SearchItemVO> searchItemByThirdCat(@Param("searchMap") Map<String, Object> searchMap);
}