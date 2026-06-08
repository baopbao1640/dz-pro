package com.platform.core.framework.security.route;

import java.util.List;
import java.util.Set;

/** 动态路由服务只暴露当前用户可见路由和权限码。具体数据来源应是 system 菜单和角色授权， framework 不直接查询菜单表，避免路由能力反向绑定业务模块。 */
public interface DynamicRouteService {

  List<RouteDefinition> listCurrentUserRoutes();

  Set<String> listCurrentUserPermissionCodes();
}
