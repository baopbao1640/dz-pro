package com.platform.core.system.auth.service;

import com.platform.core.framework.security.authz.PermissionService;
import com.platform.core.framework.security.context.CurrentUser;
import com.platform.core.framework.security.context.CurrentUserProvider;
import java.util.Collection;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * system 权限服务实现，基于当前用户 permission code 执行后端授权判断。
 *
 * <p>职责：为 `@RequiresPermission` 提供 permission code 查询和匹配能力。
 *
 * <p>边界：不从 Keycloak 读取权限，不修改角色菜单授权关系。
 *
 * <p>当前阶段能力：支持超级管理员通配符和普通权限码集合匹配。
 */
/*
 * Boundary:
 * 权限来源是 system 菜单角色关系，Keycloak 只提供认证身份。
 */
/*
 * Deferred:
 * 权限缓存和授权变更实时失效暂未实现，后续需要结合 Redis 或事件机制补齐。
 */
/*
 * Risk:
 * 前端权限码不能替代此服务；所有核心接口必须继续依赖后端 enforce。
 */
@Service
public class SystemPermissionService implements PermissionService {

  private static final String ALL_PERMISSION = "*:*:*";

  private final CurrentUserProvider currentUserProvider;

  public SystemPermissionService(CurrentUserProvider currentUserProvider) {
    this.currentUserProvider = currentUserProvider;
  }

  /**
   * 判断当前用户是否拥有指定权限码。
   *
   * <p>关键规则：超级管理员或 `*:*:*` 直接放行，普通用户必须显式包含权限码。
   *
   * <p>异常行为：当前用户不可用时由 `CurrentUserProvider` 抛出访问拒绝。
   */
  @Override
  public boolean hasPermission(String permissionCode) {
    CurrentUser currentUser = currentUserProvider.requireCurrentUser();
    return currentUser.superAdmin()
        || currentUser.permissionCodes().contains(ALL_PERMISSION)
        || currentUser.permissionCodes().contains(permissionCode);
  }

  /**
   * 判断当前用户是否拥有任一权限码。
   *
   * <p>边界条件：空权限集合视为无需权限，保持 annotation 空值的兼容行为。
   *
   * <p>返回含义：任一权限匹配即返回 true。
   */
  @Override
  public boolean hasAnyPermission(Collection<String> permissionCodes) {
    if (permissionCodes == null || permissionCodes.isEmpty()) {
      return true;
    }
    return permissionCodes.stream().anyMatch(this::hasPermission);
  }

  /**
   * 列出当前用户权限码集合。
   *
   * <p>返回含义：用于后端授权辅助和前端权限展示，不能作为前端唯一安全边界。
   */
  @Override
  public Set<String> listPermissionCodes() {
    return currentUserProvider.requireCurrentUser().permissionCodes();
  }
}
