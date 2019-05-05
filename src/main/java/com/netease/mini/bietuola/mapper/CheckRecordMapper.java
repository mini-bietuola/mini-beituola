package com.netease.mini.bietuola.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

public interface CheckRecordMapper {

    int CountCheckTimeByUserId(@Param("userId") Long userId, @Param("teamId") Long teamId);

}
