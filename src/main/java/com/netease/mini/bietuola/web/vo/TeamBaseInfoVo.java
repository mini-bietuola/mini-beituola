package com.netease.mini.bietuola.web.vo;


import com.netease.mini.bietuola.entity.Team;

import java.io.Serializable;
import java.util.List;

public class TeamBaseInfoVo implements Serializable {
    private static final long serialVersionUID = 1043132128069470716L;

    private Team team;

    /**
     * 当前小组人数
     */
    private Integer currentMemberNum;

    /**
     * 打卡成员与次数的集合
     */
    private List<CheckDetailVo> checkDetailList;

    /**
     * 我的打卡次数
     */
    private Integer myCheckTime;

    /**
     * 我的打卡积分
     */
    private Integer myCheckScore;

    /**
     * 目前天数
     */
    private Integer currentDay;

    /**
     * 团队总积分
     */
    private Integer totleScore;

    /**
     * 获得金钱
     */
    private String awardAmount;


    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    /**
     * 当前队员数量
     */
    public Integer getCurrentMemberNum() {
        return currentMemberNum;
    }

    /**
     * 当前队员数量
     */
    public void setCurrentMemberNum(Integer currentMemberNum) {
        this.currentMemberNum = currentMemberNum;
    }

    /**
     * 打卡成员与次数的集合
     */
    public List<CheckDetailVo> getCheckDetailList() {
        return checkDetailList;
    }

    /**
     * 打卡成员与次数的集合
     */
    public void setCheckDetailList(List<CheckDetailVo> checkDetailList) {
        this.checkDetailList = checkDetailList;
    }


    /**
     * 我的打卡次数
     */
    public Integer getMyCheckTime() {
        return myCheckTime;
    }

    /**
     * 我的打卡次数
     */
    public void setMyCheckTime(Integer myCheckTime) {
        this.myCheckTime = myCheckTime;
    }

    /**
     * 我的打卡积分
     */
    public Integer getMyCheckScore() {
        return myCheckScore;
    }

    /**
     * 我的打卡积分
     */
    public void setMyCheckscore(Integer myCheckScore) {
        this.myCheckScore = myCheckScore;
    }

    /**
     * 目前天数
     */
    public Integer getCurrentDay() {
        return currentDay;
    }

    /**
     * 目前天数
     */
    public void setCurrentDay(Integer currentDay) {
        this.currentDay = currentDay;
    }

    /**
     * 团队总积分
     */
    public Integer getTotleScore() {
        return totleScore;
    }

    /**
     * 团队总积分
     */
    public void setTotlescore(Integer totleScore) {
        this.totleScore = totleScore;
    }

    /**
     * 获得金钱
     */
    public String getAwardAmount() {
        return awardAmount;
    }

    /**
     * 获得金钱
     */
    public void setAwardAmount(String awardAmount) {
        this.awardAmount = awardAmount;
    }
}
