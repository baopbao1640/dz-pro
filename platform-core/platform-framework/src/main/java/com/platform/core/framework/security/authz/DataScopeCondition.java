package com.platform.core.framework.security.authz;

import java.util.List;
import java.util.Objects;

/**
 * 数据权限 SQL 条件契约，承载业务侧计算出的条件片段和参数。
 *
 * <p>职责：在 framework 与后续 MyBatis/AOP 应用点之间传递数据范围结果。
 *
 * <p>边界：本 record 不拼接到具体 Mapper，也不验证 SQL 片段来源。
 *
 * <p>当前阶段能力：表达无限制条件或带参数的数据范围条件。
 */
/*
 * Boundary:
 * 只保存已计算的条件，不决定条件应用到哪些业务 SQL。
 */
/*
 * Deferred:
 * 后续需要统一参数绑定和 SQL 注入点，避免不同 Mapper 自行拼接。
 */
/*
 * Risk:
 * 如果调用方传入未受控 SQL 片段，会扩大 SQL 注入风险；当前只能由实现方保证来源可信。
 */
public record DataScopeCondition(String sqlSegment, List<Object> parameters) {

  public DataScopeCondition {
    sqlSegment = Objects.requireNonNull(sqlSegment, "sqlSegment");
    parameters = parameters == null ? List.of() : List.copyOf(parameters);
  }

  /**
   * 构建不限制数据范围的条件。
   *
   * <p>关键规则：用于超级管理员或 ALL 数据范围，不代表跳过认证授权。
   *
   * <p>返回含义：返回空 SQL 片段和空参数列表。
   */
  public static DataScopeCondition unrestricted() {
    return new DataScopeCondition("", List.of());
  }
}
