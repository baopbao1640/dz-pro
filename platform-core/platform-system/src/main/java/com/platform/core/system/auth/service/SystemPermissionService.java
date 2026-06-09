package com.platform.core.system.auth.service;

import com.platform.core.framework.security.authz.PermissionService;
import com.platform.core.framework.security.context.CurrentUser;
import com.platform.core.framework.security.context.CurrentUserProvider;
import java.util.Collection;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class SystemPermissionService implements PermissionService {

  private static final String ALL_PERMISSION = "*:*:*";

  private final CurrentUserProvider currentUserProvider;

  public SystemPermissionService(CurrentUserProvider currentUserProvider) {
    this.currentUserProvider = currentUserProvider;
  }

  @Override
  public boolean hasPermission(String permissionCode) {
    CurrentUser currentUser = currentUserProvider.requireCurrentUser();
    return currentUser.superAdmin()
        || currentUser.permissionCodes().contains(ALL_PERMISSION)
        || currentUser.permissionCodes().contains(permissionCode);
  }

  @Override
  public boolean hasAnyPermission(Collection<String> permissionCodes) {
    if (permissionCodes == null || permissionCodes.isEmpty()) {
      return true;
    }
    return permissionCodes.stream().anyMatch(this::hasPermission);
  }

  @Override
  public Set<String> listPermissionCodes() {
    return currentUserProvider.requireCurrentUser().permissionCodes();
  }
}
