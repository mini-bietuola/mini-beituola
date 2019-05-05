package com.netease.mini.bietuola.web.vo;

import com.netease.mini.bietuola.entity.User;

import java.io.Serializable;

public class CheckDetailVo implements Serializable {
    private static final long serialVersionUID = 3407023874601594579L;

    //小组里某个成员
    private User user;

    //打卡次数
    private Integer checkTime;

    //个人积分
    private Integer score;

    public CheckDetailVo(User user, Integer checkTime, Integer score) {
        this.user = user;
        this.checkTime = checkTime;
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Integer checkTime) {
        this.checkTime = checkTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
