package com.lanxinbase.config;

import com.lanxinbase.system.provider.CacheProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by alan on 2018/6/2.
 */
@Configuration
public class CacheConfig {

    @Value("${spring.redis.id}")
    private String id;

    public CacheConfig() {
    }

    @Bean
    public CacheProvider cacheProvider(){
        CacheProvider cacheProvider = new CacheProvider(id);
        return cacheProvider;
    }
}
