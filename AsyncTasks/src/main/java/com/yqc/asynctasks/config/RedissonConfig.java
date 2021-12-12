package com.yqc.asynctasks.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value(value = "third-part.redis.end-point")
    private String redisEndPoint;

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() {
        //1、创建配置
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisEndPoint);
        return Redisson.create(config);
    }
}
