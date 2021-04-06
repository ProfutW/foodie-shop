package com.java.learn.controller;

import com.java.learn.config.CustomConfig;
import com.java.learn.pojo.Items;
import com.java.learn.pojo.ItemsImg;
import com.java.learn.pojo.ItemsParam;
import com.java.learn.pojo.ItemsSpec;
import com.java.learn.pojo.vo.CommentLevelCountVO;
import com.java.learn.pojo.vo.ItemVO;
import com.java.learn.pojo.vo.ShopcartVO;
import com.java.learn.service.ItemService;
import com.java.learn.utils.JsonResp;
import com.java.learn.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"商品相关接口"})
@RestController
@RequestMapping("items")
@Validated
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "获取商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId:[-a-zA-Z0-9-]+}")
    public JsonResp getItem(@PathVariable String itemId) {
        Items item = itemService.getItem(itemId);
        List<ItemsImg> itemImgList = itemService.getItemImg(itemId);
        List<ItemsSpec> itemSpecList = itemService.getItemSpec(itemId);
        ItemsParam itemParams = itemService.getItemParam(itemId);

        ItemVO itemVO = new ItemVO();
        itemVO.setItem(item);
        itemVO.setItemImgList(itemImgList);
        itemVO.setItemSpecList(itemSpecList);
        itemVO.setItemParams(itemParams);

        return JsonResp.success(itemVO);
    }

    @ApiOperation(value = "查询商品评价数量", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JsonResp getItemCommentCount(@RequestParam @NotBlank String itemId) {
        CommentLevelCountVO commentLevelCountVO = itemService.getItemCommentCount(itemId);
        return JsonResp.success(commentLevelCountVO);
    }

    @ApiOperation(value = "查询商品评价详情", httpMethod = "GET")
    @GetMapping("/comments")
    public JsonResp getItemComments(@ApiParam(name = "itemId", value = "商品ID", required = true)
                                        @RequestParam @NotBlank String itemId,
                                    @ApiParam(name = "level", value = "商品评价类别", required = false)
                                        @RequestParam(required = false) Integer level,
                                    @ApiParam(name = "page", value = "查询第几页", required = false)
                                        @RequestParam(defaultValue = CustomConfig.PAGE) Integer page,
                                    @ApiParam(name = "pageSize", value = "每一页显示的数目", required = false)
                                        @RequestParam(defaultValue = CustomConfig.PAGE_SIZE) Integer pageSize) {

        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("itemId", itemId);
        commentMap.put("level", level);

        PagedGridResult itemComments = itemService.getItemComments(commentMap, page, pageSize);
        return JsonResp.success(itemComments);
    }

    @ApiOperation(value = "根据关键词搜索商品", httpMethod = "GET")
    @GetMapping("/search")
    public JsonResp searchItem(
                                    @ApiParam(name = "keyword", value = "搜索关键词", required = false)
                                    @RequestParam(required = false) String keyword,
                                    @ApiParam(name = "sort",
                                            value = "根据什么排序（默认按商品名称降序，c：根据销量降序，p:根据价格升序）",
                                            required = false)
                                    @RequestParam(required = false) String sort,
                                    @ApiParam(name = "page", value = "查询第几页", required = false)
                                    @RequestParam(defaultValue = CustomConfig.PAGE) Integer page,
                                    @ApiParam(name = "pageSize", value = "每一页显示的数目", required = false)
                                    @RequestParam(defaultValue = CustomConfig.PAGE_SIZE) Integer pageSize) {


        PagedGridResult itemComments = itemService.searchItem(keyword, sort, page, pageSize);
        return JsonResp.success(itemComments);
    }

    @ApiOperation(value = "根据三级分类ID搜索商品", httpMethod = "GET")
    @GetMapping("/catItems")
    public JsonResp searchItemByThirdCat(
                               @ApiParam(name = "catId", value = "三级分类ID")
                               @RequestParam Integer catId,

                               @ApiParam(name = "sort",
                                       value = "根据什么排序（默认按商品名称降序，c：根据销量降序，p:根据价格升序）",
                                       required = false)
                               @RequestParam(required = false) String sort,

                               @ApiParam(name = "page", value = "查询第几页", required = false)
                               @RequestParam(defaultValue = CustomConfig.PAGE) Integer page,

                               @ApiParam(name = "pageSize", value = "每一页显示的数目", required = false)
                               @RequestParam(defaultValue = CustomConfig.PAGE_SIZE) Integer pageSize) {


        PagedGridResult itemComments = itemService.searchItemByThirdCat(catId, sort, page, pageSize);
        return JsonResp.success(itemComments);
    }

    @ApiOperation(value = "更新购物车商品详情", httpMethod = "GET")
    @GetMapping("/refresh")
    public JsonResp getItemsBySpecIds(
            @ApiParam(name = "itemSpecIds", value = "商品规格id数组", required = true, example = "1001,1002,1003")
            @RequestParam @NotBlank String itemSpecIds) {

        String[] ids = itemSpecIds.split(",");

        List<ShopcartVO> shopcartVOS = itemService.getItemsBySpecIds(Arrays.asList(ids));
        return JsonResp.success(shopcartVOS);
    }
}
