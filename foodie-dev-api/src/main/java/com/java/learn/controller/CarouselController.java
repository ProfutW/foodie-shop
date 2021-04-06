package com.java.learn.controller;

import com.java.learn.enums.YesOrNo;
import com.java.learn.pojo.Carousel;
import com.java.learn.pojo.Category;
import com.java.learn.pojo.vo.CategoryVO;
import com.java.learn.pojo.vo.RecommendCategoryVO;
import com.java.learn.service.CarouselService;
import com.java.learn.service.CategorylService;
import com.java.learn.utils.JsonResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"首页展示相关接口"})
@RestController
@RequestMapping("index")
public class CarouselController {
    private static final Logger logger = LoggerFactory.getLogger(CarouselController.class);

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategorylService categorylService;

    @ApiOperation(value = "轮播图展示接口", httpMethod = "GET")
    @GetMapping("/carousel")
    public JsonResp getAllCarousel() {
        List<Carousel> carousels = carouselService.getAllCarousel(YesOrNo.YES.type);
        return JsonResp.success(carousels);
    }

    @ApiOperation(value = "获取一级分类商品", httpMethod = "GET")
    @GetMapping("/cats")
    public JsonResp getAllRootCat() {
        List<Category> categories = categorylService.getAllRootCat();
        return JsonResp.success(categories);
    }

    @ApiOperation(value = "获取商品子分类", httpMethod = "GET")
    // 路径参数使用正则表达式的方式匹配验证，不匹配时返回404
    @GetMapping("/subCat/{rootCatId:\\d+}")
    public JsonResp getSubCat(
            @ApiParam(name = "rootCatId", value = "一级分类ID", required = true)
            @PathVariable
                    Integer rootCatId) {

        List<CategoryVO> categories = categorylService.getSubCatList(rootCatId);
        return JsonResp.success(categories);
    }

    @ApiOperation(value = "根据一级分类id查询最新的6个商品作为推荐商品", httpMethod = "GET")
    // 路径参数使用正则表达式的方式匹配验证，不匹配时返回404
    @GetMapping("/sixNewItems/{rootCatId:\\d+}")
    public JsonResp getRecommendItem(
            @ApiParam(name = "rootCatId", value = "一级分类ID", required = true)
            @PathVariable
                    Integer rootCatId) {

        List<RecommendCategoryVO> items = categorylService.getRecommendItem(rootCatId);
        return JsonResp.success(items);
    }
}
