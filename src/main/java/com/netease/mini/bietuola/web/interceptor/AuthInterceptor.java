package com.netease.mini.bietuola.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.mini.bietuola.config.session.Session;
import com.netease.mini.bietuola.config.session.SessionService;
import com.netease.mini.bietuola.web.util.HttpUtils;
import com.netease.mini.bietuola.web.util.JsonResponse;
import com.netease.mini.bietuola.web.util.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(AuthInterceptor.class);

    private final SessionService sessionService;
    private final ObjectMapper mapper = new ObjectMapper();

    public AuthInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        // 请求不含token，拒绝访问，可以返回http状态码401或者仍然返回200ok，但在响应体里返回未登录的业务状态码
        if (StringUtils.isBlank(token)) {
            LOG.warn("request without token: {}", HttpUtils.getRequestPath(request));
            createNotLoginResp(response);
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        Session session = sessionService.getSession(token);
        // 请求token已过期
        if (session == null) {
            createNotLoginResp(response);
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        sessionService.refreshToken(token);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        sessionService.destoryThreadLocalSession();
    }

    private void createNotLoginResp(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(mapper.writeValueAsString(
                JsonResponse.codeOf(ResultCode.ERROR_NOT_LOGGED_IN).setMsg("未登录")));
    }

}
