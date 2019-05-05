package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.RecomTeamInfo;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.entity.UserTeam;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Team;

import java.util.List;


import com.netease.mini.bietuola.entity.Team;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public interface TeamMapper {

    /**
     * @param teamStatus 队伍状态信息
     * @return
     */
    List<Team> findTeamByActivityStatus(TeamStatus teamStatus);
    int save(Team team);

    List<Team> listTeam();

    List<Team> getTeamByCategory(Long categoryId);

    Team getTeamById(long teamId);

    int countMember(long teamId);

    int updateStatus(@Param("startDate") long startDate, @Param("teamStatus") TeamStatus teamStatus, @Param("teamId") long teamId);
}
