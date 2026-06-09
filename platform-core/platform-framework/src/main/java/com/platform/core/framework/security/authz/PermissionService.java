package com.platform.core.framework.security.authz;

import java.util.Collection;
import java.util.Set;

/** 权限服务是 framework 对业务权限来源的抽象。Keycloak 只负责认证身份，具体权限码仍来自 system 的角色、菜单和按钮授权，后续可在这里接入缓存和失效策略。 */
public interface PermissionService {

  boolean hasPermission(String permissionCode);

  boolean hasAnyPermission(Collection<String> permissionCodes);

  Set<String> listPermissionCodes();
}
