package com.netease.mini.bietuola.config.convert;

import com.netease.mini.bietuola.config.mybatis.IntEnum;
import com.netease.mini.bietuola.config.mybatis.IntEnumCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/28
 */
public class StringToIntEnum<T extends Enum<T> & IntEnum> implements Converter<String, IntEnum> {

    private Class<T> enumType;

    public StringToIntEnum(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public IntEnum convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        return IntEnumCache.getEnumValue(enumType, Integer.parseInt(s.trim()));
    }
}
