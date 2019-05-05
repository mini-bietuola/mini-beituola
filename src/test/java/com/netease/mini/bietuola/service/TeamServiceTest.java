package com.netease.mini.bietuola.service;

import com.netease.mini.bietuola.vo.TeamDetailVo;
import com.netease.mini.bietuola.web.controller.query.TeamQuery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhang on 2019/5/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Test
    public void findRecuitTeamDetailTest(){
        TeamQuery teamQuery =new TeamQuery();
        teamQuery.setUserId(1l);
        List<TeamDetailVo> teamDetailVoList=teamService.findRecuitTeamDetail(teamQuery);
        Assert.assertNotNull(teamDetailVoList);
    }

    @Test
    public void findProccessingTeamDetailTest(){
        TeamQuery teamQuery =new TeamQuery();
        teamQuery.setUserId(1l);
        List<TeamDetailVo> teamDetailVoList=teamService.findProccessingTeamDetail(teamQuery);
        Assert.assertNotNull(teamDetailVoList);
    }

    @Test
    public void findFinishedTeamDetailTest(){
        TeamQuery teamQuery =new TeamQuery();
        teamQuery.setUserId(1l);
        List<TeamDetailVo> teamDetailVoList=teamService.findFinishedTeamDetail(teamQuery);
        Assert.assertNotNull(teamDetailVoList);
    }
}
