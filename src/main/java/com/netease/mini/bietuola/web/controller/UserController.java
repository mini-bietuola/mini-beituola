package com.netease.mini.bietuola.web.controller;

import com.netease.mini.bietuola.config.im.CaptchaService;
import com.netease.mini.bietuola.config.session.Session;
import com.netease.mini.bietuola.config.session.SessionService;
import com.netease.mini.bietuola.entity.User;
import com.netease.mini.bietuola.service.UserService;
import com.netease.mini.bietuola.web.util.JsonResponse;
import com.netease.mini.bietuola.web.util.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/30
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final SessionService sessionService;
    private final CaptchaService captchaService;

    public UserController(UserService userService, SessionService sessionService, CaptchaService captchaService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.captchaService = captchaService;
    }

    @PostMapping("/login")
    public JsonResponse login(String phone, String passwordMd5) {
        if (StringUtils.isAnyBlank(phone, passwordMd5)) {
            return JsonResponse.codeOf(ResultCode.ERROR_BAD_PARAMETER).setMsg("参数格式错误");
        }
        User user = userService.getByPhone(phone.trim());
        if (user == null || !passwordMd5.trim().equalsIgnoreCase(user.getPasswordMd5())) {
            return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("用户名或密码错误");
        }
        Session session = sessionService.login(user);
        LOG.info("user login, id: {}", user.getId());
        return JsonResponse.success(session);
    }

    @GetMapping("/check-phone-registered")
    public JsonResponse checkPhoneRegistered(String phone) {
        if (StringUtils.isBlank(phone)) {
            return JsonResponse.codeOf(ResultCode.ERROR_BAD_PARAMETER).setMsg("参数格式错误");
        }
        return userService.checkPhoneRegistered(phone) ? JsonResponse.success()
                : JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("账号未注册");
    }

    @PostMapping("/authcode")
    public JsonResponse sendPhoneAuthVerifyCode(String phone) {
        if (StringUtils.isBlank(phone)) {
            return JsonResponse.codeOf(ResultCode.ERROR_BAD_PARAMETER).setMsg("参数格式错误");
        }
        return captchaService.sendAuthVerifyCode(phone.trim()) ? JsonResponse.success()
                : JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("验证码发送失败");
    }

    @PostMapping("/register")
    public JsonResponse userRegister(String phone, String passwordMd5, String verifyCode) {
        if(StringUtils.isAnyBlank(phone, passwordMd5, verifyCode) ||
                (passwordMd5 = passwordMd5.trim()).length() != 32){
            return JsonResponse.codeOf(ResultCode.ERROR_BAD_PARAMETER).setMsg("参数格式错误");
        }
        phone = phone.trim();
        verifyCode = verifyCode.trim();
        passwordMd5 = passwordMd5.toLowerCase();
        if (!captchaService.verifyAuthVerifyCode(phone, verifyCode)) {
            return JsonResponse.codeOf(ResultCode.VERIFY_CODE_FAIL).setMsg("验证码校验失败");
        }
        // 注册新用户
        User user = userService.registerUser(phone, passwordMd5);
        if (user == null) {
            return JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("注册失败");
        }
        return JsonResponse.success();
    }

    @PostMapping("/reset-password")
    public JsonResponse resetPassword(String phone, String passwordMd5, String verifyCode) {
        if(StringUtils.isAnyBlank(phone, passwordMd5, verifyCode) ||
                (passwordMd5 = passwordMd5.trim()).length() != 32){
            return JsonResponse.codeOf(ResultCode.ERROR_BAD_PARAMETER).setMsg("参数格式错误");
        }
        phone = phone.trim();
        verifyCode = verifyCode.trim();
        passwordMd5 = passwordMd5.toLowerCase();
        if (!captchaService.verifyAuthVerifyCode(phone, verifyCode)) {
            return JsonResponse.codeOf(ResultCode.VERIFY_CODE_FAIL).setMsg("验证码校验失败");
        }
        // 修改密码
        return userService.resetPassword(phone, passwordMd5) ? JsonResponse.success()
                : JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("重置密码失败");
    }

    @PostMapping("/logout")
    public JsonResponse logout() {
        sessionService.logout();
        return JsonResponse.success();
    }

    @GetMapping("/info")
    public JsonResponse getUserInfo() {
        Long uid = sessionService.getCurrentUserId();
        User user = userService.getBaseInfoById(uid);
        return user != null ? JsonResponse.success(user)
                : JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("用户不存在");
    }

    @PostMapping("/update")
    public JsonResponse updateUserInfo(User user) {
        if (StringUtils.isAnyBlank(user.getNickname(), user.getAvatarUrl())) {
            return JsonResponse.codeOf(ResultCode.ERROR_BAD_PARAMETER).setMsg("参数格式错误");
        }
        return userService.updateBaseInfo(user) ? JsonResponse.success()
                : JsonResponse.codeOf(ResultCode.ERROR_UNKNOWN).setMsg("用户信息更新失败");
    }

}
