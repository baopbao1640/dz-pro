package com.platform.core.system.role.enums;

/** Data permission scope values defined by the system role PRD. */
public enum RoleDataScope {
  ALL("1"),
  CUSTOM("2"),
  DEPT("3"),
  DEPT_AND_CHILD("4"),
  SELF("5");

  private final String code;

  RoleDataScope(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public static boolean supports(String code) {
    for (RoleDataScope scope : values()) {
      if (scope.code.equals(code)) {
        return true;
      }
    }
    return false;
  }
}
