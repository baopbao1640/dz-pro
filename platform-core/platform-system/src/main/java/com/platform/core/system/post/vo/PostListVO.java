package com.platform.core.system.post.vo;

import java.time.OffsetDateTime;

public class PostListVO {

  private Long id;
  private String postCode;
  private String postName;
  private Integer postSort;
  private String status;
  private Long boundUserCount;
  private OffsetDateTime createTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public Long getBoundUserCount() {
    return boundUserCount;
  }

  public void setBoundUserCount(Long boundUserCount) {
    this.boundUserCount = boundUserCount;
  }

  public OffsetDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(OffsetDateTime createTime) {
    this.createTime = createTime;
  }
}
