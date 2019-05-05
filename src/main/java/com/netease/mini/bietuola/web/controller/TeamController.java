package com.netease.mini.bietuola.web.controller;

import com.netease.mini.bietuola.service.TeamService;
import com.netease.mini.bietuola.web.controller.query.TeamQuery;
import com.netease.mini.bietuola.web.util.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @RequestMapping("/findRecuitTeamDetail")
    public JsonResponse findRecuitTeamDetail(TeamQuery teamQuery){
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.setData(teamService.findRecuitTeamDetail(teamQuery));
        return jsonResponse;
    }

    @RequestMapping("/findProccessingTeamDetail")
    public JsonResponse findProccessingTeamDetail(TeamQuery teamQuery){
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.setData(teamService.findProccessingTeamDetail(teamQuery));
        return jsonResponse;
    }

    @RequestMapping("/findFinishedTeamDetail")
    public JsonResponse findFinishedTeamDetail(TeamQuery teamQuery){
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.setData(teamService.findFinishedTeamDetail(teamQuery));
        return jsonResponse;
    }
}
