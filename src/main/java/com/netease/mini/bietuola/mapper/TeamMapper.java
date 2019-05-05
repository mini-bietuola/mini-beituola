package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Team;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
