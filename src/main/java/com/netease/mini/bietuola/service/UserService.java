package com.netease.mini.bietuola.service;

import com.netease.mini.bietuola.entity.User;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public interface UserService {
    User getByPhone(String phone);

    boolean checkPhoneRegistered(String phone);

    User registerUser(String phone, String passwordMd5);

    boolean resetPassword(String phone, String passwordMd5);

    User getBaseInfoById(Long uid);

    boolean updateBaseInfo(User user);

    User getById(Long currentUserId);
}
