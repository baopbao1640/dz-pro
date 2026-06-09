package com.platform.core.framework.web;

import com.platform.core.common.api.ApiResult;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 全局 API 异常处理器，负责把业务 Controller 抛出的参数、业务和未知异常收敛为统一 `ApiResult`。
 *
 * <p>职责：保证系统模块返回稳定 JSON 错误契约。
 *
 * <p>边界：认证失败由 Spring Security entrypoint 处理，本类不解析 token、不执行权限判断。
 *
 * <p>当前阶段能力：覆盖 400、403、500 三类业务侧异常响应。
 */
/*
 * Boundary:
 * 只处理进入 MVC 后的异常；安全过滤链阶段的 401/403 不从这里兜底。
 */
/*
 * Deferred:
 * 后续可以引入业务异常类型和错误码字典；当前先保持 Phase 3B 的统一响应结构。
 */
/*
 * Risk:
 * 未知异常统一返回 internal server error，避免泄露堆栈；排查依赖服务端日志。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * 处理请求参数、JSON body 和校验失败。
   *
   * <p>关键规则：客户端输入问题统一返回 400，不应被安全链误判为 401。
   *
   * <p>返回含义：返回统一 `bad request` 响应，不暴露内部校验实现细节。
   */
  @ExceptionHandler({
    MethodArgumentNotValidException.class,
    ConstraintViolationException.class,
    MethodArgumentTypeMismatchException.class,
    MissingServletRequestParameterException.class,
    HttpMessageNotReadableException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResult<Void> handleBadRequest(Exception ex) {
    return ApiResult.of(HttpStatus.BAD_REQUEST.value(), "bad request", null);
  }

  /**
   * 处理当前阶段的业务校验异常。
   *
   * <p>边界条件：仅承接服务层主动抛出的非法参数或非法状态，不替代领域错误码体系。
   *
   * <p>异常行为：保留业务异常 message，便于前端提示具体业务原因。
   */
  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResult<Void> handleBusinessException(RuntimeException ex) {
    return ApiResult.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
  }

  /**
   * 处理 MVC 阶段抛出的授权失败。
   *
   * <p>安全影响：返回 403 且不暴露缺失权限码，避免调用方枚举系统权限点。
   *
   * <p>边界：过滤链阶段的授权失败仍由 `RestAccessDeniedHandler` 输出。
   */
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiResult<Void> handleAccessDenied(AccessDeniedException ex) {
    return ApiResult.of(HttpStatus.FORBIDDEN.value(), "forbidden", null);
  }

  /**
   * 处理未被更具体规则覆盖的异常。
   *
   * <p>Risk: 对外隐藏异常细节，只记录服务端日志；后续如引入 traceId，应在响应和日志中统一关联。
   *
   * <p>返回含义：返回统一 500 响应，保持前端错误处理契约稳定。
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResult<Void> handleUnknown(Exception ex) {
    LOGGER.error("Unhandled API exception", ex);
    return ApiResult.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal server error", null);
  }
}
