package com.platform.core.system.menu.enums;

/** Menu resource types backed by sys_menu.menu_type. */
public enum MenuType {
  DIRECTORY("M"),
  MENU("C"),
  FUNCTION("F");

  private final String code;

  MenuType(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public static boolean supports(String code) {
    for (MenuType type : values()) {
      if (type.code.equals(code)) {
        return true;
      }
    }
    return false;
  }
}
