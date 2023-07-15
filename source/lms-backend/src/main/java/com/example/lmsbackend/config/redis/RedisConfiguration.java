package com.example.lmsbackend.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedissonClient redisClient(RedisConfig redisConfig){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec(objectMapper));
        config.useSingleServer()
                .setAddress(redisConfig.getUrl())
                .setPassword(redisConfig.getPassword());

        return Redisson.create(config);
    }
}
