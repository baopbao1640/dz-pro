package com.platform.core.system.role.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.core.common.domain.AuditFields;

/** Role definition and data permission scope. */
@TableName("sys_role")
public class SysRole extends AuditFields {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  @TableField("role_name")
  private String roleName;

  @TableField("role_key")
  private String roleKey;

  @TableField("role_sort")
  private Integer roleSort;

  @TableField("data_scope")
  private String dataScope;

  @TableField("menu_check_strictly")
  private String menuCheckStrictly;

  @TableField("dept_check_strictly")
  private String deptCheckStrictly;

  private String status;

  @TableField("del_flag")
  private String delFlag;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Integer getRoleSort() {
    return roleSort;
  }

  public void setRoleSort(Integer roleSort) {
    this.roleSort = roleSort;
  }

  public String getDataScope() {
    return dataScope;
  }

  public void setDataScope(String dataScope) {
    this.dataScope = dataScope;
  }

  public String getMenuCheckStrictly() {
    return menuCheckStrictly;
  }

  public void setMenuCheckStrictly(String menuCheckStrictly) {
    this.menuCheckStrictly = menuCheckStrictly;
  }

  public String getDeptCheckStrictly() {
    return deptCheckStrictly;
  }

  public void setDeptCheckStrictly(String deptCheckStrictly) {
    this.deptCheckStrictly = deptCheckStrictly;
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
