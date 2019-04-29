package com.netease.mini.bietuola;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netease.mini.bietuola.entity.Hello;
import com.netease.mini.bietuola.mapper.HelloMapper;
import com.netease.mini.bietuola.web.controller.query.HelloQuery;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BietuolaApplicationTests {

    @Autowired
    private HelloMapper helloMapper;

	@Test
	public void contextLoads() {
	}

	@Test
    public void testHello() {
//        System.out.println(helloMapper.getHelloByName("tom"));
        System.out.println(helloMapper.listHello());
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

}
