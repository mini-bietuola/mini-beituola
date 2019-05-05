package com.netease.mini.bietuola.service;

import com.netease.mini.bietuola.web.util.JsonResponse;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public interface TeamService {
    JsonResponse getBaseInfo(Long teamId, Long userId);

    //JsonResponse getCheckDetail(Integer teamId, Integer userId);
}
