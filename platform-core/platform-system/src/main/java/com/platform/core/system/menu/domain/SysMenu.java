package com.platform.core.system.menu.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.core.common.domain.AuditFields;

/** Directory, menu, and function permission resource. */
@TableName("sys_menu")
public class SysMenu extends AuditFields {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  @TableField("menu_name")
  private String menuName;

  @TableField("parent_id")
  private Long parentId;

  @TableField("order_num")
  private Integer orderNum;

  private String path;
  private String component;

  @TableField("query_param")
  private String queryParam;

  @TableField("route_name")
  private String routeName;

  @TableField("is_frame")
  private String isFrame;

  @TableField("is_cache")
  private String isCache;

  @TableField("menu_type")
  private String menuType;

  private String visible;
  private String status;

  @TableField("permission_code")
  private String permissionCode;

  private String icon;

  @TableField("del_flag")
  private String delFlag;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMenuName() {
    return menuName;
  }

  public void setMenuName(String menuName) {
    this.menuName = menuName;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public Integer getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(Integer orderNum) {
    this.orderNum = orderNum;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public String getQueryParam() {
    return queryParam;
  }

  public void setQueryParam(String queryParam) {
    this.queryParam = queryParam;
  }

  public String getRouteName() {
    return routeName;
  }

  public void setRouteName(String routeName) {
    this.routeName = routeName;
  }

  public String getIsFrame() {
    return isFrame;
  }

  public void setIsFrame(String isFrame) {
    this.isFrame = isFrame;
  }

  public String getIsCache() {
    return isCache;
  }

  public void setIsCache(String isCache) {
    this.isCache = isCache;
  }

  public String getMenuType() {
    return menuType;
  }

  public void setMenuType(String menuType) {
    this.menuType = menuType;
  }

  public String getVisible() {
    return visible;
  }

  public void setVisible(String visible) {
    this.visible = visible;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPermissionCode() {
    return permissionCode;
  }

  public void setPermissionCode(String permissionCode) {
    this.permissionCode = permissionCode;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(String delFlag) {
    this.delFlag = delFlag;
  }
}
