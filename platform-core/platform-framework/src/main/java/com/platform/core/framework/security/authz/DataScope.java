package com.platform.core.framework.security.authz;

import java.util.Arrays;
import java.util.Optional;

/**
 * 数据范围枚举，集中表达 `sys_role.data_scope` 的稳定编码。
 *
 * <p>职责：为 system 侧角色数据范围和 framework 侧条件契约提供统一含义。
 *
 * <p>边界：枚举只定义编码，不生成 SQL，也不判断部门树关系。
 *
 * <p>当前阶段能力：覆盖 ALL、CUSTOM、DEPT、DEPT_AND_CHILD、SELF 五类范围。
 */
/*
 * Boundary:
 * 数据范围语义在这里命名，具体部门、自定义范围和本人条件由业务模块解释。
 */
/*
 * Deferred:
 * CUSTOM 与 DEPT_AND_CHILD 的完整 SQL 展开暂未全局注入，后续在数据权限专项补齐。
 */
/*
 * Risk:
 * 编码必须与数据库字典和角色表保持一致；新增编码需要同步迁移、前端和测试。
 */
public enum DataScope {
  ALL("1"),
  CUSTOM("2"),
  DEPT("3"),
  DEPT_AND_CHILD("4"),
  SELF("5");

  private final String code;

  DataScope(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  /**
   * 根据数据库编码解析数据范围。
   *
   * <p>边界条件：未知编码返回 empty，由调用方决定默认降级策略。
   *
   * <p>返回含义：返回匹配的数据范围枚举，或表示无法识别的空结果。
   */
  public static Optional<DataScope> fromCode(String code) {
    return Arrays.stream(values()).filter(scope -> scope.code.equals(code)).findFirst();
  }
}
