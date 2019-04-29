package com.netease.mini.bietuola.web.controller.query;

import com.netease.mini.bietuola.constant.Role;
import com.netease.mini.bietuola.web.controller.query.common.PageQuery;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/28
 */
public class HelloQuery extends PageQuery {
    private static final long serialVersionUID = -2811679923963385766L;

    private String name;
    private int age;
    private Role role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "HelloQuery{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", role=" + role +
                "} " + super.toString();
    }
}
