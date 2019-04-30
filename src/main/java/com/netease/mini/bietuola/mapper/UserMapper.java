package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.entity.User;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public interface UserMapper {

    /**
     * 根据用户id查找用户
     * @param userId 用户id
     * @return
     */
    User findUserByUserId(int userId);


    /**
     * @param user 用户
     */
    void save(User user);

    /**
     * @param user 用户
     */
    void updateByUserId(User user);
}
