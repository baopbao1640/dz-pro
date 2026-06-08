<script setup lang="ts">
import { onMounted } from 'vue';
import { useUserStore } from '@/store/user';
import Dashboard from './Dashboard.vue';

const userStore = useUserStore();

onMounted(async () => {
  const user = await userStore.fetchUserInfo();
  if (!user) {
    window.location.href = '/login';
  }
});
</script>

<template>
  <Dashboard />
</template>
