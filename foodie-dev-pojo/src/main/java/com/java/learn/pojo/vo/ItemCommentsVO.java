package com.java.learn.pojo.vo;

import lombok.Data;

import java.util.Date;

// 商品详情VO
@Data
public class ItemCommentsVO {
    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nickName;
}
