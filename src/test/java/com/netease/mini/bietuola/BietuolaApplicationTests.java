package com.netease.mini.bietuola;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netease.mini.bietuola.constant.Role;
import com.netease.mini.bietuola.constant.StartType;
import com.netease.mini.bietuola.constant.TeamStatus;
import com.netease.mini.bietuola.entity.Category;
import com.netease.mini.bietuola.entity.Hello;
import com.netease.mini.bietuola.entity.Team;
import com.netease.mini.bietuola.mapper.CategoryMapper;
import com.netease.mini.bietuola.mapper.HelloMapper;
import com.netease.mini.bietuola.mapper.TeamMapper;
import com.netease.mini.bietuola.web.controller.query.HelloQuery;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BietuolaApplicationTests {

    @Autowired
    private HelloMapper helloMapper;
    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private CategoryMapper categoryMapper;
	@Test
	public void contextLoads() {
	}

	@Test
    public void testHello() {
//        System.out.println(helloMapper.getHelloByName("tom"));
        System.out.println(helloMapper.listHello());
    }

    @Test
    public void testHello1() {
//        System.out.println(helloMapper.getHelloByName("tom"));
        Hello hello=new Hello();
        hello.setName("fang");
        hello.setAge(11);
        hello.setCreateTime(Long.parseLong("1111"));
        hello.setPrice(50);
        hello.setRole(Role.ADMIN);
        System.out.println(helloMapper.save(hello));
    }
    @Test
    public void testPageHelper() {
        HelloQuery helloQuery = new HelloQuery();
        // 设置查询参数以及分页参数
        helloQuery.setPageNumber(2);
        helloQuery.setPageSize(2);
//        helloQuery.setName("tom");

        // 分页演示，如果要分页，按照以下三个步骤（注意：1、2两步骤要不都执行，要不都不执行）
        // 1、设置分页参数
        PageHelper.startPage(helloQuery.getPageNumber(), helloQuery.getPageSize());
        // 2、执行mybatis查询
        List<Hello> lists;
        if (StringUtils.isNotBlank(helloQuery.getName()))
        {
            lists = helloMapper.getHelloByName(helloQuery.getName());
        }
        else
        {
            lists = helloMapper.listHello();
        }
        // 3、包装分页结果
        PageInfo<Hello> result = new PageInfo<>(lists);
        System.out.println(result);
    }
    @Test
    public void testfindByName(){
	    System.out.println(helloMapper.getHelloByName("tom"));
    }


    @Test
    public void insertTeam(){
	    Team h=new Team();
        h.setName("早起小队");
        h.setAvatarUrl("1231313");
        h.setImgUrl("1313131");
        h.setFee(BigDecimal.valueOf(10));
        h.setStartDate(Long.valueOf("3131311"));
        h.setDuration(10);
        h.setMemberNum(10);
        h.setActivityStatus(TeamStatus.FINISHED);
        h.setCategoryId(Long.valueOf(1));
        h.setStartType(StartType.FULL_PEOPLE);
        h.setStartTime(1111);
        h.setEndTime(22222);
        h.setCreateTime((long) 9999);
        h.setUpdateTime((long) 999999);
        h.setCreateUserId((long) 9);
        System.out.println(teamMapper.save(h));
    }


    @Test
    public void testlistcategort(){
	    System.out.println(categoryMapper.listcategory());
    }

}
