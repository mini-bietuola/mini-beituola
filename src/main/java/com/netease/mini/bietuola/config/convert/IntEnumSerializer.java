package com.netease.mini.bietuola.config.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.netease.mini.bietuola.config.mybatis.IntEnum;

import java.io.IOException;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/4/28
 */
public class IntEnumSerializer extends StdScalarSerializer<IntEnum> {
    private static final long serialVersionUID = -8060014593493248173L;

    public static final IntEnumSerializer INSTANCE = new IntEnumSerializer();

    protected IntEnumSerializer() {
        super(IntEnum.class);
    }

    @Override
    public void serialize(IntEnum intEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(intEnum.getIntValue());
    }
}
