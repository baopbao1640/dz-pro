package com.platform.core.system.auth.vo;

import java.util.Set;

public class AuthProfileVO {

  private Long userId;
  private String keycloakUserId;
  private String userName;
  private Long deptId;
  private Set<Long> roleIds;
  private Set<String> roleKeys;
  private Set<String> permissions;
  private String dataScope;
  private boolean superAdmin;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getKeycloakUserId() {
    return keycloakUserId;
  }

  public void setKeycloakUserId(String keycloakUserId) {
    this.keycloakUserId = keycloakUserId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Long getDeptId() {
    return deptId;
  }

  public void setDeptId(Long deptId) {
    this.deptId = deptId;
  }

  public Set<Long> getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(Set<Long> roleIds) {
    this.roleIds = roleIds;
  }

  public Set<String> getRoleKeys() {
    return roleKeys;
  }

  public void setRoleKeys(Set<String> roleKeys) {
    this.roleKeys = roleKeys;
  }

  public Set<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<String> permissions) {
    this.permissions = permissions;
  }

  public String getDataScope() {
    return dataScope;
  }

  public void setDataScope(String dataScope) {
    this.dataScope = dataScope;
  }

  public boolean isSuperAdmin() {
    return superAdmin;
  }

  public void setSuperAdmin(boolean superAdmin) {
    this.superAdmin = superAdmin;
  }
}
