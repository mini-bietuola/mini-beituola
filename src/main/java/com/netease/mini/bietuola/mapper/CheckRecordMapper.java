package com.netease.mini.bietuola.mapper;

import com.netease.mini.bietuola.entity.CheckRecord;

import java.util.List;

/**
 * Created by zhang on 2019/5/1.
 */
public interface CheckRecordMapper {
    /**
     * 通过userTeamId查找对应得CheckRecord列表
     * @param userTeamId
     * @return
     */
    List<CheckRecord> findCheckRecordByUserTeamId(Long userTeamId);
}
