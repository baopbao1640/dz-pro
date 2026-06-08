package com.platform.core.system.role.dto;

import java.time.OffsetDateTime;

/** Role page query contract. */
public class RolePageQueryDTO {

  private Integer pageNum;
  private Integer pageSize;
  private String roleName;
  private String roleKey;
  private String status;
  private OffsetDateTime createdStartTime;
  private OffsetDateTime createdEndTime;

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public OffsetDateTime getCreatedStartTime() {
    return createdStartTime;
  }

  public void setCreatedStartTime(OffsetDateTime createdStartTime) {
    this.createdStartTime = createdStartTime;
  }

  public OffsetDateTime getCreatedEndTime() {
    return createdEndTime;
  }

  public void setCreatedEndTime(OffsetDateTime createdEndTime) {
    this.createdEndTime = createdEndTime;
  }
}
