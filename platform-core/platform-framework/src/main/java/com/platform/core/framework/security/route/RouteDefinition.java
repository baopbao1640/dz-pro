package com.platform.core.framework.security.route;

import java.util.List;

/** 前端动态路由契约对应 `sys_menu` 中目录和菜单。按钮权限不应生成路由，只应进入权限码集合， 以便前端区分页面级可见性和按钮级授权。 */
public record RouteDefinition(
    String name,
    String path,
    String component,
    String redirect,
    RouteMeta meta,
    List<RouteDefinition> children) {

  public RouteDefinition {
    children = children == null ? List.of() : List.copyOf(children);
  }
}
