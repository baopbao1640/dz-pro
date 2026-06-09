package com.platform.core.framework.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.core.common.api.ApiResult;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * 安全链 JSON 响应写入器，统一 401 和 403 的响应结构。
 *
 * <p>职责：把安全链状态码和 message 写成平台统一 `ApiResult`。
 *
 * <p>边界：不决定状态码语义，不处理业务异常。
 *
 * <p>当前阶段能力：为认证失败和授权失败提供稳定 JSON。
 */
/*
 * Boundary:
 * 只服务 Spring Security handler；普通 Controller 错误由 MVC 异常处理器负责。
 */
/*
 * Deferred:
 * 暂未写入 traceId、错误码字典和国际化 message，后续可在 API 契约治理中统一扩展。
 */
/*
 * Risk:
 * 如果 response 已提交则不会再写入 JSON，调用方需要避免在过滤链前提前提交响应。
 */
@Component
public class SecurityJsonResponseWriter {

  private final ObjectMapper objectMapper;

  public SecurityJsonResponseWriter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * 写出统一安全错误响应。
   *
   * <p>边界条件：response 已提交时直接返回，避免二次写入导致容器异常。
   *
   * <p>返回含义：方法无返回值，成功时响应体为统一 `ApiResult`。
   */
  public void write(HttpServletResponse response, int status, String message) throws IOException {
    if (response.isCommitted()) {
      return;
    }
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getWriter(), ApiResult.of(status, message, null));
  }
}
