package com.netease.mini.bietuola.config.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/28
 */
public class CustomStringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToIntEnum(targetType);
    }
}
