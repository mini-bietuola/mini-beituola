package com.netease.mini.bietuola.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netease.mini.bietuola.config.redis.RedisService;
import com.netease.mini.bietuola.config.redis.component.RedisLock;
import com.netease.mini.bietuola.config.session.SessionService;
import com.netease.mini.bietuola.constant.Constants;
import com.netease.mini.bietuola.constant.StartType;
import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.*;
import com.netease.mini.bietuola.exception.ServiceException;
import com.netease.mini.bietuola.mapper.*;
import com.netease.mini.bietuola.service.TeamService;
import com.netease.mini.bietuola.vo.TeamDetailVo;
import com.netease.mini.bietuola.web.util.DateUtil;
import com.netease.mini.bietuola.web.util.JsonResponse;
import com.netease.mini.bietuola.web.vo.CheckDetailVo;
import com.netease.mini.bietuola.web.vo.TeamBaseInfoVo;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

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
    private final CategoryMapper categoryMapper;
    private final RedisService redisService;

    public TeamServiceImpl(TeamMapper teamMapper, UserTeamMapper userTeamMapper, CheckRecordMapper checkRecordMapper, UserMapper userMapper, SessionService sessionService, CategoryMapper categoryMapper, RedisService redisService) {
        this.teamMapper = teamMapper;
        this.userTeamMapper = userTeamMapper;
        this.checkRecordMapper = checkRecordMapper;
        this.userMapper = userMapper;
        this.sessionService = sessionService;
        this.categoryMapper = categoryMapper;
        this.redisService = redisService;
    }

    @Override
    @Transactional
    public boolean save(Team team) {
        long userId = team.getCreateUserId();
        BigDecimal fee = team.getFee();
        BigDecimal amount = userMapper.getAmount(userId);
        if (amount.compareTo(fee) < 0) {
//            JsonResponse.codeOf(-1).setMsg("小组参数错误！");
            return false;
        }
        teamMapper.save(team);
        Long teamId = team.getId();
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setCreateTime(System.currentTimeMillis());
        userTeamMapper.insert(userTeam);
        BigDecimal newAmount = amount.subtract(fee);
        userMapper.updateAmount(newAmount, userId);
        return true;
    }

    @Override
    public List<TeamDetailVo> findRecuitTeamDetail(Long userId) {
        List<TeamDetailVo> teamDetailVoList = new ArrayList<>();
        List<Team> teamList = teamMapper.findTeamByActivityStatus(TeamStatus.RECUIT);
        List<Team> teamList1 = teamMapper.findTeamByActivityStatus(TeamStatus.WAITING_START);
        List<UserTeam> userTeamList = userTeamMapper.findUserTeamByUserId(userId);
        for (UserTeam userTeam : userTeamList) {
            for (Team team : teamList) {
                if (Objects.equals(userTeam.getTeamId(), team.getId())) {
                    TeamDetailVo teamDetailVo = new TeamDetailVo();
                    teamDetailVo.setTeam(team);
                    teamDetailVo.setNumOfJoin(userTeamMapper.findTeamJoinNum(team.getId()));
                    teamDetailVoList.add(teamDetailVo);
                }
            }
            for (Team team : teamList1) {
                if (Objects.equals(userTeam.getTeamId(), team.getId())) {
                    TeamDetailVo teamDetailVo = new TeamDetailVo();
                    teamDetailVo.setNumOfJoin(team.getMemberNum().longValue());
                    teamDetailVo.setTeam(team);
                    teamDetailVoList.add(teamDetailVo);
                }
            }
        }
        return teamDetailVoList;
    }

    @Override
    public List<TeamDetailVo> findProccessingTeamDetail(Long userId) {
        List<TeamDetailVo> teamDetailVoList = new ArrayList<>();
        List<Team> teamList = teamMapper.findTeamByActivityStatus(TeamStatus.PROCCESSING);
        List<UserTeam> userTeamList = userTeamMapper.findUserTeamByUserId(userId);
        for (UserTeam userTeam : userTeamList) {
            for (Team team : teamList) {
                if (Objects.equals(userTeam.getTeamId(), team.getId())) {
                    TeamDetailVo teamDetailVo = new TeamDetailVo();
                    teamDetailVo.setTeam(team);
                    teamDetailVo.setNumOfJoin(team.getMemberNum().longValue());
                    List<CheckRecord> checkRecordList = checkRecordMapper.findCheckRecordByUserTeamId(userTeam.getId());
                    for (CheckRecord checkRecord : checkRecordList) {
                        if (todayCheckRecord(System.currentTimeMillis(), checkRecord.getCheckTime(), team.getStartTime(), team.getEndTime())) {
                            teamDetailVo.setCheckRecordInfo(true);
                        }
                    }
                    Integer checkTime = checkRecordMapper.CountCheckTimeByUserId(userId, team.getId());
                    teamDetailVo.setCheckDays(checkTime.longValue());
                    teamDetailVoList.add(teamDetailVo);
                }
            }
        }
        return teamDetailVoList;
    }

    public List<TeamDetailVo> findFinishedTeamDetail(Long userId) {
        List<TeamDetailVo> teamDetailVoList = new ArrayList<>();
        List<Team> teamList = teamMapper.findTeamByActivityStatus(TeamStatus.FINISHED);
        List<UserTeam> userTeamList = userTeamMapper.findUserTeamByUserId(userId);
        for (UserTeam userTeam : userTeamList) {
            for (Team team : teamList) {
                if (Objects.equals(userTeam.getTeamId(), team.getId())) {
                    TeamDetailVo teamDetailVo = new TeamDetailVo();
                    teamDetailVo.setTeam(team);
                    teamDetailVo.setNumOfJoin(team.getMemberNum().longValue());
                    Integer checkTime = checkRecordMapper.CountCheckTimeByUserId(userId, team.getId());
                    teamDetailVo.setCheckDays(checkTime.longValue());
                    teamDetailVoList.add(teamDetailVo);
                }
            }
        }
        return teamDetailVoList;
    }

    public boolean todayCheckRecord(long currentDate, long checkTime, Integer startTime, Integer endTime) {
        //long current = System.currentTimeMillis();
        long zero = currentDate / (1000 * 24 * 3600) * (1000 * 24 * 3600) - TimeZone.getDefault().getRawOffset();
        if (checkTime >= zero + startTime * 60 * 1000 && checkTime <= zero + endTime * 60 * 1000) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkRecord(Long userId, Long teamId) {
        Team team = teamMapper.findTeamByTeamId(teamId);
        UserTeam userTeam = userTeamMapper.findUserTeamByUserIdAndTeamId(userId, teamId);
        if(userTeam==null){
            throw new ServiceException("当前用户："+userId+"未参与小组："+teamId);
        }
        Integer startTime = team.getStartTime();
        Integer endTime = team.getEndTime();
        Long startDay = team.getStartDate();
        Integer days = team.getDuration();
        long current = System.currentTimeMillis();
        long zero = current / (1000 * 24 * 3600) * (1000 * 24 * 3600) - TimeZone.getDefault().getRawOffset();
        if (current >= startDay && current <= startDay + days * 24 * 60 * 60 * 1000) {
            if (current >= zero + startTime * 60 * 1000 && current <= zero + endTime * 60 * 1000) {
                List<CheckRecord> checkRecordList = checkRecordMapper.CheckStatus(userTeam.getId(), DateUtil.getTodayStart(), DateUtil.getTodayEnd());
                if (checkRecordList.size() == 0) {
                    checkRecordMapper.save(userTeam.getId(), current);
                }
                String key = Constants.REDIS_CACHE_PREFIX + userId + "_" + teamId + "_" + DateUtil.getTodayStart();
                redisService.delete(key);

                //删除进行中小组详情的缓存
                redisService.delete(Constants.TEAM_DETAIL_PREFIX + teamId);
                return true;
            }else {
                throw new ServiceException("当前不在打卡时间段内");
            }
        }else {
            throw new ServiceException("当前不在打卡时间段内");
        }
    }

    @Override
    public boolean queryTodayCheckStatus(Long userId, Long teamId, long currentDate) {
        String key = Constants.REDIS_CACHE_PREFIX + userId + "_" + teamId + "_" + currentDate;
        if (redisService.get(key) != null) {
            return (boolean) redisService.get(key);
        } else {
            UserTeam userTeam = userTeamMapper.findUserTeamByUserIdAndTeamId(userId, teamId);
            if(userTeam==null){
                throw new ServiceException("当前用户："+userId+"未参与小组："+teamId);
            }
            List<CheckRecord> checkRecordList = checkRecordMapper.CheckStatus(userTeam.getId(), currentDate, currentDate + DateUtils.MILLIS_PER_DAY);
            if (checkRecordList.size() == 0) {
                redisService.set(key, false);
                return false;
            }
            redisService.set(key, true);
            return true;
        }
    }

    public static void main(String[] args) {
        long current = System.currentTimeMillis();
        long zero = current / (1000 * 24 * 3600) * (1000 * 24 * 3600) - TimeZone.getDefault().getRawOffset();
        System.out.println(current);
        System.out.println(zero);
    }

    @Override
    public RecomTeamResult getRecomTeam(Long categoryId, int pageNumber, int pageSize) {
        RecomTeamResult recomTeamResult = new RecomTeamResult();
        List<RecomTeamInfo> recomTeamInfos = new ArrayList<>();
        PageHelper.startPage(pageNumber, pageSize);
        List<Team> teams = new ArrayList<>();
        if (categoryId != null) {
            teams = teamMapper.getTeamByCategory(categoryId);
        } else {
            teams = teamMapper.listTeam();
        }
        if (CollectionUtils.isEmpty(teams)) {
            return null;
        }
        PageInfo<Team> pageInfo = new PageInfo<>(teams);
        recomTeamResult.setPageNumber(pageInfo.getPageNum());
        recomTeamResult.setPageSize(pageInfo.getPageSize());
        recomTeamResult.setHasPreviousPage(pageInfo.isHasPreviousPage());
        recomTeamResult.setHasNextPage(pageInfo.isHasNextPage());
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
            recomTeamInfo.setCategoryId(team.getCategoryId());
            recomTeamInfos.add(recomTeamInfo);
        }
        recomTeamResult.setTeamInfoList(recomTeamInfos);
        return recomTeamResult;
    }

    @Override
    @Transactional
    public String participateTeam(long teamId) {
        long userId = sessionService.getCurrentUserId();
        String key = Constants.REDIS_LOCK_PREFIX + teamId;
        RedisLock lock = redisService.getLock(key);
        String errMsg = "加入失败";
        boolean isLocked = lock.tryLock(10, 10);
        if (!isLocked) {
            return errMsg;
        }
        try {
            Team team = teamMapper.getTeamById(teamId);
            if (team.getActivityStatus() != TeamStatus.RECUIT) {
                return errMsg;
            }
            int currentNum = teamMapper.countMember(teamId);
            if (currentNum >= team.getMemberNum()) {
                return "当前小组人数已满";
            }
            BigDecimal fee = team.getFee();
            BigDecimal amount = userMapper.getAmount(userId);
            if (amount.compareTo(fee) < 0) {
                return "余额不足，请充值";
            }
            UserTeam userTeam = new UserTeam();
            userTeam.setUserId(userId);
            userTeam.setTeamId(teamId);
            userTeam.setCreateTime(System.currentTimeMillis());
            UserTeam result = userTeamMapper.findUserTeamByUserIdAndTeamId(userId, teamId);
            if (result != null) {
                return "你已加入过该小组";
            }
            if (userTeamMapper.insert(userTeam) != 1) {
                return errMsg;
            }
            BigDecimal newAmount = amount.subtract(fee);
            if (userMapper.updateAmount(newAmount, userId) != 1) {
                return errMsg;
            }
            if ((team.getMemberNum() - currentNum) == 1) {
                long startDate = team.getStartType() == StartType.FULL_PEOPLE ? DateUtil.getDayZeroTime(DateUtil.getTimeOffsetDays(System.currentTimeMillis(), 1)) : team.getStartDate();
                teamMapper.updateStatus(startDate, TeamStatus.WAITING_START, teamId);
            }
            redisService.delete(Constants.TEAM_DETAIL_PREFIX + teamId);
            return "";
        } finally {
            lock.unlock();
        }
    }

    @Override
    public JsonResponse getBaseInfo(Long teamId, Long userId) {
        TeamBaseInfoVo teamVo = new TeamBaseInfoVo();
        TeamBaseInfoVo redisTeamVo;
        //从缓存获得小组详情信息
        redisTeamVo = (TeamBaseInfoVo) redisService.get(Constants.TEAM_DETAIL_PREFIX + teamId);
        //如果缓存未命中，则从数据库查找数据
        if (redisTeamVo == null) {
            Team team = teamMapper.selectTeamInfoById(teamId);
            if (team == null) {
                return JsonResponse.codeOf(-1).setMsg("小组参数错误！");
            }
            //招募中需要计算当前人数
            int currentMemberNum = team.getMemberNum();
            if (team.getActivityStatus() == TeamStatus.RECUIT) {
                currentMemberNum = teamMapper.countCurrentMemberNum(teamId);
            }
            //进行中和结束状态需要查询小组成员打卡信息
            if (team.getActivityStatus() == TeamStatus.PROCCESSING || team.getActivityStatus() == TeamStatus.FINISHED) {
                List<User> userList = userMapper.getAllUserByTeamId(teamId);
                List<CheckDetailVo> checkDetailVoList = new ArrayList<CheckDetailVo>();

                int unit_score = categoryMapper.selectScoreById(team.getCategoryId());
                int totalScore = 0;
                for (User tempUser : userList) {
                    int checkTime = checkRecordMapper.CountCheckTimeByUserId(tempUser.getId(), teamId);
                    int tempScore = checkTime * unit_score;
                    checkDetailVoList.add(new CheckDetailVo(tempUser, checkTime, tempScore));
                    totalScore += tempScore;
                }
                teamVo.setCheckDetailList(checkDetailVoList);
                teamVo.setTotlescore(totalScore);
            }
            //招募中状态只查询小组成员信息
            else {
                List<User> userList = userMapper.getAllUserByTeamId(teamId);
                List<CheckDetailVo> checkDetailVoList = new ArrayList<CheckDetailVo>();
                for (User tempUser : userList) {
                    int checkTime = checkRecordMapper.CountCheckTimeByUserId(tempUser.getId(), teamId);
                    checkDetailVoList.add(new CheckDetailVo(tempUser, null, null));
                }
                teamVo.setCheckDetailList(checkDetailVoList);
            }

            teamVo.setTeam(team);
            teamVo.setCurrentMemberNum(currentMemberNum);
            //如果队伍状态为进行中，需要设置当前天数字段
            if (team.getActivityStatus() == TeamStatus.PROCCESSING) {
                long currentMillis = System.currentTimeMillis();
                int currentDay = (int) ((currentMillis - team.getStartDate()) / (1000 * 60 * 60 * 24) + 1);
                teamVo.setCurrentDay(currentDay);
            }

            //将teamVo放入redis缓存，过期时间一天
            redisService.set(Constants.TEAM_DETAIL_PREFIX + teamId, teamVo, 60 * 60 * 24);
        } else {
            teamVo = redisTeamVo;
        }
        teamVo = AssembleMyCheckInfo(teamVo, userId, teamId);

        return JsonResponse.success(teamVo);
    }

    //装配个人打卡次数、个人打卡积分及获得金钱数量
    private TeamBaseInfoVo AssembleMyCheckInfo(TeamBaseInfoVo teamVo, Long userId, Long teamId) {
        if (teamVo.getTeam().getActivityStatus() == TeamStatus.PROCCESSING || teamVo.getTeam().getActivityStatus() == TeamStatus.FINISHED) {
            for (CheckDetailVo tempCheckVo : teamVo.getCheckDetailList()) {
                if (Objects.equals(tempCheckVo.getUser().getId(), userId)) {
                    teamVo.setMyCheckTime(tempCheckVo.getCheckTime());
                    teamVo.setMyCheckscore(tempCheckVo.getScore());
                    break;
                }
            }
            if (teamVo.getTeam().getActivityStatus() == TeamStatus.FINISHED) {
                teamVo.setAwardAmount(userTeamMapper.selectAwardAmountByUserIdTeamId(userId, teamId).toString());
            }
        }
        return teamVo;
    }

    @Override
    public List<RecomTeamInfo> searchTeam(String name) {
        List<RecomTeamInfo> recomTeamInfos = new ArrayList<>();
        List<Team> teamList = new ArrayList<>();
        teamList = teamMapper.getTeamByName(name);
        if (CollectionUtils.isEmpty(teamList)) {
            return recomTeamInfos;
        }
        for (Team team : teamList) {
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
    public List<TeamDetailVo> findTeam(TeamStatus teamStatus, String name) {
        List<TeamDetailVo> teamDetailVoList = new ArrayList<>();
        List<TeamStatus> statusList = new ArrayList<>();
        statusList.add(teamStatus);
        if (teamStatus == TeamStatus.RECUIT) {
            statusList.add(TeamStatus.WAITING_START);
        }
        List<Team> teamList = teamMapper.findTeamByStatusList(statusList, name);
        List<UserTeam> userTeamList = userTeamMapper.findUserTeamByUserId(sessionService.getCurrentUserId());
        for (UserTeam userTeam : userTeamList) {
            for (Team team : teamList) {
                if (Objects.equals(userTeam.getTeamId(), team.getId())) {
                    TeamDetailVo teamDetailVo = new TeamDetailVo();
                    teamDetailVo.setTeam(team);
                    teamDetailVo.setNumOfJoin(team.getMemberNum().longValue());
                    if (team.getActivityStatus() == TeamStatus.PROCCESSING) {
                        List<CheckRecord> checkRecordList = checkRecordMapper.findCheckRecordByUserTeamId(userTeam.getId());
                        for (CheckRecord checkRecord : checkRecordList) {
                            if (todayCheckRecord(System.currentTimeMillis(), checkRecord.getCheckTime(), team.getStartTime(), team.getEndTime())) {
                                teamDetailVo.setCheckRecordInfo(true);
                            }
                        }
                    }
                    teamDetailVoList.add(teamDetailVo);
                }
            }
        }
        return teamDetailVoList;
    }

    @Override
    public List<DateCheck> getCheckLog(long teamId) {
        List<DateCheck> dateCheckList = new ArrayList<>();
        Team team = teamMapper.getTeamById(teamId);
        if (team == null || System.currentTimeMillis() < team.getStartDate()) {
            return dateCheckList;
        }
        List<User> userList = userMapper.getAllUserByTeamId(teamId);
        if (CollectionUtils.isEmpty(userList)) {
            return dateCheckList;
        }
        long endTime = team.getStartDate() + team.getDuration() * DateUtils.MILLIS_PER_DAY;
        long currentDay = System.currentTimeMillis() > endTime ? endTime : DateUtil.getDayZeroTime(System.currentTimeMillis());
        int betweenDays = DateUtil.getBetweenDays(team.getStartDate(), currentDay);
        for (int i = 0; i < betweenDays; i++) {
            DateCheck dateCheck = new DateCheck();
            long date = DateUtil.getTimeOffsetDays(team.getStartDate(), i);
            dateCheck.setCurretDate(date);
            List<CheckLog> checkLogs = new ArrayList<>();
            for (User user : userList) {
                CheckLog checkLog = new CheckLog();
                checkLog.setUserId(user.getId());
                checkLog.setUserName(user.getNickname());
                checkLog.setAvatarUrl(user.getAvatarUrl());
                checkLog.setCheckStatus(queryTodayCheckStatus(user.getId(), teamId, date));
                Long checkTime = checkRecordMapper.getCheckTime(user.getId(), teamId, date, date + DateUtils.MILLIS_PER_DAY);
                if (checkTime!=null) {
                    checkLog.setCheckTime(checkTime);
                }
                checkLogs.add(checkLog);
            }
            dateCheck.setCheckLogList(checkLogs);
            dateCheckList.add(dateCheck);
        }
        return dateCheckList;
    }

    @Override
    public List<Team> getTeamByCategory(Long categoryId) {
        List<Team> teams = new ArrayList<>();
        teams = teamMapper.getTeamByCategory(categoryId);
        return teams;
    }



}
