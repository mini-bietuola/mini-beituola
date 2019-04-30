package com.netease.mini.bietuola.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netease.mini.bietuola.constant.Role;
import com.netease.mini.bietuola.constant.StartType;
import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Category;
import com.netease.mini.bietuola.entity.Hello;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.service.CategoryService;
import com.netease.mini.bietuola.service.TeamService;
import com.netease.mini.bietuola.web.controller.query.HelloQuery;
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
    public TeamController(TeamService teamService,CategoryService categoryService) {
        this.teamService = teamService;
        this.categoryService =categoryService;
    }

    @PostMapping
    public JsonResponse create(String name,String avatarUrl,String imgUrl,BigDecimal fee,Long startDate,Integer duration,Integer startTime, Integer endTime,Integer memberNum,String desc,TeamStatus activityStatus,Long categoryId, StartType startType,Long createTime,Long updateTime,Long createUserId) {
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
        h.setCreateUserId(createUserId);
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
}
