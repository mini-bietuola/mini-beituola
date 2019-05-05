package com.netease.mini.bietuola.web.controller;

import com.netease.mini.bietuola.config.session.SessionService;
import com.netease.mini.bietuola.entity.User;
import com.netease.mini.bietuola.service.TeamService;
import com.netease.mini.bietuola.web.controller.query.TeamQuery;
import com.netease.mini.bietuola.web.util.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private SessionService sessionService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @RequestMapping("/findRecuitTeamDetail")
    public JsonResponse findRecuitTeamDetail(){
        Long userid = sessionService.getCurrentUserId();
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.setData(teamService.findRecuitTeamDetail(userid));
        return jsonResponse;
    }

    @RequestMapping("/findProccessingTeamDetail")
    public JsonResponse findProccessingTeamDetail(){
        Long userid = sessionService.getCurrentUserId();
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.setData(teamService.findProccessingTeamDetail(userid));
        return jsonResponse;
    }

    @RequestMapping("/findFinishedTeamDetail")
    public JsonResponse findFinishedTeamDetail(){
        Long userid = sessionService.getCurrentUserId();
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.setData(teamService.findFinishedTeamDetail(userid));
        return jsonResponse;
    }
}
