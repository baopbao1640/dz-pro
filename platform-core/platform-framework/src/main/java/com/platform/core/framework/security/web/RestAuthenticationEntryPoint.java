package com.platform.core.framework.security.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * REST 认证失败入口，负责把 Spring Security 的未认证状态转换为统一 401 JSON。
 *
 * <p>职责：处理缺少 token、token 失效或认证失败的请求。
 *
 * <p>边界：不处理已认证但无权限的 403，也不解析业务用户。
 *
 * <p>当前阶段能力：输出 `{code,message,data}` 结构的 unauthorized 响应。
 */
/*
 * Boundary:
 * 只处理认证失败，业务参数错误和 MVC 异常由 `GlobalExceptionHandler` 处理。
 */
/*
 * Deferred:
 * 暂未区分 token 过期、签名失败和无 token 的用户提示，前端先统一跳转登录。
 */
/*
 * Risk:
 * 统一 message 有利于安全，但排查需要依赖安全日志和 Keycloak 侧信息。
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final SecurityJsonResponseWriter responseWriter;

  public RestAuthenticationEntryPoint(SecurityJsonResponseWriter responseWriter) {
    this.responseWriter = responseWriter;
  }

  /**
   * 写出认证失败响应。
   *
   * <p>安全影响：不暴露 token 失败细节，避免帮助调用方枚举认证状态。
   *
   * <p>异常行为：响应写出失败时交由 servlet 容器处理 IOException。
   */
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    responseWriter.write(response, HttpStatus.UNAUTHORIZED.value(), "unauthorized");
  }
}
