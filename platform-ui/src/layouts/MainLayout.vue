<script setup lang="ts">
import { computed, h, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { Layout, Menu, Dropdown, Avatar, theme } from 'ant-design-vue';
import {
  DashboardOutlined,
  UserOutlined,
  LogoutOutlined,
} from '@ant-design/icons-vue';
import { useUserStore } from '@/store/user';

const router = useRouter();
const userStore = useUserStore();

const { token } = theme.useToken();
const colorBgContainer = computed(() => token.value.colorBgContainer);

const menuItems = [
  {
    key: 'dashboard',
    icon: () => h(DashboardOutlined),
    label: '工作台',
  },
];

const handleMenuClick = ({ key }: { key: PropertyKey }) => {
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
    icon: () => h(LogoutOutlined),
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

const handleUserMenuClick = ({ key }: { key: PropertyKey }) => {
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
        <Dropdown :menu="{ items: userMenuItems, onClick: handleUserMenuClick }">
          <span class="user-action">
            <Avatar :size="28">
              <template #icon><UserOutlined /></template>
            </Avatar>
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
          @select="handleMenuClick"
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
