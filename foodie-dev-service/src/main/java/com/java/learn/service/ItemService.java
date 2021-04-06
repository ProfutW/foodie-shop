package com.java.learn.service;

import com.java.learn.pojo.*;
import com.java.learn.pojo.vo.CommentLevelCountVO;
import com.java.learn.pojo.vo.ShopcartVO;
import com.java.learn.utils.PagedGridResult;

import java.util.List;
import java.util.Map;

public interface ItemService {

    Items getItem(String itemId);

    List<ItemsImg> getItemImg(String itemId);

    List<ItemsSpec> getItemSpec(String itemId);

    ItemsParam getItemParam(String itemId);

    CommentLevelCountVO getItemCommentCount(String itemId);

    PagedGridResult getItemComments(Map<String, Object> itemCommentMap,
                                    Integer page,
                                    Integer pageSize);


    PagedGridResult searchItem(String keyword, String sort, Integer page, Integer pageSize);


    PagedGridResult searchItemByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);


    /**
     * 根据规格IDs查询最新的购物车中的商品详情（用于刷新购物车中的商品）
     * @param specIds
     * @return
     */
    List<ShopcartVO> getItemsBySpecIds(List<String> specIds);

    ItemsSpec getItemSpecById(String specId);

    String getItemUrlById(String itemId);

    void decreaseItemSpecStock(String specId, Integer buyCounts);
}
