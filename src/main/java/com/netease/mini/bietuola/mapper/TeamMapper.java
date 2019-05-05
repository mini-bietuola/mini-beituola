package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Team;
import org.apache.ibatis.annotations.Param;

import java.util.List;


import com.netease.mini.bietuola.entity.Team;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public interface TeamMapper {

    /**
     * 根据小组的状态查询小组详情列表
     * @param teamStatus 队伍状态信息
     * @return
     */
    List<Team> findTeamByActivityStatus(@Param("teamStatus") TeamStatus teamStatus);

    /**
     * 根据小组id查询小组详情
     * @param teamId
     * @return
     */
    Team findTeamByTeamId(Long teamId);

    /**
     * 新建小组
     * @param team
     * @return
     */
    int save(Team team);

    List<Team> listTeam();

    List<Team> getTeamByCategory(Long categoryId);

    Team getTeamById(long teamId);

    int countMember(long teamId);

    int updateStatus(@Param("startDate") long startDate, @Param("teamStatus") TeamStatus teamStatus, @Param("teamId") long teamId);
}
