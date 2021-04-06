package com.java.learn.pojo.vo;

import com.java.learn.pojo.Items;
import com.java.learn.pojo.ItemsImg;
import com.java.learn.pojo.ItemsParam;
import com.java.learn.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

// 商品详情VO
@Data
public class ItemVO {
    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;
}
