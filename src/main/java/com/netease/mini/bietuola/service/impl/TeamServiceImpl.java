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
import com.netease.mini.bietuola.service.TeamService;
import com.netease.mini.bietuola.vo.TeamDetailVo;
import com.netease.mini.bietuola.web.util.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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
    @Transactional
    public boolean save(Team team) {
        long userId = team.getCreateUserId();
        BigDecimal fee = team.getFee();
        BigDecimal amount = userMapper.getAmount(userId);
        if (amount.compareTo(fee) > 0) {
            BigDecimal newAmount = amount.subtract(fee);
            userMapper.updateAmount(newAmount, userId);
            return teamMapper.save(team) == 1;
        }
        return false;
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
}
