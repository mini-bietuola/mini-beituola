package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.entity.User;
import org.apache.ibatis.annotations.Param;

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
    int save(User user);

    /**
     * @param user 用户
     */
    void updateByUserId(User user);

    /**
     *
     * @param phone
     * @return
     */
    User getByPhone(@Param("phone") String phone);

    int countByPhone(@Param("phone") String phone);

    int updatePassword(@Param("phone") String phone, @Param("passwordMd5") String passwordMd5);

    User getBaseInfoById(@Param("id") Long id);

    int updateBaseInfoById(User user);
}
