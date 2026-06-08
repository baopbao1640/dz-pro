<script setup lang="ts">
import { computed, h, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { Avatar, Dropdown, Layout, Menu, theme } from 'ant-design-vue';
import type { MenuProps } from 'ant-design-vue';
import {
  ApartmentOutlined,
  DashboardOutlined,
  IdcardOutlined,
  LogoutOutlined,
  MenuOutlined,
  SafetyCertificateOutlined,
  SettingOutlined,
  TeamOutlined,
  UserOutlined,
} from '@ant-design/icons-vue';
import { hasPermissionCode } from '@/permissions';
import { useUserStore } from '@/store/user';

const router = useRouter();
const userStore = useUserStore();

const { token } = theme.useToken();
const colorBgContainer = computed(() => token.value.colorBgContainer);

const systemChildren = computed<MenuProps['items']>(() =>
  [
    {
      key: 'SystemUsers',
      icon: () => h(TeamOutlined),
      label: 'Users',
      permission: 'system:user:list',
    },
    {
      key: 'SystemDepts',
      icon: () => h(ApartmentOutlined),
      label: 'Departments',
      permission: 'system:dept:list',
    },
    {
      key: 'SystemPosts',
      icon: () => h(IdcardOutlined),
      label: 'Posts',
      permission: 'system:post:list',
    },
    {
      key: 'SystemRoles',
      icon: () => h(SafetyCertificateOutlined),
      label: 'Roles',
      permission: 'system:role:list',
    },
    {
      key: 'SystemMenus',
      icon: () => h(MenuOutlined),
      label: 'Menus',
      permission: 'system:menu:list',
    },
  ].filter((item) => hasPermissionCode(item.permission)),
);

const menuItems = computed<MenuProps['items']>(() => [
  {
    key: 'Dashboard',
    icon: () => h(DashboardOutlined),
    label: 'Dashboard',
  },
  {
    key: 'SystemManagement',
    icon: () => h(SettingOutlined),
    label: 'System',
    children: systemChildren.value,
  },
]);

const selectedKeys = computed(() => [String(router.currentRoute.value.name || 'Dashboard')]);

const handleMenuClick = ({ key }: { key: PropertyKey }) => {
  if (key === 'SystemManagement') {
    return;
  }
  void router.push({ name: String(key) });
};

const handleLogout = () => {
  userStore.clearUser();
  void router.replace({ name: 'Login' });
};

const userMenuItems = [
  { key: 'profile', label: 'Profile' },
  {
    key: 'logout',
    icon: () => h(LogoutOutlined),
    label: 'Logout',
  },
];

onMounted(() => {
  if (!userStore.token) {
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
      userStore.setToken(storedToken);
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
      <div class="header-left">Platform Admin</div>
      <div class="header-right">
        <Dropdown :menu="{ items: userMenuItems, onClick: handleUserMenuClick }">
          <span class="user-action">
            <Avatar :size="28">
              <template #icon><UserOutlined /></template>
            </Avatar>
            <span class="username">{{ userStore.userInfo?.name || 'User' }}</span>
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
          :selected-keys="selectedKeys"
          :default-open-keys="['SystemManagement']"
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
