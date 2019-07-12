package com.xiang.cloud.consumer.config;

import com.xiang.cloud.consumer.cache.RedisCacheProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @author 李鹏翔(lipengxiang1)
 * @date 2019/7/11
 **/
@Configuration
public class XiangRedisCacheProperty implements RedisCacheProperty {
    @Override
    public Duration defaultExpireTime() {
        return Duration.ofMinutes(30);
    }

    @Override
    public String keyPrefix() {
        return "xiang-cloud";
    }

    @Override
    public Boolean cacheNullValues() {
        return false;
    }

    @Override
    public RedisSerializer<String> keySerializer() {
        return null;
    }

    @Override
    public RedisSerializer<?> valueSerializer() {
        return null;
    }

    @Override
    public ConversionService conversionService() {
        return null;
    }
}
