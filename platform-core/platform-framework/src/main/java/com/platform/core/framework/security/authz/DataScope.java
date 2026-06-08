package com.platform.core.framework.security.authz;

import java.util.Arrays;
import java.util.Optional;

/**
 * 数据范围编码必须与 `sys_role.data_scope` 保持一致。这里使用枚举集中表达，是为了避免 Controller、Service 和 SQL 层各自硬编码导致权限含义漂移。
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

  public static Optional<DataScope> fromCode(String code) {
    return Arrays.stream(values()).filter(scope -> scope.code.equals(code)).findFirst();
  }
}
