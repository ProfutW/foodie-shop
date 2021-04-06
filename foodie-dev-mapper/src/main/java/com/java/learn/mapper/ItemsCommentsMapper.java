package com.java.learn.mapper;

import com.java.learn.my.MyMapper;
import com.java.learn.pojo.ItemsComments;
import com.java.learn.pojo.vo.ItemCommentsVO;
import com.java.learn.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapper extends MyMapper<ItemsComments> {

    List<ItemCommentsVO> getItemComments(@Param("itemCommentMap") Map<String, Object> itemCommentMap);

    List<MyCommentVO> queryMyComments(@Param("userId") String userId);

    void saveComments(Map<String, Object> map);
}