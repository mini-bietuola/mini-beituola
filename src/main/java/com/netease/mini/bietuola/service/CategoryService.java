package com.netease.mini.bietuola.service;

import com.netease.mini.bietuola.entity.Category;
import com.netease.mini.bietuola.entity.Hello;

import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/28
 */
public interface CategoryService {

//    List<Hello> listHello();
//
//    List<Hello> getHelloByName(String name);
//
//    boolean save(Hello hello);
//
//    boolean update(String name, long id);

    List<Category> listcategory();


    /**
     * 根据ID查询类别
     *
     * @param categoryId
     * @return
     */

    List<Category> getCategory(Long categoryId);
}
