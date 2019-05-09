package com.netease.mini.bietuola.config.session;

import com.netease.mini.bietuola.config.redis.RedisService;
import com.netease.mini.bietuola.entity.User;
import com.sun.javafx.tk.TKClipboard;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
@Service
public class SessionService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

    private static final String SESSION_KEY_PREFIX = "token:";
    private static final long SESSION_TIMEOUT_SECOND = 60 * 60 * 24 * 30; // 30天

    private final RedisService redisService;

    /**
     * 用threadlocal来存储session，用于当前请求线程中的后续访问，避免每次都从redis取
     */
    private ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();

    public SessionService(RedisService redisService) {
        this.redisService = redisService;
    }

    public void destoryThreadLocalSession() {
        sessionThreadLocal.remove();
    }

    /**
     * 创建用户会话，返回Session，其中包含token
     * @param user
     * @return
     */
    public Session login(User user) {
        Session session = new Session();
        String token = UUID.randomUUID().toString().replace("-", "");
        session.setToken(token);
        user.setPasswordMd5(null);
        session.setUser(user);
        String key = SESSION_KEY_PREFIX + token;
        redisService.set(key, session, SESSION_TIMEOUT_SECOND);
        return session;
    }

    /**
     * 登出，删除token
     * @param token
     */
    public void logout(String token) {
        String key = SESSION_KEY_PREFIX + token;
        redisService.delete(key);
    }

    public void logout() {
        String token = getRequest().getHeader("token");
        if (StringUtils.isNotBlank(token)) {
            logout(token);
        }
    }

    /**
     * 刷新token
     * @param token
     */
    public void refreshToken(String token) {
        String key = SESSION_KEY_PREFIX + token;
        redisService.setExpire(key, SESSION_TIMEOUT_SECOND);
    }

    /**
     * 根据token获取session信息
     * @param token
     * @return
     */
    public Session getSession(String token) {
        Session session = sessionThreadLocal.get();
        if (session == null) {
            String key = SESSION_KEY_PREFIX + token;
            Session freshSession = (Session) redisService.get(key);
            if (freshSession != null) {
                sessionThreadLocal.set(freshSession);
                session = freshSession;
            }
        }
        return session;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public User getCurrentUser() {
        String token = getRequest().getHeader("token");
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Session session = getSession(token);
        return session == null ? null : session.getUser();
    }

    /**
     * 得到当前登录用户id
     * @return
     */
    public Long getCurrentUserId() {
        User user = getCurrentUser();
        return user == null ? null : user.getId();
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

}
