const whiteList: string[] = ['/login'];

export function hasPermission(roles: string[], route: Record<string, unknown>): boolean {
  if (route.meta?.roles) {
    return roles.some((role) => (route.meta?.roles as string[])?.includes(role));
  }
  return true;
}

export function filterAsyncRoutes(
  routes: Record<string, unknown>[],
  roles: string[],
): Record<string, unknown>[] {
  const res: Record<string, unknown>[] = [];

  routes.forEach((route) => {
    const tmp = { ...route };
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children as Record<string, unknown>[], roles);
      }
      res.push(tmp);
    }
  });

  return res;
}

export function checkWhiteList(whiteList: string[], path: string): boolean {
  return whiteList.includes(path);
}
