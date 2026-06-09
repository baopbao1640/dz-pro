package com.platform.core.framework.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.core.common.api.ApiResult;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * 安全链响应统一从这里写出 JSON，是为了避免 entrypoint、access denied 和异常处理各自拼接响应结构，导致前端在 401 / 403 / 400 / 500
 * 下拿到不同契约。
 */
@Component
public class SecurityJsonResponseWriter {

  private final ObjectMapper objectMapper;

  public SecurityJsonResponseWriter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

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
