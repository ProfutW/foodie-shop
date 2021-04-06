package com.java.learn.service.impl;

import com.github.pagehelper.PageInfo;
import com.java.learn.utils.PagedGridResult;

import java.util.List;

public class BaseService {

    /**
     * 入参是分页查询的原生结果集
     * 调用api获取页数、总数等信息
     * @param list
     * @param page
     * @return
     */
    public PagedGridResult setPageGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
