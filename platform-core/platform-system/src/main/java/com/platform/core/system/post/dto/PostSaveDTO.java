package com.platform.core.system.post.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PostSaveDTO {

  @NotBlank
  @Size(min = 2, max = 64)
  private String postCode;

  @NotBlank
  @Size(min = 2, max = 128)
  private String postName;

  @NotNull
  @Min(0)
  private Integer postSort;

  @Pattern(regexp = "[01]")
  private String status;

  @Size(max = 500)
  private String remark;

  public String getPostCode() {
    return postCode;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  public String getPostName() {
    return postName;
  }

  public void setPostName(String postName) {
    this.postName = postName;
  }

  public Integer getPostSort() {
    return postSort;
  }

  public void setPostSort(Integer postSort) {
    this.postSort = postSort;
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
