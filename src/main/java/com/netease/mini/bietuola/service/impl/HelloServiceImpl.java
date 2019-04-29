package com.netease.mini.bietuola.service.impl;

import com.netease.mini.bietuola.entity.Hello;
import com.netease.mini.bietuola.mapper.HelloMapper;
import com.netease.mini.bietuola.service.HelloService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/28
 */
@Service("helloService")
public class HelloServiceImpl implements HelloService {

    private final HelloMapper helloMapper;

    public HelloServiceImpl(HelloMapper helloMapper) {
        this.helloMapper = helloMapper;
    }

    @Override
    public List<Hello> listHello() {
        return helloMapper.listHello();
    }

    @Override
    public List<Hello> getHelloByName(String name) {
        return helloMapper.getHelloByName(name);
    }

    @Override
    public boolean save(Hello hello) {
        return helloMapper.save(hello) == 1;
    }

    @Override
    public boolean update(String name, long id) {
        return helloMapper.update(name, id) == 1;
    }
}
