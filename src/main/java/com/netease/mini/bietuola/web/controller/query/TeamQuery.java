package com.netease.mini.bietuola.web.controller.query;

import com.netease.mini.bietuola.web.controller.query.common.PageQuery;

import java.io.Serializable;

/**
 * Created by zhang on 2019/5/3.
 */
public class TeamQuery extends PageQuery implements Serializable {

    private static final long serialVersionUID = -3437880120499478967L;

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
