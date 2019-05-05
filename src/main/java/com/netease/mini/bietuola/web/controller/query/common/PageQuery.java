package com.netease.mini.bietuola.web.controller.query.common;

import java.io.Serializable;

/**
 * @Description 封装分页查询参数
 * @Auther ctl
 * @Date 2019/4/28
 */
public class PageQuery implements Serializable {
    private static final long serialVersionUID = 6221080575231282954L;

    private int pageNumber = 1; // 页码，默认1
    private int pageSize = 5; // 每页记录数，默认10
    private String sortName; // 排序字段名称
    private String sortOrder; // 排序顺序，asc/desc
    private String searchText; // 其它查询字符串

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public String toString() {
        return "PageQuery{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", sortName='" + sortName + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", searchText='" + searchText + '\'' +
                '}';
    }
}
