package com.xiang.cloud.cache.redis.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * FastJson方式序列化
 * @author 李鹏翔(lipengxiang1)
 * @date 2019-07-24
 * @deprecated FastJson的autoType老是报漏洞，慎用
 **/
public class SimpleFastJsonRedisSerializer implements RedisSerializer<Object> {

    private static ParserConfig parseConfig;

    static {
        parseConfig = new ParserConfig();
        parseConfig.setAutoTypeSupport(true);
    }

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return JSON.toJSONBytes(o, SerializerFeature.NotWriteDefaultValue, SerializerFeature.WriteEnumUsingName, SerializerFeature.WriteClassName);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return JSON.parse(new String(bytes, IOUtils.UTF8), parseConfig);
    }
}
