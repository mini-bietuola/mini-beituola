package com.netease.mini.bietuola.service;

import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.vo.TeamDetailVo;
import com.netease.mini.bietuola.web.controller.query.TeamQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
}
