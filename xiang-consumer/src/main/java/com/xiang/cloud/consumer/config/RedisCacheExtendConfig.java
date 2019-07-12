package com.xiang.cloud.consumer.config;

import com.xiang.cloud.consumer.cache.CacheExpire;
import com.xiang.cloud.consumer.cache.KryoRedisSerializer;
import com.xiang.cloud.consumer.cache.RedisCacheProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Cache的redis扩展配置
 *
 * 支持超时@CacheExpire注解，生效前提是class上面需要有@CacheConfig注解，支持和CachePut,Cachable,Caching,
 * CacheConfig混用，优先级：CachePut > Cacheable > Caching (Caching里优先级CachePut > Cacheable)
 * @author 李鹏翔(lipengxiang1)
 * @date 2019/6/26
 **/
@Slf4j
@Configuration
@ConditionalOnBean(RedisCacheProperty.class)
public class RedisCacheExtendConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private RedisCacheProperty redisCacheProperty;

    /**
     * 让这个bean最后初始化
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    @Order(value = Ordered.LOWEST_PRECEDENCE)
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        if (StringUtils.isBlank(redisCacheProperty.keyPrefix()) || redisCacheProperty.defaultExpireTime() == null) {
            throw new IllegalArgumentException("keyPrefix and defaultExpireTime can't be null");
        }

        // 默认配置
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(redisCacheProperty.defaultExpireTime())
                .computePrefixWith(cacheName -> String.format("%s:%s:", redisCacheProperty.keyPrefix(), cacheName));

        if (redisCacheProperty.cacheNullValues() != null && !redisCacheProperty.cacheNullValues()) {
            defaultConfig = defaultConfig.disableCachingNullValues();
        }

        if (redisCacheProperty.conversionService() != null) {
            defaultConfig = defaultConfig.withConversionService(redisCacheProperty.conversionService());
        }

        RedisSerializer<String> keySerializer = redisCacheProperty.keySerializer() != null ? redisCacheProperty.keySerializer() : new StringRedisSerializer();
        RedisSerializer<?> valueSerializer = redisCacheProperty.valueSerializer() != null ? redisCacheProperty.valueSerializer() : new KryoRedisSerializer(Object.class);

        defaultConfig = defaultConfig
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer));

        // 通过扫描@CacheConfig注解的类加载特殊配置
        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();

        String[] names = applicationContext.getBeanNamesForAnnotation(CacheConfig.class);

        log.info("Load redis cache extension config of beans: {}", String.join(",", names));

        for (String beanName : names) {

            Class<?> beanClass = applicationContext.getBean(beanName).getClass();

            // 加载class上面的配置
            CacheExpire classCacheExpire = AnnotationUtils.findAnnotation(beanClass, CacheExpire.class);
            CacheConfig cacheConfig = AnnotationUtils.findAnnotation(beanClass, CacheConfig.class);
            String[] rootCacheNames = cacheConfig != null ? cacheConfig.cacheNames() : new String[]{};
            if (classCacheExpire != null && rootCacheNames.length != 0) {
                fillRedisCacheConfigMap(rootCacheNames, classCacheExpire, defaultConfig, cacheConfigurationMap);
            }


            // 加载方法的配置
            for (Method method : beanClass.getMethods()) {

                CacheExpire cacheExpire = AnnotationUtils.findAnnotation(method, CacheExpire.class);
                if (cacheExpire == null) {
                    cacheExpire = classCacheExpire;
                }

                // 读取Caching配置
                Caching caching = AnnotationUtils.findAnnotation(method, Caching.class);
                if (caching != null) {
                    for (Cacheable cacheable : caching.cacheable()) {
                        String[] cacheNames = cacheable.cacheNames().length > 0 ? cacheable.cacheNames() : rootCacheNames;
                        fillRedisCacheConfigMap(cacheNames, cacheExpire, defaultConfig, cacheConfigurationMap);
                    }
                    for (CachePut cachePut : caching.put()) {
                        String[] cacheNames = cachePut.cacheNames().length > 0 ? cachePut.cacheNames() : rootCacheNames;
                        fillRedisCacheConfigMap(cacheNames, cacheExpire, defaultConfig, cacheConfigurationMap);
                    }
                }

                // 读取Cacheable配置
                Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
                if (cacheable != null) {
                    String[] cacheNames = cacheable.cacheNames().length > 0 ? cacheable.cacheNames() : rootCacheNames;
                    fillRedisCacheConfigMap(cacheNames, cacheExpire, defaultConfig, cacheConfigurationMap);
                }

                // 读取CachePut配置
                CachePut cachePut = AnnotationUtils.findAnnotation(method, CachePut.class);
                if (cachePut != null) {
                    String[] cacheNames = cachePut.cacheNames().length > 0 ? cachePut.cacheNames() : rootCacheNames;
                    fillRedisCacheConfigMap(cacheNames, cacheExpire, defaultConfig, cacheConfigurationMap);
                }
            }
        }

        log.info("Load redis cache extension config success, cache keys: {}", String.join(",", cacheConfigurationMap.keySet()));

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
                .withInitialCacheConfigurations(cacheConfigurationMap)
                .cacheDefaults(defaultConfig)
                .build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    private void fillRedisCacheConfigMap(String[] cacheNames, CacheExpire cacheExpire, RedisCacheConfiguration defaultConfig,
                           Map<String, RedisCacheConfiguration> cacheConfigurationMap) {
        if (cacheNames == null || cacheExpire == null) {
            return;
        }
        for (String cacheName : cacheNames) {
            Duration exp = Duration.ofSeconds(cacheExpire.timeUnit().toSeconds(cacheExpire.value()));
            cacheConfigurationMap.put(cacheName, defaultConfig.entryTtl(exp));
        }
    }
}
