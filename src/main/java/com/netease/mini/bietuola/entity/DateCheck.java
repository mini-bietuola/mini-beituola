/**
 * @(#)DateCheck.java, 2019-05-09.
 * <p/>
 * Copyright 2019 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.mini.bietuola.entity;

import java.util.List;

/**
 * @author hanbing(hanbing5 @ corp.netease.com)
 */
public class DateCheck {
    /**
     * 当前日期
     */
    private long currentDate;

    /**
     * 打卡记录列表
     */
    private List<CheckLog> checkLogList;


    public long getCurretDate() {
        return currentDate;
    }

    public void setCurretDate(long curretDate) {
        this.currentDate = curretDate;
    }

    public List<CheckLog> getCheckLogList() {
        return checkLogList;
    }

    public void setCheckLogList(List<CheckLog> checkLogList) {
        this.checkLogList = checkLogList;
    }
}