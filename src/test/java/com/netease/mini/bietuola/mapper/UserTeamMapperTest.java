package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.entity.UserTeam;
import com.netease.mini.bietuola.mapper.UserTeamMapper;
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
public class UserTeamMapperTest {

    @Autowired
    private UserTeamMapper userTeamMapper;

    @Test
    public void findUserTeamByTeamIdTest() {
        Long userId = 1l;
        List<UserTeam> userTeamList = userTeamMapper.findUserTeamByUserId(userId);
        Assert.assertNotNull(userTeamList);
    }

    @Test
    public void findTeamJoinNum(){
        Long teamId = 1l;
        long num= userTeamMapper.findTeamJoinNum(teamId);
        Assert.assertNotNull(num);
    }
}
