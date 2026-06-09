package com.platform.core.framework.security.context;

import java.util.Objects;
import java.util.Set;

/**
 * 当前用户上下文保存平台业务身份，而不是完整 Keycloak token。这样可以保持认证边界清晰： Keycloak 提供 subject，system 负责映射成本地
 * `sys_user.id`、角色和权限码。
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
