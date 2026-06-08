package com.platform.core.system.menu.vo;

import java.util.ArrayList;
import java.util.List;

/** Menu tree node view. */
public class MenuTreeVO {

  private Long id;
  private Long parentId;
  private String menuName;
  private String menuType;
  private String path;
  private String component;
  private String routeName;
  private String permissionCode;
  private String icon;
  private String visible;
  private String isCache;
  private String isFrame;
  private String status;
  private Integer orderNum;
  private List<MenuTreeVO> children = new ArrayList<>();

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

  public String getMenuName() {
    return menuName;
  }

  public void setMenuName(String menuName) {
    this.menuName = menuName;
  }

  public String getMenuType() {
    return menuType;
  }

  public void setMenuType(String menuType) {
    this.menuType = menuType;
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

  public String getRouteName() {
    return routeName;
  }

  public void setRouteName(String routeName) {
    this.routeName = routeName;
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

  public String getVisible() {
    return visible;
  }

  public void setVisible(String visible) {
    this.visible = visible;
  }

  public String getIsCache() {
    return isCache;
  }

  public void setIsCache(String isCache) {
    this.isCache = isCache;
  }

  public String getIsFrame() {
    return isFrame;
  }

  public void setIsFrame(String isFrame) {
    this.isFrame = isFrame;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(Integer orderNum) {
    this.orderNum = orderNum;
  }

  public List<MenuTreeVO> getChildren() {
    return children;
  }

  public void setChildren(List<MenuTreeVO> children) {
    this.children = children;
  }
}
