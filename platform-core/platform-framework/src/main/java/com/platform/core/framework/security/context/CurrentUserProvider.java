package com.platform.core.framework.security.context;

import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;

/**
 * 当前用户提供者，是 Spring Security 认证结果与平台业务授权之间的扩展边界。
 *
 * <p>职责：向 framework 能力提供当前业务用户上下文。
 *
 * <p>边界：接口不规定用户如何查询、角色如何加载或权限如何缓存。
 *
 * <p>当前阶段能力：system 模块从 JWT subject 映射本地用户并组装权限摘要。
 */
/*
 * Boundary:
 * framework 只依赖接口，不能反向访问 `sys_user`、角色或菜单表。
 */
/*
 * Deferred:
 * 后续可接入缓存、租户上下文和用户同步状态，但必须保持认证边界仍属于 Keycloak。
 */
/*
 * Risk:
 * 当前用户缺失时会拒绝访问；调用方不能把 empty 当作匿名业务用户继续处理。
 */
public interface CurrentUserProvider {

  /**
   * 获取当前业务用户上下文。
   *
   * <p>边界条件：未认证、JWT 无法映射、用户禁用或删除时返回 empty。
   *
   * <p>返回含义：返回可用于授权和审计的本地业务用户摘要。
   */
  Optional<CurrentUser> getCurrentUser();

  /**
   * 获取当前业务用户，缺失时直接拒绝访问。
   *
   * <p>安全影响：用于必须登录且必须有本地用户映射的授权入口。
   *
   * <p>异常行为：当前用户不可用时抛出 `AccessDeniedException`。
   */
  default CurrentUser requireCurrentUser() {
    return getCurrentUser()
        .orElseThrow(() -> new AccessDeniedException("Current user is not available"));
  }
}
