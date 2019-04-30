package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Team;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public interface TeamMapper {
    /**
     * @param id
     * @param teamStatus
     * @return
     */
    Team findTeamByTeamIdAndActivityStatus(Long id, TeamStatus teamStatus);
}
