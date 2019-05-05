package com.netease.mini.bietuola.service;

import com.netease.mini.bietuola.entity.CheckRecord;
import com.netease.mini.bietuola.mapper.CheckRecordMapper;
import com.netease.mini.bietuola.vo.TeamDetailVo;
import com.netease.mini.bietuola.web.controller.query.TeamQuery;
import com.netease.mini.bietuola.web.util.DateUtil;
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
    @Autowired
    private CheckRecordMapper checkRecordMapper;

    @Test
    public void findRecuitTeamDetailTest(){
        TeamQuery teamQuery =new TeamQuery();
        teamQuery.setUserId(1l);
        List<TeamDetailVo> teamDetailVoList=teamService.findRecuitTeamDetail(1l);
        Assert.assertNotNull(teamDetailVoList);
    }

    @Test
    public void findProccessingTeamDetailTest(){
        TeamQuery teamQuery =new TeamQuery();
        teamQuery.setUserId(1l);
        List<TeamDetailVo> teamDetailVoList=teamService.findProccessingTeamDetail(1l);
        Assert.assertNotNull(teamDetailVoList);
    }

    @Test
    public void findFinishedTeamDetailTest(){
        TeamQuery teamQuery =new TeamQuery();
        teamQuery.setUserId(1l);
        List<TeamDetailVo> teamDetailVoList=teamService.findFinishedTeamDetail(1l);
        Assert.assertNotNull(teamDetailVoList);
    }

    @Test
    public void checkRecord(){
        Long teamId = 1l;
        Long userId = 1l;
        Long userTeamId = 1l;
        boolean b=teamService.checkRecord(userId,teamId);
        List<CheckRecord> checkRecordList = checkRecordMapper.CheckStatus(userTeamId, DateUtil.getTodayStart(),DateUtil.getTodayEnd());
        Assert.assertEquals(false,b);
    }
}
