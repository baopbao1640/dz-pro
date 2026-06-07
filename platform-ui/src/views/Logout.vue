<script setup lang="ts">
import { onMounted } from 'vue';
import { useUserStore } from '@/store/user';

const userStore = useUserStore();

onMounted(() => {
  userStore.clearUser();

  // 直接访问 Keycloak 的 OIDC 注销端点，会清理 SSO 会话后再 redirect 回前端
  window.location.href =
    'http://localhost:8080/realms/platform/protocol/openid-connect/logout' +
    '?client_id=platform-client' +
    '&post_logout_redirect_uri=' +
    encodeURIComponent(window.location.origin + '/login');
});
</script>

<template>
  <div class="logout-container">
    <span>正在退出登录...</span>
  </div>
</template>

<style scoped>
.logout-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
</style>
