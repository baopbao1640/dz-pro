package com.platform.core.common.api;

import java.util.List;

/** Standard page response payload. */
public record PageResult<T>(List<T> records, long total, long pageNum, long pageSize) {

  public static <T> PageResult<T> of(List<T> records, long total, long pageNum, long pageSize) {
    return new PageResult<>(List.copyOf(records), total, pageNum, pageSize);
  }

  public static <T> PageResult<T> empty(long pageNum, long pageSize) {
    return new PageResult<>(List.of(), 0, pageNum, pageSize);
  }
}
