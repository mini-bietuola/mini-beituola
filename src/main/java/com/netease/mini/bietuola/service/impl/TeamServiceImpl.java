package com.netease.mini.bietuola.service.impl;

import com.github.pagehelper.PageHelper;
import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.CheckRecord;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.entity.UserTeam;
import com.netease.mini.bietuola.mapper.CheckRecordMapper;
import com.netease.mini.bietuola.mapper.TeamMapper;
import com.netease.mini.bietuola.mapper.UserTeamMapper;
import com.netease.mini.bietuola.service.TeamService;
import com.netease.mini.bietuola.vo.TeamDetailVo;
import com.netease.mini.bietuola.web.controller.query.TeamQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
@Service("teamService")
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private UserTeamMapper userTeamMapper;
    @Autowired
    private CheckRecordMapper checkRecordMapper;

    @Override
    public List<TeamDetailVo> findRecuitTeamDetail(TeamQuery teamQuery) {
        List<TeamDetailVo> teamDetailVoList = new ArrayList<>();
        List<Team> teamList=teamMapper.findTeamByActivityStatus(TeamStatus.RECUIT);
        List<UserTeam> userTeamList = userTeamMapper.findUserTeamByUserId(teamQuery.getUserId());
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
    public List<TeamDetailVo> findProccessingTeamDetail(TeamQuery teamQuery) {
        List<TeamDetailVo> teamDetailVoList = new ArrayList<>();
        List<Team> teamList=teamMapper.findTeamByActivityStatus(TeamStatus.PROCCESSING);
        List<UserTeam> userTeamList = userTeamMapper.findUserTeamByUserId(teamQuery.getUserId());
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

    public List<TeamDetailVo> findFinishedTeamDetail(TeamQuery teamQuery){
        List<TeamDetailVo> teamDetailVoList = new ArrayList<>();
        List<Team> teamList=teamMapper.findTeamByActivityStatus(TeamStatus.FINISHED);
        List<UserTeam> userTeamList = userTeamMapper.findUserTeamByUserId(teamQuery.getUserId());
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

    public static void main(String[] args) {
        long current= System.currentTimeMillis();
        long zero = current/(1000*24*3600)*(1000*24*3600)- TimeZone.getDefault().getRawOffset();
        System.out.println(current);
        System.out.println(zero);
    }
}
