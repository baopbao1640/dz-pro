package com.platform.core.system.dept.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DeptSaveDTO {

  @NotNull private Long parentId;

  @NotBlank
  @Size(min = 2, max = 128)
  private String deptName;

  @NotNull
  @Min(0)
  private Integer orderNum;

  private Long leaderUserId;

  @Size(max = 32)
  private String phone;

  @Email
  @Size(max = 128)
  private String email;

  @Pattern(regexp = "[01]")
  private String status;

  @Size(max = 500)
  private String remark;

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public Integer getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(Integer orderNum) {
    this.orderNum = orderNum;
  }

  public Long getLeaderUserId() {
    return leaderUserId;
  }

  public void setLeaderUserId(Long leaderUserId) {
    this.leaderUserId = leaderUserId;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
