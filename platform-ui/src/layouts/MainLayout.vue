<script setup lang="ts">
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { Layout, Menu, Button, Dropdown, Avatar, theme } from 'ant-design-vue';
import {
  DashboardOutlined,
  UserOutlined,
  LogoutOutlined,
} from '@ant-design/icons-vue';
import { useUserStore } from '@/store/user';

const router = useRouter();
const userStore = useUserStore();

const {
  token: { colorBgContainer },
} = theme.useToken();

const menuItems = [
  {
    key: 'dashboard',
    icon: () => DashboardOutlined(),
    label: '工作台',
  },
];

const handleMenuClick = ({ key }: { key: string }) => {
  router.push({ name: String(key) });
};

const handleLogout = () => {
  userStore.clearUser();
  router.replace({ name: 'Login' });
};

const userMenuItems = [
  { key: 'profile', label: '个人中心' },
  {
    key: 'logout',
    icon: () => LogoutOutlined(),
    label: '退出登录',
  },
];

onMounted(() => {
  if (!userStore.token) {
    const t = localStorage.getItem('token');
    if (t) {
      userStore.setToken(t);
    }
  }
});

const handleUserMenuClick = ({ key }: { key: string }) => {
  if (key === 'logout') {
    handleLogout();
  }
};
</script>

<template>
  <Layout style="min-height: 100vh">
    <Layout.Header class="layout-header">
      <div class="header-left">平台管理系统</div>
      <div class="header-right">
        <Dropdown :menu="{ items: userMenuItems }" @select="handleUserMenuClick">
          <span class="user-action">
            <Avatar :size="28" icon="UserOutlined" />
            <span class="username">{{ userStore.userInfo?.name || '用户' }}</span>
          </span>
        </Dropdown>
      </div>
    </Layout.Header>
    <Layout>
      <Layout.Sider
        width="220"
        :style="{ background: colorBgContainer, overflow: 'auto', height: 'calc(100vh - 64px)' }"
      >
        <Menu
          mode="inline"
          :selected-keys="[router.currentRoute.value.name as string]"
          :style="{ height: '100%', borderRight: 0 }"
          :items="menuItems"
          @select="({ key }) => handleMenuClick(key as string)"
        />
      </Layout.Sider>
      <Layout.Content
        :style="{
          margin: '24px 16px',
          padding: 24,
          minHeight: 280,
          background: colorBgContainer,
          borderRadius: 6,
        }"
      >
        <router-view />
      </Layout.Content>
    </Layout>
  </Layout>
</template>

<style scoped>
.layout-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-right: 24px;
}

.header-left {
  font-size: 18px;
  font-weight: 600;
  color: #ffffff;
}

.header-right {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.user-action {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #ffffff;
}

.username {
  font-size: 14px;
}
</style>
