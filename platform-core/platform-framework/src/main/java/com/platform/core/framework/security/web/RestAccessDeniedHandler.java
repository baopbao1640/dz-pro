package com.platform.core.framework.security.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/** 授权失败统一返回 403 JSON。这里不暴露具体权限缺失细节，是为了避免把系统权限码清单直接泄露给调用方。 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  private final SecurityJsonResponseWriter responseWriter;

  public RestAccessDeniedHandler(SecurityJsonResponseWriter responseWriter) {
    this.responseWriter = responseWriter;
  }

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    responseWriter.write(response, HttpStatus.FORBIDDEN.value(), "forbidden");
  }
}
