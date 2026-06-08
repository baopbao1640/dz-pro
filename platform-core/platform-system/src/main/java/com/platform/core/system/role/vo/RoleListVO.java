package com.platform.core.system.role.vo;

import java.time.OffsetDateTime;

/** Role table row view. */
public class RoleListVO {

  private Long id;
  private String roleName;
  private String roleKey;
  private Integer roleSort;
  private String dataScope;
  private String dataScopeLabel;
  private String status;
  private OffsetDateTime createTime;

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

  public String getDataScopeLabel() {
    return dataScopeLabel;
  }

  public void setDataScopeLabel(String dataScopeLabel) {
    this.dataScopeLabel = dataScopeLabel;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public OffsetDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(OffsetDateTime createTime) {
    this.createTime = createTime;
  }
}
