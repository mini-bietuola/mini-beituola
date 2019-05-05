package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.entity.UserTeam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhang on 2019/5/1.
 */
public interface UserTeamMapper {
    /**
     * 查询用户的所有小组
     * @param userId 用户id
     * @return
     */
    List<UserTeam> findUserTeamByUserId(Long userId);

    /**
     * 查询当前小组参与人数
     * @param teamId
     * @return
     */
    long findTeamJoinNum(Long teamId);

    /**
     * 查询用户参与的小组的信息
     * @param userId
     * @param teamId
     * @return
     */
    UserTeam findUserTeamByUserIdAndTeamId(@Param("userId") Long userId, @Param("teamId") Long teamId);
}
