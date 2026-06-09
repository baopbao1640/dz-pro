package com.platform.core.system.role.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.core.common.domain.AuditFields;

/** Role to department relationship for custom data permission scope. */
@TableName("sys_role_dept")
public class SysRoleDept extends AuditFields {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  @TableField("role_id")
  private Long roleId;

  @TableField("dept_id")
  private Long deptId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public Long getDeptId() {
    return deptId;
  }

  public void setDeptId(Long deptId) {
    this.deptId = deptId;
  }
}
