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
 * system 模块在这里把 Keycloak subject 解释成本地业务用户。这样 framework 只依赖 `CurrentUserProvider` 契约，不需要读取
 * `sys_user`、角色或菜单表。
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
