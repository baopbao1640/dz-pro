interface PermissionRoute {
  meta?: {
    roles?: string[];
    permission?: string;
  };
  children?: PermissionRoute[];
  [key: string]: unknown;
}

/*
 * Boundary:
 * 前端权限工具只负责菜单和按钮的显示控制，不能替代后端 `@RequiresPermission`。
 */
export function hasPermission(roles: string[], route: PermissionRoute): boolean {
  const permissionCodes = getLocalPermissionCodes();
  if (route.meta?.permission) {
    return hasPermissionCode(route.meta.permission, permissionCodes);
  }
  if (route.meta?.roles) {
    return roles.some((role) => route.meta?.roles?.includes(role));
  }
  return true;
}

/*
 * Deferred:
 * 当前保留角色和 permission code 双轨过滤，后续动态路由完全后端化后应优先使用 permission code。
 */
export function filterAsyncRoutes(routes: PermissionRoute[], roles: string[]): PermissionRoute[] {
  const res: PermissionRoute[] = [];

  routes.forEach((route) => {
    const tmp = { ...route };
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles);
      }
      res.push(tmp);
    }
  });

  return res;
}

export function checkWhiteList(whiteList: string[], path: string): boolean {
  return whiteList.includes(path);
}

export function getLocalPermissionCodes(): string[] {
  const raw = localStorage.getItem('permissionCodes');
  if (!raw) {
    return [];
  }
  try {
    const parsed = JSON.parse(raw) as unknown;
    return Array.isArray(parsed) ? parsed.filter((item): item is string => typeof item === 'string') : [];
  } catch {
    return raw
      .split(',')
      .map((item) => item.trim())
      .filter(Boolean);
  }
}

/*
 * Risk:
 * `*:*:*` 只表示前端体验层面的超级管理员放行，真实接口仍必须由后端权限服务校验。
 */
export function hasPermissionCode(permission: string, permissionCodes = getLocalPermissionCodes()): boolean {
  if (!permission) {
    return true;
  }
  if (permissionCodes.includes('*:*:*')) {
    return true;
  }
  return permissionCodes.includes(permission);
}
