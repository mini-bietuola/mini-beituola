package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.entity.CheckRecord;
import com.netease.mini.bietuola.mapper.CheckRecordMapper;
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
public class CheckRecordMapperTest {
    @Autowired
    private CheckRecordMapper checkRecordMapper;

    @Test
    public void findCheckRecordByUserTeamIdTest(){
        Long userTeamId = 1l;
        List<CheckRecord> checkRecordList=checkRecordMapper.findCheckRecordByUserTeamId(userTeamId);
        Assert.assertNotNull(checkRecordList);
    }
}
