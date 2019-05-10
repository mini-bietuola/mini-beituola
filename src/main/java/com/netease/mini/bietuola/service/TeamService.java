package com.netease.mini.bietuola.service;

import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.*;
import com.netease.mini.bietuola.vo.TeamDetailVo;
import com.netease.mini.bietuola.web.util.JsonResponse;

import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public interface TeamService {

    /**
     * 查询userId对应的招募小组详情列表
     *
     * @param userId
     * @return
     */
    List<TeamDetailVo> findRecuitTeamDetail(Long userId);

    /**
     * 查询userId对应的进行小组详情列表
     *
     * @param userId
     * @return
     */
    List<TeamDetailVo> findProccessingTeamDetail(Long userId);

    /**
     * 查询userId对应的已结束小组详情列表
     *
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
    RecomTeamResult getRecomTeam(Long categoryId, int pageNumber, int pageSize);

    /**
     * 加入小组
     *
     * @param teamId 小组ID
     * @return
     */
    String participateTeam(long teamId);


    /**
     * 打卡操作
     *
     * @param userId
     * @param teamId
     * @return
     */
    boolean checkRecord(Long userId, Long teamId);

    /**
     * 查询今日打卡情况
     *
     * @param userId
     * @param teamId
     * @return
     */
    boolean queryTodayCheckStatus(Long userId, Long teamId);

    JsonResponse getBaseInfo(Long teamId, Long userId);

    /**
     * 查询小组
     *
     * @param name 小组名称
     * @return
     */
    List<RecomTeamInfo> searchTeam(String name);

    /**
     * 个人查询小组
     *
     * @param teamStatus 小组状态
     * @param name       小组名称
     * @return
     */
    List<TeamDetailVo> findTeam(TeamStatus teamStatus, String name);

    /**
     * 查询小组成员打卡历史
     *
     * @param teamId     小组ID
     * @param currentDay 当前第几天
     * @return
     */
    List<CheckLog> getCheckLog(long teamId, int currentDay);

    /**
     * 查询小组类别id
     *
     * @param categoryId     小组类别ID
     * @return
     */
    List<Team> getTeamByCategory(Long categoryId);







}
