package com.platform.core.framework.security.authz;

import java.util.List;
import java.util.Objects;

/**
 * 数据权限条件是 framework 与 MyBatis 拦截层之间的中间契约。当前只保存 SQL 片段和参数， Deferred: 后续需要统一参数绑定策略，避免字符串拼接扩大 SQL
 * 注入风险。
 */
public record DataScopeCondition(String sqlSegment, List<Object> parameters) {

  public DataScopeCondition {
    sqlSegment = Objects.requireNonNull(sqlSegment, "sqlSegment");
    parameters = parameters == null ? List.of() : List.copyOf(parameters);
  }

  public static DataScopeCondition unrestricted() {
    return new DataScopeCondition("", List.of());
  }
}
