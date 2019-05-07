package com.netease.mini.bietuola.schedule;

import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.User;
import com.netease.mini.bietuola.mapper.CheckRecordMapper;
import com.netease.mini.bietuola.mapper.TeamMapper;
import com.netease.mini.bietuola.mapper.UserMapper;
import com.netease.mini.bietuola.mapper.UserTeamMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhang on 2019/5/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@Transactional
public class ScheduleTaskTest {
    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserTeamMapper userTeamMapper;
    @Autowired
    private CheckRecordMapper checkRecordMapper;
    @Autowired
    private ScheduleTask scheduleTask;

    @Before
    public void before(){
        User user1 = new User();
    }

    @Test
    public void teamStatusChangeTest() throws InterruptedException {
        TeamStatus teamStatus = teamMapper.getTeamById(1l).getActivityStatus();
        scheduleTask.teamStatusChange();
        Thread.sleep(1000*10l);
        TeamStatus teamStatus1 =teamMapper.getTeamById(1l).getActivityStatus();
    }

    public static void main(String[] args) {
        Date date= new Date(1557037942100l);
        System.out.println(date);
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime().getTime());
        calendar.set(2019,3,28);
        System.out.println(calendar.getTime().getTime());
        calendar.set(2019,4,2);
        System.out.println(calendar.getTime().getTime());
        calendar.set(2019,4,5);
        System.out.println(calendar.getTime().getTime());
    }

    @Test
    public void testChangeRecuit() {
        teamMapper.changeRecuitToFailForFullPeople(System.currentTimeMillis());
    }
}
