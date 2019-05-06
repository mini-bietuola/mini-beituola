package com.netease.mini.bietuola.service;

import com.netease.mini.bietuola.entity.RecomTeamInfo;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.vo.TeamDetailVo;
import com.netease.mini.bietuola.web.controller.query.TeamQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.netease.mini.bietuola.entity.Team;

import com.netease.mini.bietuola.web.util.JsonResponse;

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
    boolean save(Team team);
    /**
     * 查询推荐小组
     *
     * @param categoryId 类别ID
     * @return
     */
    List<RecomTeamInfo> getRecomTeam(Long categoryId);

    /**
     * 加入小组
     *
     * @param teamId 小组ID
     * @return
     */
    boolean participateTeam(long teamId);


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

    JsonResponse getBaseInfo(Long teamId, Long userId);

}
