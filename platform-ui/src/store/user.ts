import { defineStore } from 'pinia';

interface UserState {
  token: string | null;
  userInfo: Record<string, unknown> | null;
}

interface ApiResult<T> {
  code: number;
  message: string;
  data: T;
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: null,
    userInfo: null,
  }),
  actions: {
    setToken(token: string) {
      this.token = token;
      localStorage.setItem('token', token);
    },
    setUserInfo(user: Record<string, unknown>) {
      this.userInfo = user;
    },
    async fetchUserInfo() {
      try {
        const res = await fetch('/api/user/info', {
          method: 'GET',
          credentials: 'include',
          headers: { Accept: 'application/json' },
        });
        if (!res.ok) {
          this.clearUser();
          return null;
        }
        const result = (await res.json()) as ApiResult<Record<string, unknown>>;
        this.setUserInfo(result.data);
        return result.data;
      } catch {
        this.clearUser();
        return null;
      }
    },
    clearUser() {
      this.token = null;
      this.userInfo = null;
      localStorage.removeItem('token');
    },
  },
});
