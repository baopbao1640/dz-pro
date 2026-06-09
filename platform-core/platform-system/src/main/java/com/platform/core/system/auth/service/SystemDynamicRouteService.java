package com.platform.core.system.auth.service;

import com.platform.core.framework.security.context.CurrentUser;
import com.platform.core.framework.security.context.CurrentUserProvider;
import com.platform.core.framework.security.route.DynamicRouteService;
import com.platform.core.framework.security.route.RouteDefinition;
import com.platform.core.framework.security.route.RouteMeta;
import com.platform.core.system.menu.service.SysMenuService;
import com.platform.core.system.menu.vo.RouteVO;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * system 动态路由服务实现，负责把菜单授权结果转换为 framework 路由契约。
 *
 * <p>职责：根据当前用户角色读取菜单路由，并提供权限码集合。
 *
 * <p>边界：不加载前端组件文件，不判断前端组件是否存在。
 *
 * <p>当前阶段能力：支撑 Phase 3B 登录后动态菜单和按钮权限状态。
 */
/*
 * Boundary:
 * 路由树只表示页面入口，按钮权限通过 permission code 单独返回。
 */
/*
 * Deferred:
 * 组件白名单、动态路由缓存和菜单变更实时刷新暂未实现。
 */
/*
 * Risk:
 * 后端菜单 component 配置错误会导致前端路由加载失败，需要后续治理菜单配置校验。
 */
@Service
public class SystemDynamicRouteService implements DynamicRouteService {

  private final CurrentUserProvider currentUserProvider;
  private final SysMenuService menuService;

  public SystemDynamicRouteService(
      CurrentUserProvider currentUserProvider, SysMenuService menuService) {
    this.currentUserProvider = currentUserProvider;
    this.menuService = menuService;
  }

  /**
   * 查询当前用户可访问路由。
   *
   * <p>关键规则：只基于当前用户角色菜单关系返回页面级路由。
   *
   * <p>返回含义：返回 framework `RouteDefinition`，供 `/api/system/auth/routes` 输出。
   */
  @Override
  public List<RouteDefinition> listCurrentUserRoutes() {
    CurrentUser currentUser = currentUserProvider.requireCurrentUser();
    return menuService.routesForRoleIds(currentUser.roleIds()).stream()
        .map(this::toDefinition)
        .toList();
  }

  /**
   * 查询当前用户权限码集合。
   *
   * <p>安全影响：该集合服务前端按钮显隐，真实接口授权仍依赖后端注解。
   */
  @Override
  public Set<String> listCurrentUserPermissionCodes() {
    return currentUserProvider.requireCurrentUser().permissionCodes();
  }

  private RouteDefinition toDefinition(RouteVO route) {
    RouteMetaVOAdapter meta = new RouteMetaVOAdapter(route.getMeta());
    return new RouteDefinition(
        route.getName(),
        route.getPath(),
        route.getComponent(),
        route.getRedirect(),
        new RouteMeta(meta.title(), meta.icon(), meta.hidden(), meta.keepAlive()),
        route.getChildren().stream().map(this::toDefinition).toList());
  }

  private record RouteMetaVOAdapter(RouteVO.RouteMetaVO source) {
    String title() {
      return source == null ? null : source.getTitle();
    }

    String icon() {
      return source == null ? null : source.getIcon();
    }

    boolean hidden() {
      return source != null && Boolean.TRUE.equals(source.getHidden());
    }

    boolean keepAlive() {
      return source != null && Boolean.TRUE.equals(source.getKeepAlive());
    }
  }
}
