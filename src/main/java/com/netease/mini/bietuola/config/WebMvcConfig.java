package com.netease.mini.bietuola.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.netease.mini.bietuola.config.convert.CustomStringToEnumConverterFactory;
import com.netease.mini.bietuola.config.convert.IntEnumSerializer;
import com.netease.mini.bietuola.config.mybatis.IntEnum;
import com.netease.mini.bietuola.config.session.SessionService;
import com.netease.mini.bietuola.web.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/28
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private final SessionService sessionService;

    public WebMvcConfig(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * 处理请求传参枚举类反序列化
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CustomStringToEnumConverterFactory());
    }

    /**
     * 自定义 jsckson2 json序列化，用于处理 IntEnum
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() throws Exception {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule sm = new SimpleModule();
        sm.addSerializer(IntEnum.class, IntEnumSerializer.INSTANCE);
        mapper.registerModule(sm);
        converter.setObjectMapper(mapper);
        return converter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(sessionService)).excludePathPatterns(
                "/api/user/login", "/api/user/logout", "/api/user/register",
                "/api/user/authcode", "/api/user/reset-password", "/api/user/check-phone-registered",
                "/error");
    }

}
