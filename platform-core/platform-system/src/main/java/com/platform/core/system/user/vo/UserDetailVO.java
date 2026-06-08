package com.platform.core.system.user.vo;

import java.util.ArrayList;
import java.util.List;

public class UserDetailVO extends UserListVO {

  private List<Long> roleIds = new ArrayList<>();
  private List<Long> postIds = new ArrayList<>();
  private String dataScopeSummary;
  private List<String> permissionCodes = new ArrayList<>();
  private List<Long> menuIds = new ArrayList<>();

  public List<Long> getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(List<Long> roleIds) {
    this.roleIds = roleIds;
  }

  public List<Long> getPostIds() {
    return postIds;
  }

  public void setPostIds(List<Long> postIds) {
    this.postIds = postIds;
  }

  public String getDataScopeSummary() {
    return dataScopeSummary;
  }

  public void setDataScopeSummary(String dataScopeSummary) {
    this.dataScopeSummary = dataScopeSummary;
  }

  public List<String> getPermissionCodes() {
    return permissionCodes;
  }

  public void setPermissionCodes(List<String> permissionCodes) {
    this.permissionCodes = permissionCodes;
  }

  public List<Long> getMenuIds() {
    return menuIds;
  }

  public void setMenuIds(List<Long> menuIds) {
    this.menuIds = menuIds;
  }
}
