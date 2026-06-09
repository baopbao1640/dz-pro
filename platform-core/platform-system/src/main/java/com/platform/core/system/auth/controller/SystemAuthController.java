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

  @GetMapping("/profile")
  public ApiResult<AuthProfileVO> profile() {
    return ApiResult.success(toProfile(currentUserProvider.requireCurrentUser()));
  }

  @GetMapping("/routes")
  public ApiResult<List<RouteDefinition>> routes() {
    return ApiResult.success(dynamicRouteService.listCurrentUserRoutes());
  }

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
