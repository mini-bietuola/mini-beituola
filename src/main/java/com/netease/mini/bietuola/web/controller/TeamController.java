package com.netease.mini.bietuola.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.netease.mini.bietuola.config.jpush.JPushService;
import com.netease.mini.bietuola.config.redis.RedisService;
import com.netease.mini.bietuola.config.session.SessionService;
import com.netease.mini.bietuola.constant.StartType;
import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Category;
import com.netease.mini.bietuola.entity.RecomTeamInfo;
import com.netease.mini.bietuola.entity.RecomTeamResult;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.exception.ServiceException;
import com.netease.mini.bietuola.service.CategoryService;
import com.netease.mini.bietuola.service.TeamService;
import com.netease.mini.bietuola.vo.TeamDetailVo;
import com.netease.mini.bietuola.web.controller.query.common.PageQuery;
import com.netease.mini.bietuola.web.util.DateUtil;
import com.netease.mini.bietuola.web.util.HttpUtils;
import com.netease.mini.bietuola.web.util.JsonResponse;
import com.netease.mini.bietuola.web.util.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private final JPushService jPushService;
    static String default_img = "https://bietuola.nos-eastchina1.126.net/dcd36fc34be747fab44dec9ccea8d813.jpg";

    public TeamController(TeamService teamService, CategoryService categoryService, SessionService sessionService, JPushService jPushService) {
        this.teamService = teamService;
        this.categoryService = categoryService;
        this.sessionService = sessionService;
        this.jPushService = jPushService;
    }

    @PostMapping
    public JsonResponse create(String name, String avatarUrl, String imgUrl, BigDecimal fee, Long startDate, Integer duration, Long startTime, Long endTime, Integer memberNum, String desc, Long categoryId, StartType startType, Long maxRecuitDate) {
        Team team = new Team();
        if (StringUtils.trim(name)==null) {
            return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("小组名不能为空");
        } else {
            if (StringUtils.isBlank(name)) {
                return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("小组名不能为特殊字符");
            } else {
                team.setName(name.trim());
            }
        }
        if (HttpUtils.checkUrl(avatarUrl)) {
            team.setAvatarUrl(avatarUrl);
        } else {
            team.setAvatarUrl(default_img);
        }
        if (HttpUtils.checkUrl(imgUrl)) {
            team.setImgUrl(imgUrl);
        }
        BigDecimal low = new BigDecimal(0);
        if (fee.compareTo(low) < 0) {
            return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("押金不能为负数");
        }
        team.setFee(fee);
        if (duration < 0) {
            return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("持续时间不能为负数");
        }
        team.setDuration(duration);
        if (startTime < endTime) {
            Integer st = DateUtil.getCurDayMinutes(startTime);
            Integer ed = DateUtil.getCurDayMinutes(endTime);
            team.setStartTime(st);
            team.setEndTime(ed);
        } else {
            return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("开始时间不能超过截止时间");
        }
        if (memberNum >= 2 && memberNum <= 100) {
            team.setMemberNum(memberNum);
        } else {
            return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("人数不符合2到100要求");
        }
        team.setDesc(desc);
        team.setActivityStatus(TeamStatus.RECUIT);
        if (categoryService.getCategory(categoryId).isEmpty()) {
            return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("小组类型不存在");
        }
        team.setCategoryId(categoryId);
        long time = System.currentTimeMillis();
        team.setCreateTime(time);
        team.setUpdateTime(time);
        team.setStartType(startType);
        if (startType == StartType.SCHEDULE) {
            if (startDate > time) {
                team.setStartDate(startDate);
            }
           else{ return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("设置时间不正确");}
        } else {
            if (maxRecuitDate < 0) {
                return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("最大招募时间不能为负数");
            }
           team.setStartDate(null);
            team.setMaxRecuitDate(maxRecuitDate);
        }
        team.setCreateUserId(sessionService.getCurrentUserId());
        if (teamService.save(team)) {
            LOG.info("创建小组");
            return JsonResponse.success().setData(team.getId());
        }
        return JsonResponse.codeOf(ResultCode.BALANCE_NOT_ENOUGH).setMsg("押金不足");
    }

    @GetMapping("/category")
    public JsonResponse listcategory() {
        List<Category> lists = categoryService.listcategory();
        if (lists != null) {
            LOG.info("显示类型");
            return JsonResponse.success(lists);
        }
        return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("显示失败");
    }

    @RequestMapping("/findRecuitTeamDetail")
    public JsonResponse findRecuitTeamDetail() {
        Long userid = sessionService.getCurrentUserId();
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.setData(teamService.findRecuitTeamDetail(userid));
        return jsonResponse;
    }

    @RequestMapping("/findProccessingTeamDetail")
    public JsonResponse findProccessingTeamDetail() {
        Long userid = sessionService.getCurrentUserId();
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.setData(teamService.findProccessingTeamDetail(userid));
        return jsonResponse;
    }

    @RequestMapping("/findFinishedTeamDetail")
    public JsonResponse findFinishedTeamDetail() {
        Long userid = sessionService.getCurrentUserId();
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.setData(teamService.findFinishedTeamDetail(userid));
        return jsonResponse;
    }

    @RequestMapping("/checkRecord")
    public JsonResponse checkRecord(Long teamId) {
        Long userid = sessionService.getCurrentUserId();
        try {
            if (teamService.checkRecord(userid, teamId)) {
                return JsonResponse.success().setMsg("打卡成功");
            }
            return JsonResponse.codeOf(ResultCode.ERROR_CHECK_RECORD_FAIL).setMsg("打卡失败");
        }catch (ServiceException e){
            return JsonResponse.codeOf(ResultCode.ERROR_EXCEPTION_GLOBAL).setMsg(e.getMessage());
        }
    }

    @RequestMapping("/queryTodayCheckStatus")
    public JsonResponse queryTodayCheckStatus(Long teamId) {
        Long userid = sessionService.getCurrentUserId();
        try {
            if (teamService.queryTodayCheckStatus(userid, teamId,DateUtil.getTodayStart())) {
                return JsonResponse.success().setMsg("今日已打卡").setData(true);
            }
            return JsonResponse.success().setMsg("今日未打卡").setData(false);
        }catch (ServiceException e){
            return JsonResponse.codeOf(ResultCode.ERROR_EXCEPTION_GLOBAL).setMsg(e.getMessage());
        }
    }

    /**
     * 获取推荐小组列表
     *
     * @param categoryId 类别ID
     * @return
     */
    @GetMapping("/recommend")
    public JsonResponse getRecomTeam(Long categoryId, int pageNumber, int pageSize) {
        if (pageNumber <= 0 || pageSize <= 0) {
            return JsonResponse.codeOf(ResultCode.ERROR_BAD_PARAMETER).setMsg("参数错误");
        }
        RecomTeamResult result = teamService.getRecomTeam(categoryId, pageNumber, pageSize);
        if (result == null) {
            return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN);
        }
        return JsonResponse.success(teamService.getRecomTeam(categoryId, pageNumber, pageSize));
    }

    /**
     * 加入小组
     *
     * @param teamId 小组ID
     * @return
     */
    @PostMapping("/participate")
    public JsonResponse participateTeam(long teamId) {
        if (teamId <= 0) {
            return JsonResponse.codeOf(ResultCode.ERROR_BAD_PARAMETER).setMsg("参数错误");
        }
        if (StringUtils.isEmpty(teamService.participateTeam(teamId))) {
            return JsonResponse.success();
        }
        return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg(teamService.participateTeam(teamId));
    }

    /**
     * 获取小组详情
     *
     * @param teamId
     * @return
     */
    @RequestMapping("/baseinfo")
    public JsonResponse getBaseInfo(Long teamId) {
        Long userId = sessionService.getCurrentUserId();
        return teamService.getBaseInfo(teamId, userId);
    }

    /**
     * 搜索小组
     *
     * @param name 小组名称
     * @return
     */
    @RequestMapping("/search")
    public JsonResponse searchTeam(String name) {
        List<RecomTeamInfo> teamList = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            teamList = teamService.searchTeam(name.trim());
        }
        return JsonResponse.success(teamList);
    }

    /**
     * 个人查询小组
     *
     * @param teamStatus 小组状态
     * @param name       小组名称
     * @return
     */
    @RequestMapping("/find")
    public JsonResponse findTeam(TeamStatus teamStatus, String name) {
        List<TeamDetailVo> teams = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            teams = teamService.findTeam(teamStatus, name.trim());
        }
        return JsonResponse.success(teams);
    }

    /**
     * 查询小组成员打卡历史
     *
     * @param teamId 小组ID
     * @return
     */
    @RequestMapping("/check-log")
    public JsonResponse getCheckLog(long teamId) {
        if (teamId <= 0) {
            return JsonResponse.codeOf(ResultCode.ERROR_BAD_PARAMETER).setMsg("参数错误");
        }
        return JsonResponse.success(teamService.getCheckLog(teamId));
    }

}
