package com.platform.core.framework.security.route;

import java.util.List;

/**
 * 前端动态路由定义，承载 system 菜单授权转换后的页面级路由节点。
 *
 * <p>职责：表达路由名称、路径、组件、重定向、元信息和子路由。
 *
 * <p>边界：按钮权限不生成 `RouteDefinition`，只进入 permission code 集合。
 *
 * <p>当前阶段能力：支持目录和菜单树返回给 Vue Router 注入。
 */
/*
 * Boundary:
 * 该契约只面向前端路由渲染，不表达后端接口授权。
 */
/*
 * Deferred:
 * 暂未校验 component 是否存在于前端可加载组件白名单，后续需要防止后端配置污染前端加载。
 */
/*
 * Risk:
 * children 使用空集合兜底，前端必须仍处理空子节点和隐藏路由。
 */
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
