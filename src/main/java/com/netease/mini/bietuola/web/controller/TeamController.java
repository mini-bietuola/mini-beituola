package com.netease.mini.bietuola.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netease.mini.bietuola.config.redis.RedisService;
import com.netease.mini.bietuola.config.session.SessionService;
import com.netease.mini.bietuola.constant.StartType;
import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Category;
import com.netease.mini.bietuola.entity.RecomTeamInfo;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.service.CategoryService;
import com.netease.mini.bietuola.service.TeamService;
import com.netease.mini.bietuola.web.controller.query.common.PageQuery;
import com.netease.mini.bietuola.web.util.JsonResponse;
import com.netease.mini.bietuola.web.util.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private final CategoryService categoryService;
    private final SessionService sessionService;

    public TeamController(TeamService teamService, CategoryService categoryService, SessionService sessionService) {
        this.teamService = teamService;
        this.categoryService =categoryService;
        this.sessionService = sessionService;
    }

    @PostMapping
    public JsonResponse create(String name,String avatarUrl,String imgUrl,BigDecimal fee,Long startDate,Integer duration,Integer startTime, Integer endTime,Integer memberNum,String desc,TeamStatus activityStatus,Long categoryId, StartType startType,Long createTime,Long updateTime) {
        Team h=new Team();
        h.setName(name);
        h.setAvatarUrl(avatarUrl);
        h.setImgUrl(imgUrl);
        h.setFee(fee);
        h.setStartDate(startDate);
        h.setDuration(duration);
        h.setStartTime(startTime);
        h.setEndTime(endTime);
        h.setMemberNum(memberNum);
        h.setDesc(desc);
        h.setActivityStatus(activityStatus);
        h.setCategoryId(categoryId);
        h.setStartType(startType);
        if(h.getStartType()==StartType.FULL_PEOPLE){
            h.setStartTime(null);
            h.setEndTime(null);
        }
        h.setStartTime(startTime);
        h.setEndTime(endTime);
        h.setCreateTime(createTime);
        h.setUpdateTime(updateTime);
        h.setCreateUserId(sessionService.getCurrentUserId());
        if (teamService.save(h)) {
            LOG.info("创建小组");
            return JsonResponse.success();
        }
        return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("保存失败");
    }

    @GetMapping("/category")
    public JsonResponse listcategory() {
        List<Category> lists=categoryService.listcategory();
        if (lists!=null) {
            LOG.info("显示类型");
            return JsonResponse.success(lists);
        }
        return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("显示失败");
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

    @RequestMapping("/checkRecord")
    public JsonResponse checkRecord(Long teamId){
        Long userid = sessionService.getCurrentUserId();
        if(teamService.checkRecord(userid,teamId)){
            return JsonResponse.success().setMsg("打卡成功");
        }
        return JsonResponse.codeOf(ResultCode.ERROR_CHECK_RECORD_FAIL).setMsg("打卡失败");
    }

    @RequestMapping("/queryTodayCheckStatus")
    public JsonResponse queryTodayCheckStatus(Long teamId){
        Long userid = sessionService.getCurrentUserId();
        if(teamService.queryTodayCheckStatus(userid,teamId)){
            return JsonResponse.success().setMsg("今日已打卡").setData(true);
        }
        return JsonResponse.success().setMsg("今日未打卡").setData(false);
    }
    /**
     * 获取推荐小组列表
     *
     * @param categoryId 类别ID
     * @return
     */
    @GetMapping("/recommend")
    public JsonResponse getRecomTeam(Long categoryId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNumber(), pageQuery.getPageSize());
        List<RecomTeamInfo> teamInfos = new ArrayList<>();
        teamInfos=teamService.getRecomTeam(categoryId);
        PageInfo<RecomTeamInfo> result = new PageInfo<>(teamInfos);
        return JsonResponse.success(result);
    }

    /**
     * 加入小组
     *
     * @param teamId 小组ID
     * @return
     */
    @PostMapping("/participate")
    public JsonResponse participateTeam(long teamId){
        if(teamId<=0){
            return JsonResponse.codeOf(ResultCode.ERROR_BAD_PARAMETER).setMsg("参数错误");
        }
        if(teamService.participateTeam(teamId)){
            return JsonResponse.success();
        }
        return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("加入失败");
    }

    /**
     * 获取小组详情
     * @param teamId
     * @return
     */
    @RequestMapping("/baseinfo")
    public JsonResponse getBaseInfo(Long teamId){
        Long userId = sessionService.getCurrentUserId();
        return teamService.getBaseInfo(teamId, userId);
    }

}
