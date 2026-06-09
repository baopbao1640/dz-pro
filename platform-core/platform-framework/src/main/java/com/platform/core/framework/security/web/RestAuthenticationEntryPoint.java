package com.platform.core.framework.security.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/** 认证失败必须返回 JSON，而不是 Spring Security 默认空响应或 HTML error page。这样前端可以稳定区分未登录、登录过期和普通业务错误。 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final SecurityJsonResponseWriter responseWriter;

  public RestAuthenticationEntryPoint(SecurityJsonResponseWriter responseWriter) {
    this.responseWriter = responseWriter;
  }

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    responseWriter.write(response, HttpStatus.UNAUTHORIZED.value(), "unauthorized");
  }
}
