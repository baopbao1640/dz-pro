package com.platform.core.framework.security.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * REST 授权失败处理器，负责把 Spring Security 的访问拒绝状态转换为统一 403 JSON。
 *
 * <p>职责：处理已认证但缺少访问权限的请求。
 *
 * <p>边界：不处理未认证状态，不暴露具体缺失权限码。
 *
 * <p>当前阶段能力：输出 `{code,message,data}` 结构的 forbidden 响应。
 */
/*
 * Boundary:
 * 只处理过滤链阶段的访问拒绝，MVC 内部抛出的 AccessDeniedException 可由全局异常处理器兜底。
 */
/*
 * Deferred:
 * 暂未接入权限拒绝审计；后续可以与 audit 管道联动记录高风险访问。
 */
/*
 * Risk:
 * 如果业务方法未标注权限，本处理器不会被触发；仍需 `@RequiresPermission` 覆盖核心入口。
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  private final SecurityJsonResponseWriter responseWriter;

  public RestAccessDeniedHandler(SecurityJsonResponseWriter responseWriter) {
    this.responseWriter = responseWriter;
  }

  /**
   * 写出授权失败响应。
   *
   * <p>安全影响：隐藏缺失权限细节，避免暴露 permission code 枚举空间。
   *
   * <p>返回含义：直接写入 403 JSON，不进入业务 Controller。
   */
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    responseWriter.write(response, HttpStatus.FORBIDDEN.value(), "forbidden");
  }
}
