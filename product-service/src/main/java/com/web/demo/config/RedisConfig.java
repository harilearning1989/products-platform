package com.web.demo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableCaching
public class RedisConfig {

    /*@Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();
    }*/

   /* @Bean
    public RedisCacheManager cacheManagerConfiguration(RedisConnectionFactory factory) {
        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10));

        Map<String, RedisCacheConfiguration> configs = new HashMap<>();

        configs.put("users",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(30)));

        configs.put("products",
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(5)));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(configs)
                .build();
    }*/


}
