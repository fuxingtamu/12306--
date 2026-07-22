import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/ticket',
    name: 'TicketQuery',
    component: () => import('@/views/TicketQuery.vue'),
    meta: { title: '车票查询' }
  },
  {
    path: '/passengers',
    name: 'Passengers',
    component: () => import('@/views/Passengers.vue'),
    meta: { title: '乘车人管理' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 12306旅客管理系统` : '12306旅客管理系统'

  // TODO: 暂时禁用登录检查，等后端开发完成后再开启
  // const token = localStorage.getItem('token')
  // if (to.meta.requiresAuth && !token) {
  //   next('/login')
  // } else {
  //   next()
  // }
  next()
})

export default router