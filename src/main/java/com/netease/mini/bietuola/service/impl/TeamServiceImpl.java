package com.netease.mini.bietuola.service.impl;

import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.mapper.HelloMapper;
import com.netease.mini.bietuola.mapper.TeamMapper;
import com.netease.mini.bietuola.service.TeamService;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
@Service("TeamService")
public class TeamServiceImpl implements TeamService {
    private final TeamMapper teamMapper;

    public TeamServiceImpl(TeamMapper teamMapper) {
        this.teamMapper =teamMapper;
    }

    @Override
    public boolean save(Team team) {
        return teamMapper.save(team)==1;
    }
}
