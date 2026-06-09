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

@Service
public class SystemDynamicRouteService implements DynamicRouteService {

  private final CurrentUserProvider currentUserProvider;
  private final SysMenuService menuService;

  public SystemDynamicRouteService(
      CurrentUserProvider currentUserProvider, SysMenuService menuService) {
    this.currentUserProvider = currentUserProvider;
    this.menuService = menuService;
  }

  @Override
  public List<RouteDefinition> listCurrentUserRoutes() {
    CurrentUser currentUser = currentUserProvider.requireCurrentUser();
    return menuService.routesForRoleIds(currentUser.roleIds()).stream()
        .map(this::toDefinition)
        .toList();
  }

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
