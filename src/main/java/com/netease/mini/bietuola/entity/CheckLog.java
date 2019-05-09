/**
 * @(#)CheckLog.java, 2019-05-09.
 * <p/>
 * Copyright 2019 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.mini.bietuola.entity;

/**
 * @author hanbing(hanbing5 @ corp.netease.com)
 */
public class CheckLog {
    /**
     * 用户ID
     */
    private long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像Url
     */
    private String avatarUrl;

    /**
     * 打卡状态（true 已打卡 false 未打卡）
     */
    private boolean checkStatus;

    /**
     * 打卡时间
     */
    private String checkTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }
}