package com.netease.mini.bietuola.schedule;

import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.entity.User;
import com.netease.mini.bietuola.mapper.CheckRecordMapper;
import com.netease.mini.bietuola.mapper.TeamMapper;
import com.netease.mini.bietuola.mapper.UserMapper;
import com.netease.mini.bietuola.service.TeamService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhang on 2019/5/6.
 */
@Component
@Configuration
@EnableScheduling
public class ScheduleTask {

    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private CheckRecordMapper checkRecordMapper;
    @Autowired
    private UserMapper userMapper;

    @Scheduled(cron = "0/50 * * * * ?")
    public void task() {
        teamStatusChange();
        System.out.println("执行定时任务");
    }

    /**
     * 小组状态由进行变化为结束
     */
    public void teamStatusChange() {
        List<Team> teamList = teamMapper.findTeamByActivityStatus(TeamStatus.PROCCESSING);
        for (Team team : teamList) {
            //打卡开始时间
            Long timeCheck = team.getStartDate();
            //打卡天数
            Integer day = team.getDuration();
            //当前时间
            Long current = System.currentTimeMillis();
            if (current >= timeCheck + day * 24 * 60 * 60 * 1000) {
                //小组状态由进行转换为已结束
                teamMapper.updateStatus(team.getStartDate(), TeamStatus.FINISHED, team.getId());
                //资金的计算工作
                List<User> userList = userMapper.getAllUserByTeamId(team.getId());
                //小组总的打卡数
                int sum = 0;
                for (User user : userList) {
                    sum += checkRecordMapper.CountCheckTimeByUserId(user.getId(), team.getId());
                }
                if (sum != 0) {
                    for (User user : userList) {
                        int times = checkRecordMapper.CountCheckTimeByUserId(user.getId(), team.getId());
                        user.setAmount(user.getAmount().add(team.getFee().divide(new BigDecimal(sum)).multiply(new BigDecimal(times))));
                        userMapper.updateByUserId(user);
                    }
                }
            }
        }
    }
}
