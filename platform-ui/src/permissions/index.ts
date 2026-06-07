interface PermissionRoute {
  meta?: {
    roles?: string[];
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

export function filterAsyncRoutes(
  routes: PermissionRoute[],
  roles: string[],
): PermissionRoute[] {
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
