package com.java.learn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.learn.enums.ExceptionCodeEnum;
import com.java.learn.enums.ItemCommendLevel;
import com.java.learn.enums.YesOrNo;
import com.java.learn.mapper.*;
import com.java.learn.pojo.*;
import com.java.learn.pojo.vo.CommentLevelCountVO;
import com.java.learn.pojo.vo.ItemCommentsVO;
import com.java.learn.pojo.vo.SearchItemVO;
import com.java.learn.pojo.vo.ShopcartVO;
import com.java.learn.service.ItemService;
import com.java.learn.utils.CustomException;
import com.java.learn.utils.DesensitizationUtil;
import com.java.learn.utils.PagedGridResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl extends BaseService implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items getItem(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> getItemImg(String itemId) {
        Example example = new Example(ItemsImg.class);
        example.and().andEqualTo("itemId", itemId);

        return itemsImgMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> getItemSpec(String itemId) {
        Example example = new Example(ItemsImg.class);
        example.and().andEqualTo("itemId", itemId);

        return itemsSpecMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam getItemParam(String itemId) {
        Example example = new Example(ItemsImg.class);
        example.and().andEqualTo("itemId", itemId);

        return itemsParamMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountVO getItemCommentCount(String itemId) {
        Integer goodCounts = getCommentByLevel(itemId, ItemCommendLevel.GOOD.type);
        Integer normalCounts = getCommentByLevel(itemId, ItemCommendLevel.NORMAL.type);
        Integer badCounts = getCommentByLevel(itemId, ItemCommendLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;

        CommentLevelCountVO commentLevelCountVO = new CommentLevelCountVO();
        commentLevelCountVO.setGoodCounts(goodCounts);
        commentLevelCountVO.setNormalCounts(normalCounts);
        commentLevelCountVO.setBadCounts(badCounts);
        commentLevelCountVO.setTotalCounts(totalCounts);
        return commentLevelCountVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getItemComments(Map<String, Object> itemCommentMap,
                                                Integer page,
                                                Integer pageSize) {

        /**
         * page: 第几页
         * pageSize: 每页显示数目
         */
        PageHelper.startPage(page, pageSize);

        List<ItemCommentsVO> list = itemsCommentsMapper.getItemComments(itemCommentMap);
        for (ItemCommentsVO vo : list) {
            vo.setNickName(DesensitizationUtil.commonDisplay(vo.getNickName()));
        }

        return setPageGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItem(String keyword, String sort, Integer page, Integer pageSize) {

        /**
         * page: 第几页
         * pageSize: 每页显示数目
         */
        PageHelper.startPage(page, pageSize);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("keyword", keyword);
        paramMap.put("sort", sort);
        List<SearchItemVO> list = itemsMapper.searchItem(paramMap);

        return setPageGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItemByThirdCat(Integer catId, String sort, Integer page, Integer pageSize) {
        /**
         * page: 第几页
         * pageSize: 每页显示数目
         */
        PageHelper.startPage(page, pageSize);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("catId", catId);
        paramMap.put("sort", sort);
        List<SearchItemVO> list = itemsMapper.searchItemByThirdCat(paramMap);

        return setPageGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVO> getItemsBySpecIds(List<String> specIds) {

        return itemsSpecMapper.getItemsBySpecIds(specIds);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec getItemSpecById(String specId) {

        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String getItemUrlById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);

        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result != null ? result.getUrl() : "";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, Integer buyCounts) {
        Integer result = itemsSpecMapper.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            ItemsSpec itemsSpec = itemsSpecMapper.selectByPrimaryKey(specId);
            Items item = itemsMapper.selectByPrimaryKey(itemsSpec.getItemId());

            logger.warn("库存不足,规格ID:{}", specId);
            String msg = String.format("抱歉，商品库存不足！(%s:%s)", item.getItemName(), itemsSpec.getName());
            throw new CustomException(ExceptionCodeEnum.NO_STOCK, msg);
        }
    }

    private Integer getCommentByLevel(String itemId, Integer level) {

        ItemsComments comment = new ItemsComments();
        comment.setItemId(itemId);
        comment.setCommentLevel(level);
        return itemsCommentsMapper.selectCount(comment);
    }
}
