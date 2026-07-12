import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 添加token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      if (res.code === 401) {
        // token过期，跳转到登录
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        router.push('/login')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  (error) => {
    if (error.response) {
      const status = error.response.status
      if (status === 401 || status === 403) {
        // token过期或未登录，清除本地状态并跳转到登录页
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        ElMessage.warning('登录已过期，请重新登录')
        router.push('/login')
        return Promise.reject(error)
      }
      ElMessage.error(error.response.data?.message || `请求失败 (${status})`)
    } else {
      ElMessage.error('网络错误，请检查网络连接')
    }
    return Promise.reject(error)
  }
)

// 封装请求方法
export const request = {
  get<T>(url: string, params?: object): Promise<T> {
    return service.get(url, { params })
  },
  post<T>(url: string, data?: object): Promise<T> {
    return service.post(url, data)
  },
  put<T>(url: string, data?: object): Promise<T> {
    return service.put(url, data)
  },
  delete<T>(url: string): Promise<T> {
    return service.delete(url)
  }
}

export default service