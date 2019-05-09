/**
 * @(#)RecomTeamResult.java, 2019-05-09.
 * <p/>
 * Copyright 2019 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.mini.bietuola.entity;

import java.util.List;

/**
 * @author hanbing(hanbing5 @ corp.netease.com)
 */
public class RecomTeamResult {
    /**
     * 当前页码
     */
    private int pageNumber;

    /**
     * 每页记录数
     */
    private int pageSize;

    /**
     * 是否有前一页
     */
    private boolean hasPreviousPage;

    /**
     * 是否有下一页
     */
    private boolean hasNextPage;

    /**
     * 返回结果
     */
    private List<RecomTeamInfo> teamInfoList;

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

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<RecomTeamInfo> getTeamInfoList() {
        return teamInfoList;
    }

    public void setTeamInfoList(List<RecomTeamInfo> teamInfoList) {
        this.teamInfoList = teamInfoList;
    }
}