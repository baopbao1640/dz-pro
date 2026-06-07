import { defineStore } from 'pinia'

interface UserState {
  token: string | null
  userInfo: Record<string, any> | null
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: null,
    userInfo: null,
  }),
  actions: {
    setToken(token: string) {
      this.token = token
      localStorage.setItem('token', token)
    },
    setUserInfo(user: Record<string, any>) {
      this.userInfo = user
    },
    async fetchUserInfo() {
      try {
        const res = await fetch('/api/user/info', {
          method: 'GET',
          credentials: 'include',
          headers: { Accept: 'application/json' },
        })
        if (!res.ok) {
          this.clearUser()
          return null
        }
        const data = (await res.json()) as Record<string, any>
        this.setUserInfo(data)
        return data
      } catch {
        this.clearUser()
        return null
      }
    },
    clearUser() {
      this.token = null
      this.userInfo = null
      localStorage.removeItem('token')
    },
  },
})
