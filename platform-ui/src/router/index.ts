import { createRouter, createWebHistory } from 'vue-router';
import type { RouteRecordRaw } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';
import Login from '@/views/Login.vue';
import OidcCallback from '@/views/OidcCallback.vue';
import Logout from '@/views/Logout.vue';

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

router.beforeEach((to, _from, next) => {
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
  return next();
});

export default router;
