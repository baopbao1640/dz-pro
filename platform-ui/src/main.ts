import { createApp } from 'vue';
import { createPinia } from 'pinia';
import Antd from 'ant-design-vue';
import App from './App.vue';
import router from './router';

try {
  console.log('[main.ts] starting');
  const app = createApp(App);
  app.use(createPinia());
  app.use(router);
  app.use(Antd);
  console.log('[main.ts] mounting');
  app.mount('#app');
  console.log('[main.ts] mounted OK');
} catch (err) {
  console.error('[main.ts] FATAL', err);
  const message = err instanceof Error ? (err.stack ?? err.message) : String(err);
  document.body.innerHTML = '<pre style="color:red;padding:24px">' + message + '</pre>';
}
