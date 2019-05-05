package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public interface UserMapper {

    List<User> getAllUserByTeamId(Long teamId);

}