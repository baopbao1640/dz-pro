package com.platform.core.framework.config;

import com.platform.core.framework.security.web.RestAccessDeniedHandler;
import com.platform.core.framework.security.web.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 平台安全链配置，负责把 Spring Security resource server、统一 401/403 JSON 响应和最小公开路径收敛在 framework 层。
 *
 * <p>职责：定义认证入口、鉴权失败处理、无状态会话和 actuator 暴露边界。
 *
 * <p>边界：Keycloak 仍负责认证和 token 生命周期；本类不读取 `sys_user`、角色或菜单表，业务用户映射与权限解释由 system 模块实现。
 *
 * <p>当前阶段能力：`/api/system/**` 默认需要认证，`/actuator/health` 匿名访问，其他 actuator endpoint 受保护。
 */
/*
 * Boundary:
 * framework 只配置通用安全链和错误响应，不承载业务权限数据来源。
 */
/*
 * Deferred:
 * 后续如需按业务域细化路径权限，应通过 `@RequiresPermission` 或业务授权服务扩展，不在这里硬编码菜单权限。
 */
/*
 * Risk:
 * 路径匹配规则一旦放宽会影响全部系统管理接口；后续调整必须配合安全回归和 401/403 smoke。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * 构建平台主安全链。
   *
   * <p>方法目的：把认证、授权失败响应和受保护路径统一注册到 Spring Security。
   *
   * <p>关键规则：健康探活是唯一匿名 actuator 入口，系统管理接口必须先通过 Keycloak JWT 认证。
   *
   * <p>返回含义：返回可被 Spring Boot 注册的 `SecurityFilterChain`。
   *
   * <p>异常行为：配置异常由 Spring Security 构建阶段抛出，运行时 401/403 由注入的 handler 输出 JSON。
   */
  @Bean
  @Order(0)
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      RestAuthenticationEntryPoint authenticationEntryPoint,
      RestAccessDeniedHandler accessDeniedHandler)
      throws Exception {
    http.csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            exception ->
                exception
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/actuator/health")
                    .permitAll()
                    .requestMatchers("/actuator/**")
                    .authenticated()
                    .requestMatchers("/api/system/**", "/api/user/info")
                    .authenticated()
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .oauth2Client(Customizer.withDefaults());

    return http.build();
  }
}
