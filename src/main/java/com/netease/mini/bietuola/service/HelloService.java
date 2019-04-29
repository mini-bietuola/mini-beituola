package com.netease.mini.bietuola.service;

import com.netease.mini.bietuola.entity.Hello;

import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/28
 */
public interface HelloService {

    List<Hello> listHello();

    List<Hello> getHelloByName(String name);

    boolean save(Hello hello);

    boolean update(String name, long id);


}
