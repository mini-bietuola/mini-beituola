package com.netease.mini.bietuola.web.util;

import com.netease.mini.bietuola.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

/**
 * @Description 参数检查工具类
 * @Auther ctl
 * @Date 2019/5/13
 */
public class ParamUtils {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^0?(13|14|15|17|18|19)[0-9]{9}$");
    private static final Pattern MD5_PATTERN = Pattern.compile("^([a-fA-F0-9]{32})$");

    /**
     * 校验手机号格式，国内号码
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        return null != phone && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 校验md5格式，大小写数字32位
     * @param passwordMd5
     * @return
     */
    public static boolean checkPasswordMd5(String passwordMd5) {
        return null != passwordMd5 && MD5_PATTERN.matcher(passwordMd5).matches();
    }

    public static boolean checkUserProperties(User user) {
        Assert.notNull(user, "user is null");
        int l = StringUtils.length(user.getNickname());
        if (l < 1 || l > 12) {
            return false;
        }
        if (user.getGender() == null || user.getAge() == null
                || user.getAge() < 1 || user.getAge() > 999) {
            return false;
        }
        if (!HttpUtils.checkUrl(user.getAvatarUrl())) {
            return false;
        }
        return true;
    }

}
