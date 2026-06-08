package com.platform.core.common.api;

import java.util.Objects;

/** Standard REST response envelope for platform APIs. */
public record ApiResult<T>(int code, String message, T data) {

  public static final int SUCCESS_CODE = 200;
  public static final int ERROR_CODE = 500;

  public static <T> ApiResult<T> success(T data) {
    return new ApiResult<>(SUCCESS_CODE, "success", data);
  }

  public static ApiResult<Void> success() {
    return new ApiResult<>(SUCCESS_CODE, "success", null);
  }

  public static <T> ApiResult<T> error(String message) {
    return new ApiResult<>(ERROR_CODE, Objects.requireNonNullElse(message, "error"), null);
  }
}
