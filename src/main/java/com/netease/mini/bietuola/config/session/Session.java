package com.netease.mini.bietuola.config.session;

import com.netease.mini.bietuola.entity.User;

import java.io.Serializable;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public class Session implements Serializable {
    private static final long serialVersionUID = 6429638447262053268L;

    private String token;
    private User user;
    private String ext;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "Session{" +
                "token='" + token + '\'' +
                ", user=" + user +
                ", ext='" + ext + '\'' +
                '}';
    }
}
