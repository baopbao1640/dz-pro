package com.platform.core.framework.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 基础配置，负责注册平台统一的 MyBatis Plus interceptor 容器。
 *
 * <p>职责：为后续分页、数据权限等 inner interceptor 提供统一挂载点。
 *
 * <p>边界：本阶段不在这里实现业务 SQL 条件，也不修改 Mapper 查询语义。
 *
 * <p>当前阶段能力：保持 interceptor 容器可注入，具体 inner interceptor 按阶段启用。
 */
@Configuration
public class MybatisPlusConfig {

  /**
   * 构建 MyBatis Plus interceptor 容器。
   *
   * <p>Deferred: 分页 inner interceptor 当前未启用，Phase 3B 已通过显式 count 修复分页 total。
   *
   * <p>Risk: 后续启用分页或数据权限插件时必须回归现有 Mapper 分页和 count 语义。
   */
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    return interceptor;
  }
}
