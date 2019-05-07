package com.netease.mini.bietuola.web.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @Description
 * @Auther ctl
 * @Date 2018/8/1
 */
public class HttpUtils {

    public static final String URL_REGEX = "https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

    public static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    /**
     * 检查 URL 格式
     * @param url
     * @return
     */
    public static boolean checkUrl(String url) {
        return null != url && URL_PATTERN.matcher(url).matches();
    }

    /**
     * HTTP请求全路径，包含所有浏览器地址栏信息，例如：http://test.com:8080/foo/bar.do?a=b&c=d
     */
    public static String getFullRequestUrl(HttpServletRequest req) {
        StringBuffer url = req.getRequestURL();
        String queryString = req.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            url.append("?").append(queryString);
        }
        return url.toString();
    }

    public static HttpMethod parseHttpMethod(String method) {
        if (StringUtils.isNotBlank(method)) {
            try {
                return HttpMethod.valueOf(method.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }
        return null;
    }

    /**
     * 不包含contextPath和queryString
     */
    public static String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();
        if (request.getPathInfo() != null) {
            url += request.getPathInfo();
        }
        return url;
    }

}
