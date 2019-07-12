package com.xiang.cloud.consumer.cache;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @author 李鹏翔(lipengxiang1)
 * @date 2019/7/11
 **/
public interface RedisCacheProperty {

    /**
     * 默认redis过期时间（必须）
     * @return
     */
    Duration defaultExpireTime();

    /**
     * redisKey的前缀（必须）
     * @return
     */
    String keyPrefix();

    /**
     * 是否缓存NULL值，默认为true
     * @return
     */
    Boolean cacheNullValues();

    /**
     * key序列化（可选）
     * @return
     */
    RedisSerializer<String> keySerializer();

    /**
     * value序列化（可选）
     * @return
     */
    RedisSerializer<?> valueSerializer();

    /**
     * 类型转换服务接口（可选）
     * @return
     */
    ConversionService conversionService();
}
