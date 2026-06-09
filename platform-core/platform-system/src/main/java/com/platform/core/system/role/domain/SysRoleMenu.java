package com.platform.core.system.role.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.core.common.domain.AuditFields;

/** Role to menu and button permission relationship. */
@TableName("sys_role_menu")
public class SysRoleMenu extends AuditFields {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  @TableField("role_id")
  private Long roleId;

  @TableField("menu_id")
  private Long menuId;

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

  public Long getMenuId() {
    return menuId;
  }

  public void setMenuId(Long menuId) {
    this.menuId = menuId;
  }
}
