package com.platform.core.system.role.vo;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/** Role detail view including relationship IDs. */
public class RoleDetailVO {

  private Long id;
  private String roleName;
  private String roleKey;
  private Integer roleSort;
  private String dataScope;
  private String menuCheckStrictly;
  private String deptCheckStrictly;
  private String status;
  private String remark;
  private OffsetDateTime createTime;
  private List<Long> menuIds = new ArrayList<>();
  private List<Long> halfCheckedMenuIds = new ArrayList<>();
  private List<Long> deptIds = new ArrayList<>();
  private Long assignedUserCount;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public OffsetDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(OffsetDateTime createTime) {
    this.createTime = createTime;
  }

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

  public List<Long> getDeptIds() {
    return deptIds;
  }

  public void setDeptIds(List<Long> deptIds) {
    this.deptIds = deptIds;
  }

  public Long getAssignedUserCount() {
    return assignedUserCount;
  }

  public void setAssignedUserCount(Long assignedUserCount) {
    this.assignedUserCount = assignedUserCount;
  }
}
