<template>
  <div class="login-container">
    <a-button type="primary" @click="login">Keycloak 登录</a-button>
  </div>
</template>

<script setup lang="ts">
import { getLoginUrl } from '@/config/oidc';

const login = () => {
  window.location.href = getLoginUrl();
};
</script>

<style scoped>
.login-container { display: flex; justify-content: center; align-items: center; height: 100vh; }
</style>
