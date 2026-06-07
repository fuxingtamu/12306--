import { defineStore } from 'pinia'
import { ref } from 'vue'
import { authApi } from '@/api'

interface UserInfo {
  userId: number
  phone: string
  nickname: string
  avatar?: string
  isVerified: boolean
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const userInfo = ref<UserInfo | null>(null)

  // 设置token
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  // 设置用户信息
  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  // 获取用户信息
  const getUserInfo = async () => {
    if (!token.value) return null
    try {
      const res = await authApi.getUserInfo()
      userInfo.value = res.data
      return res.data
    } catch (error) {
      return null
    }
  }

  // 登出
  const logout = () => {
    token.value = null
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  // 检查是否已登录
  const isLoggedIn = () => {
    return !!token.value
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    getUserInfo,
    logout,
    isLoggedIn
  }
})