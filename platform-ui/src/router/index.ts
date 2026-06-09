import { createRouter, createWebHistory } from 'vue-router';
import type { RouteRecordRaw } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';
import Login from '@/views/Login.vue';
import OidcCallback from '@/views/OidcCallback.vue';
import Logout from '@/views/Logout.vue';
import { hasPermissionCode } from '@/permissions';
import { useUserStore } from '@/store/user';

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { title: 'Login' },
  },
  {
    path: '/oauth2/callback',
    name: 'OidcCallback',
    component: OidcCallback,
    meta: { title: 'Login callback' },
  },
  {
    path: '/logout',
    name: 'Logout',
    component: Logout,
    meta: { title: 'Logout' },
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: 'Dashboard', requiresAuth: true },
      },
      {
        path: 'system/users',
        name: 'SystemUsers',
        component: () => import('@/views/system/UserManagement.vue'),
        meta: { title: 'User management', requiresAuth: true, permission: 'system:user:list' },
      },
      {
        path: 'system/depts',
        name: 'SystemDepts',
        component: () => import('@/views/system/DeptManagement.vue'),
        meta: { title: 'Department management', requiresAuth: true, permission: 'system:dept:list' },
      },
      {
        path: 'system/posts',
        name: 'SystemPosts',
        component: () => import('@/views/system/PostManagement.vue'),
        meta: { title: 'Post management', requiresAuth: true, permission: 'system:post:list' },
      },
      {
        path: 'system/roles',
        name: 'SystemRoles',
        component: () => import('@/views/system/RoleManagement.vue'),
        meta: { title: 'Role management', requiresAuth: true, permission: 'system:role:list' },
      },
      {
        path: 'system/menus',
        name: 'SystemMenus',
        component: () => import('@/views/system/MenuManagement.vue'),
        meta: { title: 'Menu management', requiresAuth: true, permission: 'system:menu:list' },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

/*
 * Boundary:
 * 当前路由表保留后台管理基础页面入口；真正的按钮权限和系统接口授权仍以后端 permission code enforce 为准。
 */
/*
 * Deferred:
 * 后端 `/api/system/auth/routes` 已返回动态路由契约，但当前前端仍以本地静态路由为主，后续需要接入组件白名单后再启用全量动态注入。
 */
/*
 * Risk:
 * 前端路由守卫只能控制用户体验，不能作为安全边界；任何受保护接口都必须继续由后端返回 401/403。
 */
router.beforeEach(async (to, _from, next) => {
  const token = localStorage.getItem('token');
  if (to.path === '/logout') {
    return next();
  }
  if (to.path === '/oauth2/callback') {
    return next();
  }
  if (to.path === '/login') {
    return next();
  }
  if (!token) {
    return next('/login');
  }
  const userStore = useUserStore();
  if (userStore.permissionCodes.length === 0) {
    const profile = await userStore.fetchUserInfo();
    if (!profile) {
      return next('/login');
    }
  }
  const permission = to.meta.permission;
  if (typeof permission === 'string' && !hasPermissionCode(permission, userStore.permissionCodes)) {
    return next('/dashboard');
  }
  return next();
});

export default router;
