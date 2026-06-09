package com.platform.core.system.auth.controller;

import com.platform.core.common.api.ApiResult;
import com.platform.core.framework.security.context.CurrentUser;
import com.platform.core.framework.security.context.CurrentUserProvider;
import com.platform.core.framework.security.route.DynamicRouteService;
import com.platform.core.framework.security.route.RouteDefinition;
import com.platform.core.system.auth.vo.AuthProfileVO;
import java.util.List;
import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统认证授权查询入口，向前端提供当前用户 profile、动态路由和权限码。
 *
 * <p>职责：把 framework 当前用户契约和动态路由契约包装成 `/api/system/auth/**` API。
 *
 * <p>边界：不执行登录、不签发 token、不替代 Keycloak 认证流程。
 *
 * <p>当前阶段能力：支撑 Phase 3B 前端权限加载和按钮权限判断。
 */
@RestController
@RequestMapping("/api/system/auth")
public class SystemAuthController {

  private final CurrentUserProvider currentUserProvider;
  private final DynamicRouteService dynamicRouteService;

  public SystemAuthController(
      CurrentUserProvider currentUserProvider, DynamicRouteService dynamicRouteService) {
    this.currentUserProvider = currentUserProvider;
    this.dynamicRouteService = dynamicRouteService;
  }

  /**
   * 查询当前登录用户业务身份摘要。
   *
   * <p>关键规则：必须存在可映射的本地用户，否则 `CurrentUserProvider` 会拒绝访问。
   *
   * <p>返回含义：返回用户、部门、角色、权限和数据范围摘要。
   */
  @GetMapping("/profile")
  public ApiResult<AuthProfileVO> profile() {
    return ApiResult.success(toProfile(currentUserProvider.requireCurrentUser()));
  }

  /**
   * 查询当前用户可访问的动态路由树。
   *
   * <p>权限影响：路由来自当前用户角色菜单授权，只控制前端页面入口。
   *
   * <p>返回含义：返回 Vue Router 可消费的路由定义集合。
   */
  @GetMapping("/routes")
  public ApiResult<List<RouteDefinition>> routes() {
    return ApiResult.success(dynamicRouteService.listCurrentUserRoutes());
  }

  /**
   * 查询当前用户 permission code 集合。
   *
   * <p>安全影响：前端只能用该集合做体验控制，后端接口仍必须使用 `@RequiresPermission` enforce。
   *
   * <p>返回含义：返回按钮和接口权限码，超级管理员可包含 `*:*:*`。
   */
  @GetMapping("/permissions")
  public ApiResult<Set<String>> permissions() {
    return ApiResult.success(dynamicRouteService.listCurrentUserPermissionCodes());
  }

  private AuthProfileVO toProfile(CurrentUser currentUser) {
    AuthProfileVO vo = new AuthProfileVO();
    vo.setUserId(currentUser.userId());
    vo.setKeycloakUserId(currentUser.keycloakUserId());
    vo.setUserName(currentUser.userName());
    vo.setDeptId(currentUser.deptId());
    vo.setRoleIds(currentUser.roleIds());
    vo.setRoleKeys(currentUser.roleKeys());
    vo.setPermissions(currentUser.permissionCodes());
    vo.setDataScope(currentUser.dataScope());
    vo.setSuperAdmin(currentUser.superAdmin());
    return vo;
  }
}
