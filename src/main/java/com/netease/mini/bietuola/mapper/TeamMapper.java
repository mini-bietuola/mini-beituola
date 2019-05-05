package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.entity.Team;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public interface TeamMapper {

    Team selectTeamInfoById(Long teamId);

    int countCurrentMemberNum(Long teamId);

}
