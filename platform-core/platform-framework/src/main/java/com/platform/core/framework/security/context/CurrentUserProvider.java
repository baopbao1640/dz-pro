package com.platform.core.framework.security.context;

import java.util.Optional;

/**
 * 当前用户提供者是认证结果与业务授权之间的边界。Deferred: 当前仍是接口契约，后续需要从 Spring Security principal 解析 Keycloak
 * subject，并加载本地用户、角色和权限。
 */
public interface CurrentUserProvider {

  Optional<CurrentUser> getCurrentUser();

  default CurrentUser requireCurrentUser() {
    return getCurrentUser()
        .orElseThrow(() -> new IllegalStateException("Current user is not available"));
  }
}
