package com.platform.core.system.role.dto;

import java.util.ArrayList;
import java.util.List;

/** Role data permission scope assignment contract. */
public class RoleDataScopeDTO {

  private String dataScope;
  private List<Long> deptIds = new ArrayList<>();
  private String deptCheckStrictly;

  public String getDataScope() {
    return dataScope;
  }

  public void setDataScope(String dataScope) {
    this.dataScope = dataScope;
  }

  public List<Long> getDeptIds() {
    return deptIds;
  }

  public void setDeptIds(List<Long> deptIds) {
    this.deptIds = deptIds;
  }

  public String getDeptCheckStrictly() {
    return deptCheckStrictly;
  }

  public void setDeptCheckStrictly(String deptCheckStrictly) {
    this.deptCheckStrictly = deptCheckStrictly;
  }
}
