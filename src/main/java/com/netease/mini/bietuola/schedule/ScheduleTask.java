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
import java.util.Date;
import java.util.List;

/**
 * Created by zhang on 2019/5/6.
 */
@Component
public class ScheduleTask {

    private final TeamMapper teamMapper;
    private final CheckRecordMapper checkRecordMapper;
    private final UserMapper userMapper;

    @Autowired
    public ScheduleTask(TeamMapper teamMapper, CheckRecordMapper checkRecordMapper, UserMapper userMapper) {
        this.teamMapper = teamMapper;
        this.checkRecordMapper = checkRecordMapper;
        this.userMapper = userMapper;
    }

    @Scheduled(cron = "30 0 0 * * ?")
    public void task() {
        teamStatusChange();
        System.out.println("进行中-->已结束，执行定时任务");
    }

    /**
     * 小组状态由进行变化为结束
     */
    public void teamStatusChange() {
        // todo 记录数过多时的优化处理
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
                List<User> userList = userMapper.getAllUserByTeamId(team.getId()); // todo 只查询必要字段
                //小组总的打卡数
                int sum = 0;
                for (User user : userList) {
                    sum += checkRecordMapper.CountCheckTimeByUserId(user.getId(), team.getId()); // todo 提前保存每个用户的打卡次数
                }
                if (sum != 0) {
                    for (User user : userList) {
                        int times = checkRecordMapper.CountCheckTimeByUserId(user.getId(), team.getId());
                        // todo 乘以总人数
                        user.setAmount(user.getAmount().add(team.getFee().divide(new BigDecimal(sum)).multiply(new BigDecimal(times))));
                        userMapper.updateByUserId(user); // todo 单独更新钱
                    }
                }
            }
        }
    }

}
