package com.codeuai.smarttaskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();

    template.setConnectionFactory(connectionFactory);
    template.setDefaultSerializer(StringRedisSerializer.UTF_8);
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());

    ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
    var serializer = new JacksonJsonRedisSerializer<Object>(mapper, Object.class);

    template.setValueSerializer(serializer);
    template.setHashValueSerializer(serializer);
    template.afterPropertiesSet();

    return template;

  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .computePrefixWith(cacheName -> "stm:" + cacheName + ":");
  }

}
