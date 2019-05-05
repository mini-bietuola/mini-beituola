package com.netease.mini.bietuola.service.impl;

import com.netease.mini.bietuola.config.session.SessionService;
import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.CheckRecord;
import com.netease.mini.bietuola.entity.RecomTeamInfo;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.entity.UserTeam;
import com.netease.mini.bietuola.mapper.CheckRecordMapper;
import com.netease.mini.bietuola.mapper.TeamMapper;
import com.netease.mini.bietuola.mapper.UserMapper;
import com.netease.mini.bietuola.mapper.UserTeamMapper;
import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.entity.User;
import com.netease.mini.bietuola.entity.UserTeam;
import com.netease.mini.bietuola.mapper.*;
import com.netease.mini.bietuola.service.TeamService;
import com.netease.mini.bietuola.vo.TeamDetailVo;
import com.netease.mini.bietuola.web.util.DateUtil;
import com.netease.mini.bietuola.web.util.JsonResponse;
import com.netease.mini.bietuola.web.vo.CheckDetailVo;
import com.netease.mini.bietuola.web.vo.TeamBaseInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
@Service("teamService")
public class TeamServiceImpl implements TeamService {
    private final TeamMapper teamMapper;
    private final UserTeamMapper userTeamMapper;
    private final CheckRecordMapper checkRecordMapper;
    private final UserMapper userMapper;
    private final SessionService sessionService;

    public TeamServiceImpl(TeamMapper teamMapper, UserTeamMapper userTeamMapper, CheckRecordMapper checkRecordMapper, UserMapper userMapper, SessionService sessionService) {
        this.teamMapper =teamMapper;
        this.userTeamMapper = userTeamMapper;
        this.checkRecordMapper = checkRecordMapper;
        this.userMapper=userMapper;
        this.sessionService = sessionService;
    }

    @Override
    public boolean save(Team team) {
        return teamMapper.save(team)==1;
    }

    @Override
    public List<TeamDetailVo> findRecuitTeamDetail(Long userId) {
        List<TeamDetailVo> teamDetailVoList = new ArrayList<>();
        List<Team> teamList=teamMapper.findTeamByActivityStatus(TeamStatus.RECUIT);
        List<UserTeam> userTeamList = userTeamMapper.findUserTeamByUserId(userId);
        for(UserTeam userTeam:userTeamList){
            for(Team team:teamList){
                if(userTeam.getTeamId()==team.getId()){
                   TeamDetailVo teamDetailVo =new TeamDetailVo();
                   teamDetailVo.setTeam(team);
                   teamDetailVo.setNumOfJoin(userTeamMapper.findTeamJoinNum(team.getId()));
                   teamDetailVoList.add(teamDetailVo);
                }
            }
        }
        return teamDetailVoList;
    }

    @Override
    public List<TeamDetailVo> findProccessingTeamDetail(Long userId) {
        List<TeamDetailVo> teamDetailVoList = new ArrayList<>();
        List<Team> teamList=teamMapper.findTeamByActivityStatus(TeamStatus.PROCCESSING);
        List<UserTeam> userTeamList = userTeamMapper.findUserTeamByUserId(userId);
        for(UserTeam userTeam:userTeamList){
            for(Team team:teamList){
                if(userTeam.getTeamId()==team.getId()){
                    TeamDetailVo teamDetailVo =new TeamDetailVo();
                    teamDetailVo.setTeam(team);
                    teamDetailVo.setNumOfJoin(team.getMemberNum().longValue());
                    List<CheckRecord> checkRecordList=checkRecordMapper.findCheckRecordByUserTeamId(userTeam.getId());
                    for(CheckRecord checkRecord: checkRecordList){
                        if(todayCheckRecord(checkRecord.getCheckTime(),team.getStartTime(),team.getEndTime())){
                            teamDetailVo.setCheckRecordInfo(true);
                        }
                    }
                    teamDetailVoList.add(teamDetailVo);
                }
            }
        }
        return teamDetailVoList;
    }

    public List<TeamDetailVo> findFinishedTeamDetail(Long userId){
        List<TeamDetailVo> teamDetailVoList = new ArrayList<>();
        List<Team> teamList=teamMapper.findTeamByActivityStatus(TeamStatus.FINISHED);
        List<UserTeam> userTeamList = userTeamMapper.findUserTeamByUserId(userId);
        for(UserTeam userTeam:userTeamList){
            for(Team team:teamList){
                if(userTeam.getTeamId()==team.getId()){
                    TeamDetailVo teamDetailVo =new TeamDetailVo();
                    teamDetailVo.setTeam(team);
                    teamDetailVo.setNumOfJoin(team.getMemberNum().longValue());
                    teamDetailVoList.add(teamDetailVo);
                }
            }
        }
        return teamDetailVoList;
    }

    public boolean todayCheckRecord(long checkTime,Integer startTime, Integer endTime){
        long current= System.currentTimeMillis();
        long zero = current/(1000*24*3600)*(1000*24*3600)- TimeZone.getDefault().getRawOffset();
        if(checkTime>=zero+startTime*60*1000&&checkTime<=zero+endTime*60*1000){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkRecord(Long userId, Long teamId) {
        Team team = teamMapper.findTeamByTeamId(teamId);
        UserTeam userTeam = userTeamMapper.findUserTeamByUserIdAndTeamId(userId,teamId);
        Integer startTime = team.getStartTime();
        Integer endTime = team.getEndTime();
        long current = System.currentTimeMillis();
        long zero = current/(1000*24*3600)*(1000*24*3600)- TimeZone.getDefault().getRawOffset();
        if(current>=zero+startTime*60*1000&&current<=zero+endTime*60*1000){
            List<CheckRecord> checkRecordList = checkRecordMapper.CheckStatus(userTeam.getId(),DateUtil.getTodayStart(),DateUtil.getTodayEnd());
            if(checkRecordList.size()==0){
                checkRecordMapper.save(teamId,current);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean queryTodayCheckStatus(Long userId, Long teamId) {
        UserTeam userTeam = userTeamMapper.findUserTeamByUserIdAndTeamId(userId,teamId);
        List<CheckRecord> checkRecordList = checkRecordMapper.CheckStatus(userTeam.getId(),DateUtil.getTodayStart(),DateUtil.getTodayEnd());
        if(checkRecordList.size()==0){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        long current= System.currentTimeMillis();
        long zero = current/(1000*24*3600)*(1000*24*3600)- TimeZone.getDefault().getRawOffset();
        System.out.println(current);
        System.out.println(zero);
    }

    @Override
    public List<RecomTeamInfo> getRecomTeam(Long categoryId) {
        List<RecomTeamInfo> recomTeamInfos = new ArrayList<>();
        List<Team> teams = new ArrayList<>();
        if (categoryId != null) {
            teams = teamMapper.getTeamByCategory(categoryId);
        } else {
            teams = teamMapper.listTeam();
        }
        if (CollectionUtils.isEmpty(teams)) {
            return recomTeamInfos;
        }
        for (Team team : teams) {
            RecomTeamInfo recomTeamInfo = new RecomTeamInfo();
            int currentNum = teamMapper.countMember(team.getId());
            recomTeamInfo.setId(team.getId());
            recomTeamInfo.setName(team.getName());
            recomTeamInfo.setActivityStatus(team.getActivityStatus());
            recomTeamInfo.setDuration(team.getDuration());
            recomTeamInfo.setCurrentNum(currentNum);
            recomTeamInfo.setAvatarUrl(team.getAvatarUrl());
            recomTeamInfo.setImgUrl(team.getImgUrl());
            recomTeamInfo.setFee(team.getFee());
            recomTeamInfo.setMemberNum(team.getMemberNum());
            recomTeamInfo.setDesc(team.getDesc());
            recomTeamInfos.add(recomTeamInfo);
        }
        return recomTeamInfos;
    }

    @Override
    public boolean participateTeam(long teamId) {
        Team team = teamMapper.getTeamById(teamId);
        int currentNum = teamMapper.countMember(teamId);
        if (currentNum == team.getMemberNum()) {
            return false;
        }
        UserTeam userTeam = new UserTeam();
        long userId=sessionService.getCurrentUserId();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setCreateTime(System.currentTimeMillis());
        if (userTeamMapper.insert(userTeam) != 1) {
            return false;
        }
        BigDecimal fee = team.getFee();
        BigDecimal amount = userMapper.getAmount(userId);
        if(amount.compareTo(fee)<0){
            return false;
        }
        BigDecimal newAmount = amount.subtract(fee);
        if (userMapper.updateAmount(newAmount, userId) != 1) {
            return false;
        }
        if ((team.getMemberNum() - currentNum) == 1) {
            long startDate= DateUtil.getDayZeroTime(DateUtil.getTimeOffsetDays(System.currentTimeMillis(),1));
            teamMapper.updateStatus(startDate, TeamStatus.PROCCESSING, teamId);
        }
        return true;
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
