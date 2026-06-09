package com.platform.core.system.user.vo;

import java.time.OffsetDateTime;

public class UserListVO {

  private Long id;
  private String keycloakUserId;
  private String userName;
  private String nickName;
  private Long deptId;
  private String deptName;
  private String postNames;
  private String roleNames;
  private String email;
  private String phoneNumber;
  private String sex;
  private String status;
  private OffsetDateTime lastSyncTime;
  private OffsetDateTime createTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public Long getDeptId() {
    return deptId;
  }

  public void setDeptId(Long deptId) {
    this.deptId = deptId;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public String getPostNames() {
    return postNames;
  }

  public void setPostNames(String postNames) {
    this.postNames = postNames;
  }

  public String getRoleNames() {
    return roleNames;
  }

  public void setRoleNames(String roleNames) {
    this.roleNames = roleNames;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public OffsetDateTime getLastSyncTime() {
    return lastSyncTime;
  }

  public void setLastSyncTime(OffsetDateTime lastSyncTime) {
    this.lastSyncTime = lastSyncTime;
  }

  public OffsetDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(OffsetDateTime createTime) {
    this.createTime = createTime;
  }
}
