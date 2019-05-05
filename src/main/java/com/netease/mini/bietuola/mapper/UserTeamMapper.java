package com.netease.mini.bietuola.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public interface UserTeamMapper {

    BigDecimal selectAwardAmountByUserIdTeamId(@Param("userId") Long userId, @Param("teamId") Long teamId);

}
