package com.platform.core.system.audit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.OffsetDateTime;

@TableName("sys_oper_log")
public class SysOperLog {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private String createBy;
  private OffsetDateTime createTime;
  private String traceId;
  private String moduleTitle;
  private String businessType;
  private Long operUserId;
  private String operName;
  private Long deptId;
  private String operParam;
  private String jsonResult;
  private String status;
  private OffsetDateTime operTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCreateBy() {
    return createBy;
  }

  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }

  public OffsetDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(OffsetDateTime createTime) {
    this.createTime = createTime;
  }

  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public String getModuleTitle() {
    return moduleTitle;
  }

  public void setModuleTitle(String moduleTitle) {
    this.moduleTitle = moduleTitle;
  }

  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

  public Long getOperUserId() {
    return operUserId;
  }

  public void setOperUserId(Long operUserId) {
    this.operUserId = operUserId;
  }

  public String getOperName() {
    return operName;
  }

  public void setOperName(String operName) {
    this.operName = operName;
  }

  public Long getDeptId() {
    return deptId;
  }

  public void setDeptId(Long deptId) {
    this.deptId = deptId;
  }

  public String getOperParam() {
    return operParam;
  }

  public void setOperParam(String operParam) {
    this.operParam = operParam;
  }

  public String getJsonResult() {
    return jsonResult;
  }

  public void setJsonResult(String jsonResult) {
    this.jsonResult = jsonResult;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public OffsetDateTime getOperTime() {
    return operTime;
  }

  public void setOperTime(OffsetDateTime operTime) {
    this.operTime = operTime;
  }
}
