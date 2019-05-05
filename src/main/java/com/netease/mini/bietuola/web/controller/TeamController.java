package com.netease.mini.bietuola.web.controller;

import com.netease.mini.bietuola.entity.User;
import com.netease.mini.bietuola.entity.UserTeam;
import com.netease.mini.bietuola.service.TeamService;
import com.netease.mini.bietuola.web.util.JsonResponse;
import org.apache.catalina.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
@RestController
@RequestMapping("/api/team")
public class TeamController {
    private static final Logger LOG = LoggerFactory.getLogger(TeamController.class);
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @RequestMapping("/baseinfo")
    public JsonResponse getBaseInfo(Long teamId, Long userId){
        return teamService.getBaseInfo(teamId, userId);
    }

    /*@RequestMapping("/checkdetail")
    public JsonResponse getCheckDetail(Integer teamId) {
        //后期改成从redis中获取，此处为模拟
        User user = null;
        Integer userId = 1;
        return teamService.getCheckDetail(teamId, userId);
    }*/
}
