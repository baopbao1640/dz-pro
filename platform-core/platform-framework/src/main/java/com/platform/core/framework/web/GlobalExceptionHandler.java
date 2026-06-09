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
 * 业务 Controller 异常统一在 framework 收敛，是为了保证所有模块遵守同一个 API 错误契约。安全认证失败仍由 Security entrypoint
 * 处理，避免异常处理器绕过认证边界。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResult<Void> handleBusinessException(RuntimeException ex) {
    return ApiResult.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiResult<Void> handleAccessDenied(AccessDeniedException ex) {
    return ApiResult.of(HttpStatus.FORBIDDEN.value(), "forbidden", null);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResult<Void> handleUnknown(Exception ex) {
    LOGGER.error("Unhandled API exception", ex);
    return ApiResult.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal server error", null);
  }
}
