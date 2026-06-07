<script setup lang="ts">
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/store/user';

const router = useRouter();
const userStore = useUserStore();

onMounted(async () => {
  try {
    const url = new URL(window.location.href);
    const code = url.searchParams.get('code');
    const state = url.searchParams.get('state');
    const sessionState = url.searchParams.get('session_state');

    if (code) {
      const cbUrl = new URL('/login/oauth2/code/keycloak', window.location.origin);
      if (state) cbUrl.searchParams.set('code', code);
      if (state) cbUrl.searchParams.set('state', state);
      if (sessionState) cbUrl.searchParams.set('session_state', sessionState);
      window.location.replace(cbUrl.toString());
      return;
    }

    const user = await userStore.fetchUserInfo();
    if (user) {
      router.replace('/');
    } else {
      router.replace('/login');
    }
  } catch (e) {
    console.error('OIDC callback failed', e);
    router.replace('/login');
  }
});
</script>

<template>
  <div class="callback-container">
    <span>正在处理登录，请稍候...</span>
  </div>
</template>

<style scoped>
.callback-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
</style>
