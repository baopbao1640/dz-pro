package com.platform.core.system.user.dto;

import com.platform.core.common.api.PageQuery;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public class UserPageQueryDTO extends PageQuery {

  private Long deptId;
  private Boolean includeChildren;

  @Size(max = 64)
  private String userName;

  @Size(max = 64)
  private String nickName;

  @Size(max = 32)
  private String phoneNumber;

  @Pattern(regexp = "[01]")
  private String status;

  private OffsetDateTime createdStartTime;
  private OffsetDateTime createdEndTime;

  public Long getDeptId() {
    return deptId;
  }

  public void setDeptId(Long deptId) {
    this.deptId = deptId;
  }

  public Boolean getIncludeChildren() {
    return includeChildren;
  }

  public void setIncludeChildren(Boolean includeChildren) {
    this.includeChildren = includeChildren;
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

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
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
