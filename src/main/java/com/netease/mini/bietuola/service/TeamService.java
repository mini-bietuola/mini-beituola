package com.netease.mini.bietuola.service;

import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.vo.TeamDetailVo;
import com.netease.mini.bietuola.web.controller.query.TeamQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.netease.mini.bietuola.entity.Team;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public interface TeamService {

    /**
     * 查询userId对应的招募小组详情列表
     * @param userId
     * @return
     */
    List<TeamDetailVo> findRecuitTeamDetail(Long userId);

    /**
     * 查询userId对应的进行小组详情列表
     * @param userId
     * @return
     */
    List<TeamDetailVo> findProccessingTeamDetail(Long userId);

    /**
     * 查询userId对应的已结束小组详情列表
     * @param userId
     * @return
     */
    List<TeamDetailVo> findFinishedTeamDetail(Long userId);

    /**
     * 创建小组
     * @param team
     * @return
     */
    boolean save(Team team);

    /**
     * 打卡操作
     * @param userId
     * @param teamId
     * @return
     */
    boolean checkRecord(Long userId, Long teamId);

    /**
     * 查询今日打卡情况
     * @param userId
     * @param teamId
     * @return
     */
    boolean queryTodayCheckStatus(Long userId, Long teamId);
}
