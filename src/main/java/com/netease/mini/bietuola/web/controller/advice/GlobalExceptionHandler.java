package com.netease.mini.bietuola.web.controller.advice;

import com.netease.mini.bietuola.web.util.JsonResponse;
import com.netease.mini.bietuola.web.util.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 处理全局异常
 * @Auther ctl
 * @Date 2018/7/12
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 全局异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResponse handleError(HttpServletRequest req, Exception e) {
        LOG.error("服务全局异常", e);
        return JsonResponse.codeOf(ResultCode.ERROR_EXCEPTION_GLOBAL).setMsg("服务异常，请稍后再试")
                .setData(e.getMessage());
    }
}
