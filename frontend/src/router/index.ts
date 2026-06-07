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
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册' }
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
    meta: { title: '旅客管理' }
  },
  {
    path: '/passengers-normal',
    name: 'PassengersNormal',
    component: () => import('@/views/Passengers-Normal.vue'),
    meta: { title: '旅客管理 - 普通版' }
  },
  {
    path: '/passengers-designer',
    name: 'PassengersDesigner',
    component: () => import('@/views/Passengers-Designer.vue'),
    meta: { title: '旅客管理 - 设计师版' }
  },
  {
    path: '/passengers-mobile',
    name: 'PassengersMobile',
    component: () => import('@/views/Passengers-Mobile.vue'),
    meta: { title: '乘车人' }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('@/views/Orders.vue'),
    meta: { title: '订单管理' }
  },
  {
    path: '/orders/pending',
    name: 'OrderPending',
    component: () => import('@/views/OrderPending.vue'),
    meta: { title: '待支付订单' }
  },
  {
    path: '/orders/paid',
    name: 'OrderPaid',
    component: () => import('@/views/OrderPaid.vue'),
    meta: { title: '已支付订单' }
  },
  {
    path: '/orders/waitlist',
    name: 'OrderWaitlist',
    component: () => import('@/views/OrderWaitlist.vue'),
    meta: { title: '候补订单' }
  },
  {
    path: '/orders/mytickets',
    name: 'OrderMyTickets',
    component: () => import('@/views/OrderMyTickets.vue'),
    meta: { title: '本人车票' }
  },
  {
    path: '/order/:id',
    name: 'OrderDetail',
    component: () => import('@/views/OrderDetail.vue'),
    meta: { title: '订单详情' }
  },
  {
    path: '/user',
    name: 'User',
    component: () => import('@/views/User.vue'),
    meta: { title: '个人中心' }
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