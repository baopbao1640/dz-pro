package com.platform.core.framework.security.context;

import java.util.Objects;
import java.util.Set;

/**
 * 当前用户业务上下文，保存 Keycloak subject 映射后的本地用户、角色、权限和数据范围摘要。
 *
 * <p>职责：为授权、数据权限、审计和动态路由提供统一的当前用户视图。
 *
 * <p>边界：不保存完整 JWT、refresh token、session、MFA 或密码信息。
 *
 * <p>当前阶段能力：承载 super admin、permission code 和单一最高优先级 data scope。
 */
/*
 * Boundary:
 * Keycloak 负责身份认证，system 模块负责把 subject 映射成本地业务用户。
 */
/*
 * Deferred:
 * 暂未承载多租户、岗位数据范围和更细粒度部门集合，后续按权限专项扩展。
 */
/*
 * Risk:
 * 当前上下文依赖本地用户与 Keycloak subject 正确绑定；种子占位值未替换会导致真实登录失败。
 */
public record CurrentUser(
    Long userId,
    String keycloakUserId,
    String userName,
    Long deptId,
    Set<Long> roleIds,
    Set<String> roleKeys,
    Set<String> permissionCodes,
    String dataScope,
    boolean superAdmin) {

  public CurrentUser {
    keycloakUserId = Objects.requireNonNull(keycloakUserId, "keycloakUserId");
    userName = Objects.requireNonNull(userName, "userName");
    roleIds = roleIds == null ? Set.of() : Set.copyOf(roleIds);
    roleKeys = roleKeys == null ? Set.of() : Set.copyOf(roleKeys);
    permissionCodes = permissionCodes == null ? Set.of() : Set.copyOf(permissionCodes);
  }
}
