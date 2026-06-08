package com.platform.core.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * framework 层只定义平台通用安全边界：认证由 Keycloak 提供，业务用户、角色和权限仍由 system 模块解释。这里不直接读取 `sys_user` 或菜单表，避免
 * framework 反向依赖业务模块。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * 当前安全链承担 Resource Server 基础接入。Deferred: `/api/system/**` 暂时放行是为了 Phase 3A smoke
   * 和页面联调，后续必须在接入当前用户权限后收紧到权限码校验。
   */
  @Bean
  @Order(0)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/actuator/health")
                    .permitAll()
                    .requestMatchers("/api/system/**")
                    .permitAll()
                    .requestMatchers("/api/user/info")
                    .authenticated()
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .oauth2Client(Customizer.withDefaults());

    return http.build();
  }
}
