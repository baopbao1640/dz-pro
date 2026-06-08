package com.platform.core.system.dept.vo;

import java.util.ArrayList;
import java.util.List;

public class DeptTreeVO {

  private Long id;
  private Long parentId;
  private String ancestors;
  private String deptName;
  private Integer orderNum;
  private Long leaderUserId;
  private String leaderName;
  private String phone;
  private String email;
  private String status;
  private List<DeptTreeVO> children = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public String getAncestors() {
    return ancestors;
  }

  public void setAncestors(String ancestors) {
    this.ancestors = ancestors;
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

  public String getLeaderName() {
    return leaderName;
  }

  public void setLeaderName(String leaderName) {
    this.leaderName = leaderName;
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

  public List<DeptTreeVO> getChildren() {
    return children;
  }

  public void setChildren(List<DeptTreeVO> children) {
    this.children = children;
  }
}
