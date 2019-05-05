package com.netease.mini.bietuola.vo;

import com.netease.mini.bietuola.entity.Team;

/**
 * Created by zhang on 2019/5/2.
 */
public class TeamDetailVo {
    /**
     * 小组信息
     */
    private Team team;
    /**
     * 当前参与人数
     */
    private Long numOfJoin;
    /**
     * 今日是否打卡，默认没有打卡
     */
    private Boolean checkRecordInfo=false;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Long getNumOfJoin() {
        return numOfJoin;
    }

    public void setNumOfJoin(Long numOfJoin) {
        this.numOfJoin = numOfJoin;
    }

    public Boolean isCheckRecordInfo() {
        return checkRecordInfo;
    }

    public void setCheckRecordInfo(boolean checkRecordInfo) {
        this.checkRecordInfo = checkRecordInfo;
    }
}
