package com.netease.mini.bietuola.service.impl;

import com.netease.mini.bietuola.entity.User;
import com.netease.mini.bietuola.mapper.UserMapper;
import com.netease.mini.bietuola.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

//    private static final String DEFAULT_USER_AVATAR_URL = "http://static.tongjilab.cn/image/20170127/172713501.png";
    private static final String DEFAULT_USER_AVATAR_URL = "https://bietuola.nos-eastchina1.126.net/38dd59ab38454531a65aa4fb686d1182.jpg";

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getByPhone(String phone) {
        return userMapper.getByPhone(phone);
    }

    @Override
    public boolean checkPhoneRegistered(String phone) {
        return userMapper.countByPhone(phone) > 0;
    }

    @Override
    public User registerUser(String phone, String passwordMd5) {
        String defaultNickName = "挑战者_" + phone.substring(7);
        long time = System.currentTimeMillis();
        User user = new User();
        user.setNickname(defaultNickName);
        user.setPhone(phone);
        user.setPasswordMd5(passwordMd5);
        user.setStatus(0);
        user.setCreateTime(time);
        user.setUpdateTime(time);
        user.setScore(0L);
        user.setAvatarUrl(DEFAULT_USER_AVATAR_URL);
        user.setAmount(new BigDecimal("0.00"));
        int i = userMapper.save(user);
        return i == 1 ? user : null;
    }

    @Override
    public boolean resetPassword(String phone, String passwordMd5) {
        int i = userMapper.updatePassword(phone, passwordMd5);
        return i > 0;
    }

    @Override
    public User getBaseInfoById(Long id) {
        return userMapper.getBaseInfoById(id);
    }

    @Override
    public boolean updateBaseInfo(User user) {
        user.setUpdateTime(System.currentTimeMillis());
        int i = userMapper.updateBaseInfoById(user);
        return i > 0;
    }
}
