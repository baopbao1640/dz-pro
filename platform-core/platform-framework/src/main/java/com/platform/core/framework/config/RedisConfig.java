package com.platform.core.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 基础序列化配置，负责为平台缓存能力提供统一 `RedisTemplate`。
 *
 * <p>职责：固定 key 使用字符串序列化，value 使用 JSON 序列化，避免各模块重复定义模板。
 *
 * <p>边界：本类不定义具体缓存 key、过期策略或权限缓存失效规则。
 *
 * <p>当前阶段能力：提供通用对象缓存模板。
 */
@Configuration
public class RedisConfig {

  /**
   * 构建平台通用 RedisTemplate。
   *
   * <p>Deferred: 权限缓存、菜单缓存和用户上下文缓存尚未接入，后续必须先定义失效策略。
   *
   * <p>Risk: JSON 序列化适合通用对象缓存，但跨版本字段变更需要兼容评估。
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);

    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

    template.afterPropertiesSet();
    return template;
  }
}
