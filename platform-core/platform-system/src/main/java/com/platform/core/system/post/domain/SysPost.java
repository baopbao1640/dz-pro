package com.platform.core.system.post.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.core.common.domain.AuditFields;

@TableName("sys_post")
public class SysPost extends AuditFields {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private String postCode;
  private String postName;
  private Integer postSort;
  private String status;
  private String delFlag;

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

  public String getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(String delFlag) {
    this.delFlag = delFlag;
  }
}
