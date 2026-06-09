package com.platform.core.framework.security.route;

import java.util.List;
import java.util.Set;

/**
 * 动态路由服务契约，向前端暴露当前用户可见的页面路由和按钮权限码。
 *
 * <p>职责：为前端后台菜单、路由注入和按钮权限判断提供统一入口。
 *
 * <p>边界：framework 不查询菜单表，不决定路由树排序和组件映射规则。
 *
 * <p>当前阶段能力：system 模块基于角色菜单关系返回路由树和 permission code 列表。
 */
/*
 * Boundary:
 * 路由来源属于 system 菜单授权；前端权限判断只能改善体验，不能替代后端 enforce。
 */
/*
 * Deferred:
 * 暂未支持前端组件白名单校验、路由缓存和菜单变更实时失效。
 */
/*
 * Risk:
 * 如果后端返回组件路径与前端实际文件不一致，动态路由会出现空页面或跳转失败。
 */
public interface DynamicRouteService {

  /**
   * 查询当前用户可访问的路由树。
   *
   * <p>关键规则：只返回页面级目录和菜单，不把按钮权限直接伪装成路由。
   *
   * <p>返回含义：返回适配前端动态路由注入的路由定义集合。
   */
  List<RouteDefinition> listCurrentUserRoutes();

  /**
   * 查询当前用户权限码集合。
   *
   * <p>安全影响：该集合用于前端按钮显隐，但真实授权仍以后端 `@RequiresPermission` 为准。
   *
   * <p>返回含义：返回当前用户可用 permission code，超级管理员可包含 `*:*:*`。
   */
  Set<String> listCurrentUserPermissionCodes();
}
