package com.netease.mini.bietuola.service.impl;

import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.entity.User;
import com.netease.mini.bietuola.entity.UserTeam;
import com.netease.mini.bietuola.mapper.*;
import com.netease.mini.bietuola.service.TeamService;
import com.netease.mini.bietuola.web.util.JsonResponse;
import com.netease.mini.bietuola.web.vo.CheckDetailVo;
import com.netease.mini.bietuola.web.vo.TeamBaseInfoVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
@Service("TeamService")
public class TeamServiceImpl implements TeamService {

    private TeamMapper teamMapper;
    private UserMapper userMapper;
    private CheckRecordMapper checkRecordMapper;
    private CategoryMapper categoryMapper;
    private UserTeamMapper userTeamMapper;

    public TeamServiceImpl(TeamMapper teamMapper, UserTeamMapper userTeamMapper, CheckRecordMapper checkRecordMapper, CategoryMapper categoryMapper, UserMapper userMapper) {
        this.teamMapper = teamMapper;
        this.userMapper = userMapper;
        this.checkRecordMapper = checkRecordMapper;
        this.categoryMapper = categoryMapper;
        this.userTeamMapper = userTeamMapper;
    }

    @Override
    public JsonResponse getBaseInfo(Long teamId, Long userId) {
        Team team = teamMapper.selectTeamInfoById(teamId);
        TeamBaseInfoVo teamVo = new TeamBaseInfoVo();
        //招募中需要计算当前人数
        int currentMemberNum = team.getMemberNum();
        if (team.getActivityStatus() == TeamStatus.RECUIT) {
            currentMemberNum = teamMapper.countCurrentMemberNum(teamId);
        }
        //如果队伍状态为进行中，需要设置当前天数字段
        if (team.getActivityStatus() == TeamStatus.PROCCESSING) {
            long currentMillis = System.currentTimeMillis();
            int currentDay = (int)((currentMillis - team.getStartDate())/(1000 * 60 * 60 * 24) + 1);
            teamVo.setCurrentDay(currentDay);
        }

        teamVo.setTeam(team);
        teamVo.setCurrentMemberNum(currentMemberNum);

        List<User> userList = userMapper.getAllUserByTeamId(teamId);
        List<CheckDetailVo> checkDetailVoList = new ArrayList<CheckDetailVo>();

        int unit_score = categoryMapper.selectScoreById(team.getCategoryId());
        int myCheckTime = 0;
        int myScore = 0;
        int totalScore = 0;
        for (User tempUser : userList) {
            int checkTime = checkRecordMapper.CountCheckTimeByUserId(tempUser.getId(), teamId);
            int tempScore = checkTime * unit_score;
            if(tempUser.getId() == userId){
                myCheckTime = checkTime;
                myScore = tempScore;
            } else {
                checkDetailVoList.add(new CheckDetailVo(tempUser, checkTime, tempScore));
            }
            totalScore += tempScore;
        }
        teamVo.setCheckDetailList(checkDetailVoList);
        teamVo.setMyCheckTime(myCheckTime);
        teamVo.setMyCheckscore(myScore);
        teamVo.setTotlescore(totalScore);
        teamVo.setAwardAmount(userTeamMapper.selectAwardAmountByUserIdTeamId(userId, teamId).toString());

        return JsonResponse.success(teamVo);
    }


}
