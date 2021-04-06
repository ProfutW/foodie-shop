package com.java.learn.pojo.vo;

import lombok.Data;

// 商品详情VO
@Data
public class CommentLevelCountVO {
    private Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;
}
