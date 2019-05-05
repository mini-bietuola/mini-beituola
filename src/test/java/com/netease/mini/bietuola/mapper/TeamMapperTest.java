package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.mapper.TeamMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhang on 2019/5/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class TeamMapperTest {
    @Autowired
    private TeamMapper teamMapper;

    @Test
    public void findTeamByActivityStatus(){
        List<Team> teamList= teamMapper.findTeamByActivityStatus(TeamStatus.RECUIT_FAILED);
        Assert.assertNotNull(teamList);
    }
}
