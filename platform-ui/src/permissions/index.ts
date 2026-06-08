interface PermissionRoute {
  meta?: {
    roles?: string[];
    permission?: string;
  };
  children?: PermissionRoute[];
  [key: string]: unknown;
}

export function hasPermission(roles: string[], route: PermissionRoute): boolean {
  if (route.meta?.roles) {
    return roles.some((role) => route.meta?.roles?.includes(role));
  }
  return true;
}

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

export function hasPermissionCode(permission: string, permissionCodes = getLocalPermissionCodes()): boolean {
  if (permissionCodes.length === 0) {
    return true;
  }
  return permissionCodes.includes(permission);
}
