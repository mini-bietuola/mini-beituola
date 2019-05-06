package com.netease.mini.bietuola.schedule;

import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.mapper.TeamMapper;
import com.netease.mini.bietuola.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zhang on 2019/5/6.
 */
@Component
@Configuration
@EnableScheduling
public class ScheduleTest {

    @Autowired
    private TeamMapper teamMapper;

    @Scheduled(cron = "0/5 * * * * ?")
    private void task() {
        System.out.println("执行定时任务");
    }

    /**
     * 小组状态由进行变化为结束
     */
    private void teamStatusChange() {
        List<Team> teamList = teamMapper.findTeamByActivityStatus(TeamStatus.PROCCESSING);
        for (Team team : teamList) {
            //打卡开始时间
            Long timeCheck = team.getStartDate();
            //打卡天数
            Integer day = team.getDuration();
            //当前时间
            Long current = System.currentTimeMillis();
            if(current>=timeCheck+day*24*60*60*1000){
                //小组状态由进行转换为已结束
                teamMapper.updateStatus(team.getStartDate(),TeamStatus.FINISHED,team.getId());
                //资金的计算工作

            }
        }
    }
}
