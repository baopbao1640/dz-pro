import { defineStore } from 'pinia';
import { getAuthPermissions, getAuthProfile, getAuthRoutes, type AuthProfile, type DynamicRoute } from '@/api/system/auth';

interface UserState {
  token: string | null;
  userInfo: AuthProfile | Record<string, unknown> | null;
  permissionCodes: string[];
  dynamicRoutes: DynamicRoute[];
}

/*
 * Boundary:
 * 用户 store 只缓存前端会话展示、动态路由和按钮权限状态，不保存 refresh token、密码或 Keycloak session。
 */
/*
 * Deferred:
 * 动态 routes 当前只保存后端结果，尚未统一转换为 Vue Router 可加载组件；后续需要组件白名单和 403 页面体验治理。
 */
export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: null,
    userInfo: null,
    permissionCodes: [],
    dynamicRoutes: [],
  }),
  actions: {
    setToken(token: string) {
      this.token = token;
      localStorage.setItem('token', token);
    },
    setUserInfo(user: Record<string, unknown>) {
      this.userInfo = user;
    },
    setPermissions(permissionCodes: string[]) {
      this.permissionCodes = permissionCodes;
      localStorage.setItem('permissionCodes', JSON.stringify(permissionCodes));
    },
    setDynamicRoutes(routes: DynamicRoute[]) {
      this.dynamicRoutes = routes;
    },
    async fetchUserInfo() {
      /*
       * Risk:
       * profile、permissions、routes 任一加载失败都会清理本地用户状态，避免前端保留过期权限继续展示受保护入口。
       */
      try {
        const [profile, permissions, routes] = await Promise.all([
          getAuthProfile(),
          getAuthPermissions(),
          getAuthRoutes(),
        ]);
        this.userInfo = profile;
        this.setPermissions(permissions);
        this.setDynamicRoutes(routes);
        return profile;
      } catch {
        this.clearUser();
        return null;
      }
    },
    clearUser() {
      this.token = null;
      this.userInfo = null;
      this.permissionCodes = [];
      this.dynamicRoutes = [];
      localStorage.removeItem('token');
      localStorage.removeItem('permissionCodes');
    },
  },
});
