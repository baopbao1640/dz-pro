package com.platform.core.system.role.dto;

/** Role create and edit contract. */
public class RoleSaveDTO {

  private String roleName;
  private String roleKey;
  private Integer roleSort;
  private String dataScope;
  private String menuCheckStrictly;
  private String deptCheckStrictly;
  private String status;
  private String remark;

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getRoleKey() {
    return roleKey;
  }

  public void setRoleKey(String roleKey) {
    this.roleKey = roleKey;
  }

  public Integer getRoleSort() {
    return roleSort;
  }

  public void setRoleSort(Integer roleSort) {
    this.roleSort = roleSort;
  }

  public String getDataScope() {
    return dataScope;
  }

  public void setDataScope(String dataScope) {
    this.dataScope = dataScope;
  }

  public String getMenuCheckStrictly() {
    return menuCheckStrictly;
  }

  public void setMenuCheckStrictly(String menuCheckStrictly) {
    this.menuCheckStrictly = menuCheckStrictly;
  }

  public String getDeptCheckStrictly() {
    return deptCheckStrictly;
  }

  public void setDeptCheckStrictly(String deptCheckStrictly) {
    this.deptCheckStrictly = deptCheckStrictly;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
