package com.platform.core.system.user.dto;

import java.util.List;

public class UserAssignRoleDTO {

  private List<Long> roleIds;

  public List<Long> getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(List<Long> roleIds) {
    this.roleIds = roleIds;
  }
}
