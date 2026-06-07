import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'
import Login from '@/views/Login.vue'
import Home from '@/views/Home.vue'
import OidcCallback from '@/views/OidcCallback.vue'
import Logout from '@/views/Logout.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { title: '登录' },
  },
  {
    path: '/oauth2/callback',
    name: 'OidcCallback',
    component: OidcCallback,
    meta: { title: '登录中...' },
  },
  {
    path: '/logout',
    name: 'Logout',
    component: Logout,
    meta: { title: '退出登录' },
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
        meta: { title: '工作台', requiresAuth: true },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/logout') {
    return next()
  }
  if (to.path === '/oauth2/callback') {
    return next()
  }
  if (to.path === '/login') {
    return next()
  }
  if (!token) {
    return next('/login')
  }
  return next()
})

export default router
