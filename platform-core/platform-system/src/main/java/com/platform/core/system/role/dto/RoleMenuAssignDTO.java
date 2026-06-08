package com.platform.core.system.role.dto;

import java.util.ArrayList;
import java.util.List;

/** Role menu assignment contract preserving UI half-check state. */
public class RoleMenuAssignDTO {

  private List<Long> menuIds = new ArrayList<>();
  private List<Long> halfCheckedMenuIds = new ArrayList<>();
  private String menuCheckStrictly;

  public List<Long> getMenuIds() {
    return menuIds;
  }

  public void setMenuIds(List<Long> menuIds) {
    this.menuIds = menuIds;
  }

  public List<Long> getHalfCheckedMenuIds() {
    return halfCheckedMenuIds;
  }

  public void setHalfCheckedMenuIds(List<Long> halfCheckedMenuIds) {
    this.halfCheckedMenuIds = halfCheckedMenuIds;
  }

  public String getMenuCheckStrictly() {
    return menuCheckStrictly;
  }

  public void setMenuCheckStrictly(String menuCheckStrictly) {
    this.menuCheckStrictly = menuCheckStrictly;
  }
}
