package com.platform.core.system.auth.service;

import com.platform.core.common.constant.StatusConstants;
import com.platform.core.framework.security.context.CurrentUser;
import com.platform.core.framework.security.context.CurrentUserProvider;
import com.platform.core.system.menu.service.SysMenuService;
import com.platform.core.system.role.domain.SysRole;
import com.platform.core.system.role.mapper.SysRoleMapper;
import com.platform.core.system.user.domain.SysUser;
import com.platform.core.system.user.mapper.SysUserMapper;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * 当前用户提供者的 system 实现，负责把 Keycloak JWT subject 映射成本地业务用户上下文。
 *
 * <p>职责：读取 `sys_user`、角色和菜单权限，组装 framework 所需的 `CurrentUser`。
 *
 * <p>边界：不创建本地用户，不保存 token，不实现 Keycloak 登录。
 *
 * <p>当前阶段能力：支持超级管理员 `*:*:*`、角色权限码和单一 data scope 摘要。
 */
/*
 * Boundary:
 * Keycloak 负责身份认证；本类只消费 JWT subject 并加载本地授权数据。
 */
/*
 * Deferred:
 * 用户自动同步、缓存失效和多租户上下文暂未实现，后续必须单独设计。
 */
/*
 * Risk:
 * `keycloak_user_id` 绑定错误会导致真实用户获得错误业务权限，种子数据上线前必须替换占位 subject。
 */
@Service
public class SystemCurrentUserProvider implements CurrentUserProvider {

  private static final String SUPER_ADMIN_ROLE = "super_admin";

  private final SysUserMapper userMapper;
  private final SysRoleMapper roleMapper;
  private final SysMenuService menuService;

  public SystemCurrentUserProvider(
      SysUserMapper userMapper, SysRoleMapper roleMapper, SysMenuService menuService) {
    this.userMapper = userMapper;
    this.roleMapper = roleMapper;
    this.menuService = menuService;
  }

  /**
   * 获取当前业务用户上下文。
   *
   * <p>关键规则：JWT subject 必须能映射到未删除且未禁用的本地用户。
   *
   * <p>权限影响：超级管理员角色会获得 `*:*:*`，普通用户从菜单权限码计算授权集合。
   *
   * <p>返回含义：无法映射时返回 empty，由 framework 拒绝访问。
   */
  @Override
  public Optional<CurrentUser> getCurrentUser() {
    String subject = resolveSubject();
    if (subject == null) {
      return Optional.empty();
    }
    SysUser user = userMapper.selectActiveByKeycloakUserId(subject);
    if (user == null || StatusConstants.DISABLED.equals(user.getStatus())) {
      return Optional.empty();
    }
    List<SysRole> roles = roleMapper.selectEnabledRolesByUserId(user.getId());
    Set<Long> roleIds =
        roles.stream().map(SysRole::getId).collect(LinkedHashSet::new, Set::add, Set::addAll);
    Set<String> roleKeys =
        roles.stream().map(SysRole::getRoleKey).collect(LinkedHashSet::new, Set::add, Set::addAll);
    boolean superAdmin = roleKeys.contains(SUPER_ADMIN_ROLE);
    Set<String> permissions =
        superAdmin
            ? Set.of("*:*:*")
            : new LinkedHashSet<>(menuService.permissionCodesForRoleIds(roleIds));
    String dataScope =
        roles.stream().map(SysRole::getDataScope).min(Comparator.naturalOrder()).orElse("5");
    return Optional.of(
        new CurrentUser(
            user.getId(),
            user.getKeycloakUserId(),
            user.getUserName(),
            user.getDeptId(),
            roleIds,
            roleKeys,
            permissions,
            dataScope,
            superAdmin));
  }

  private String resolveSubject() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
      return jwtAuthentication.getToken().getSubject();
    }
    return null;
  }
}
