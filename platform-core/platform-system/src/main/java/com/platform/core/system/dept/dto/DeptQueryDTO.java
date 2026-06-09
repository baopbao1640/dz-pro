package com.platform.core.system.dept.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DeptQueryDTO {

  @Size(max = 128)
  private String deptName;

  @Pattern(regexp = "[01]")
  private String status;

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
