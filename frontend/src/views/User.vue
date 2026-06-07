<template>
  <div class="mobile-user-page">
    <!-- 顶部蓝色背景区域 -->
    <div class="header-bg">
      <!-- 状态栏区域 -->
      <div class="status-bar">
        <span class="time">10:28</span>
        <div class="status-icons">
          <span class="icon-signal"></span>
          <span class="icon-wifi"></span>
          <span class="icon-battery">57</span>
        </div>
      </div>

      <!-- 用户信息区 -->
      <div class="user-profile">
        <div class="avatar-wrapper">
          <div class="avatar">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 12C14.21 12 16 10.21 16 8C16 5.79 14.21 4 12 4C9.79 4 8 5.79 8 8C8 10.21 9.79 12 12 12ZM12 14C9.33 14 4 15.34 4 18V20H20V18C20 15.34 14.67 14 12 14Z" fill="white"/>
            </svg>
          </div>
        </div>
        <div class="user-name">{{ user?.nickname || '徐明洋' }}</div>
        <div class="verify-tags">
          <span class="tag success">
            <span class="check-icon">✓</span>
            手机核验成功
          </span>
          <span class="tag success">
            <span class="check-icon">✓</span>
            已实名认证
          </span>
        </div>

        <!-- 右上角图标 -->
        <div class="header-actions">
          <button class="icon-btn" @click="showSettings">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
              <circle cx="12" cy="12" r="3"/>
              <path d="M19.4 15a1.65 1.65 0 00.33 1.82l.06.06a2 2 0 010 2.83 2 2 0 01-2.83 0l-.06-.06a1.65 1.65 0 00-1.82-.33 1.65 1.65 0 00-1 1.51V21a2 2 0 01-4 0v-.09A1.65 1.65 0 009 19.4a1.65 1.65 0 00-1.82.33l-.06.06a2 2 0 01-2.83 0 2 2 0 010-2.83l.06-.06A1.65 1.65 0 004.68 15a1.65 1.65 0 00-1.51-1H3a2 2 0 010-4h.09A1.65 1.65 0 004.6 9a1.65 1.65 0 00-.33-1.82l-.06-.06a2 2 0 010-2.83 2 2 0 012.83 0l.06.06A1.65 1.65 0 009 4.68a1.65 1.65 0 001-1.51V3a2 2 0 014 0v.09a1.65 1.65 0 001 1.51 1.65 1.65 0 001.82-.33l.06-.06a2 2 0 012.83 0 2 2 0 010 2.83l-.06.06A1.65 1.65 0 0019.4 9a1.65 1.65 0 001.51 1H21a2 2 0 010 4h-.09a1.65 1.65 0 00-1.51 1z"/>
            </svg>
          </button>
          <button class="icon-btn">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
              <path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z"/>
            </svg>
          </button>
        </div>
      </div>
    </div>

    <!-- 主内容区 - 可滚动 -->
    <div class="main-content">
      <!-- ===== 上半部分内容 ===== -->

      <!-- 功能入口卡片 -->
      <div class="feature-cards">
        <router-link to="/passengers-mobile" class="feature-card">
          <div class="card-icon blue">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="#1890ff">
              <path d="M20 21c-1.39 0-2.78-.47-4-1.32-2.44 1.71-5.56 1.71-8 0C6.78 20.53 5.39 21 4 21H2v2h2c1.38 0 2.74-.35 4-.99 2.52 1.29 5.48 1.29 8 0 1.26.65 2.62.99 4 .99h2v-2h-2zM3.95 19H4c1.6 0 3.02-.88 4-2 .98 1.12 2.4 2 4 2s3.02-.88 4-2c.98 1.12 2.4 2 4 2h.05l1.89-6.68c.08-.26.06-.54-.06-.78s-.34-.42-.6-.5L20 10.62V6c0-1.1-.9-2-2-2h-3V1H9v3H6c-1.1 0-2 .9-2 2v4.62l-1.29.42c-.26.08-.48.26-.6.5s-.15.52-.06.78L3.95 19zM6 6h12v3.97L12 8 6 9.97V6z"/>
            </svg>
          </div>
          <span class="card-label">乘车人</span>
        </router-link>

        <router-link to="/orders" class="feature-card">
          <div class="card-icon blue">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="#1890ff">
              <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
            </svg>
          </div>
          <span class="card-label">我的订单</span>
        </router-link>

        <div class="feature-card">
          <div class="card-icon blue">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="#1890ff">
              <path d="M20 4H4c-1.11 0-1.99.89-1.99 2L2 18c0 1.11.89 2 2 2h16c1.11 0 2-.89 2-2V6c0-1.11-.89-2-2-2zm0 14H4v-6h16v6zm0-10H4V6h16v2z"/>
            </svg>
          </div>
          <span class="card-label">优惠券</span>
        </div>
      </div>

      <!-- 学生优惠资质验证横幅 -->
      <div class="student-banner" @click="goToStudentVerify">
        <div class="banner-content">
          <div class="banner-text">
            <h3>学生优惠资质核验</h3>
            <p>维护学生优惠资质，购票享受优惠</p>
          </div>
          <div class="banner-action">
            <span class="action-btn">点击查看 ›</span>
          </div>
        </div>
        <div class="banner-illustration">
          <svg width="80" height="60" viewBox="0 0 100 75" fill="none">
            <ellipse cx="50" cy="70" rx="40" ry="5" fill="rgba(255,255,255,0.2)"/>
            <rect x="25" y="30" width="50" height="35" rx="3" fill="white" opacity="0.9"/>
            <rect x="30" y="35" width="40" height="3" rx="1.5" fill="#1890ff"/>
            <rect x="30" y="42" width="30" height="2" rx="1" fill="#ccc"/>
            <rect x="30" y="48" width="35" height="2" rx="1" fill="#ccc"/>
            <circle cx="72" cy="45" r="12" fill="#ffd666"/>
            <path d="M68 45 L71 48 L76 42" stroke="white" stroke-width="2" fill="none" stroke-linecap="round"/>
          </svg>
        </div>
      </div>

      <!-- 出行向导 -->
      <div class="section">
        <h3 class="section-title">出行向导</h3>
        <div class="grid-menu">
          <div class="grid-item" v-for="item in travelGuideItems" :key="item.label" @click="handleMenuClick(item)">
            <div class="menu-icon" v-html="item.icon"></div>
            <span class="menu-label">{{ item.label }}</span>
          </div>
        </div>
      </div>

      <!-- 常用功能 -->
      <div class="section">
        <h3 class="section-title">常用功能</h3>
        <div class="grid-menu">
          <div class="grid-item" v-for="item in commonFunctionItems" :key="item.label" @click="handleMenuClick(item)">
            <div class="menu-icon" v-html="item.icon"></div>
            <span class="menu-label">{{ item.label }}</span>
          </div>
        </div>
      </div>

      <!-- ===== 下半部分内容（往下滑显示） ===== -->

      <!-- 温馨服务 -->
      <div class="menu-section warm-service">
        <h3 class="section-title">温馨服务</h3>
        <div class="service-grid">
          <div class="service-item" v-for="item in warmServiceItems" :key="item.label" @click="handleMenuClick(item)">
            <div class="service-icon" v-html="item.icon"></div>
            <span class="service-label">{{ item.label }}</span>
          </div>
        </div>
      </div>

      <!-- 通知消息 -->
      <div class="menu-section list-menu">
        <div class="list-item" @click="handleMenuClick({ label: '通知消息', path: '/notifications' })">
          <span class="list-label">通知消息</span>
          <span class="arrow">›</span>
        </div>
      </div>

      <!-- 系统设置 -->
      <div class="menu-section list-menu">
        <div class="list-item" @click="handleMenuClick({ label: '系统设置', path: '/settings' })">
          <span class="list-label">系统设置</span>
          <span class="arrow">›</span>
        </div>
      </div>

      <!-- 信息服务 -->
      <div class="menu-section info-service">
        <h3 class="section-title">信息服务</h3>
        <div class="list-menu-group">
          <div class="list-item" v-for="item in infoServiceItems" :key="item.label" @click="handleMenuClick(item)">
            <span class="list-label">{{ item.label }}</span>
            <span class="arrow">›</span>
          </div>
        </div>
      </div>

      <!-- 铁路保险 -->
      <div class="menu-section list-menu">
        <div class="list-item" @click="handleMenuClick({ label: '铁路保险', path: '/insurance' })">
          <span class="list-label">铁路保险</span>
          <span class="arrow">›</span>
        </div>
      </div>
    </div>

    <!-- 底部导航栏 -->
    <nav class="bottom-nav">
      <router-link to="/" class="nav-item">
        <div class="nav-icon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
            <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"/>
          </svg>
        </div>
        <span class="nav-label">首页</span>
      </router-link>
      <div class="nav-item">
        <div class="nav-icon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
            <path d="M20 6h-4V4c0-1.11-.89-2-2-2h-4c-1.11 0-2 .89-2 2v2H4c-1.11 0-2 .89-2 2v11c0 1.11.89 2 2 2h16c1.11 0 2-.89 2-2V8c0-1.11-.89-2-2-2zm-8-2h4v2h-4V4zM4 8h16v11H4V8z"/>
          </svg>
        </div>
        <span class="nav-label">出行服务</span>
      </div>
      <router-link to="/orders" class="nav-item">
        <div class="nav-icon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
            <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
          </svg>
        </div>
        <span class="nav-label">订单</span>
      </router-link>
      <div class="nav-item">
        <div class="nav-icon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2L4 5v6.09c0 5.05 3.41 9.76 8 10.91 4.59-1.15 8-5.86 8-10.91V5l-8-3z"/>
          </svg>
        </div>
        <span class="nav-label">铁路会员</span>
      </div>
      <router-link to="/user" class="nav-item active">
        <div class="nav-icon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
          </svg>
        </div>
        <span class="nav-label">我的</span>
      </router-link>
    </nav>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { authApi } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const user = ref<any>(null)

onMounted(async () => {
  try {
    const res = await authApi.getUserInfo()
    user.value = res.data
  } catch (error) {
    console.error('加载用户信息失败')
  }
})

const showSettings = () => {
  router.push('/settings')
}

const goToStudentVerify = () => {
  router.push('/student-verify')
}

const handleMenuClick = (item: any) => {
  if (item.path) {
    router.push(item.path)
  } else {
    ElMessage.info(`${item.label}功能开发中`)
  }
}

// ===== 上半部分数据 =====

const travelGuideItems = [
  { label: '车站大屏', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><rect x="2" y="3" width="20" height="14" rx="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/></svg>', path: '/station-screen' },
  { label: '时刻表', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12,6 12,12 16,14"/></svg>', path: '/schedule' },
  { label: '起售时间', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>', path: '/sale-time' },
  { label: '正晚点查询', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12,6 12,12 16,14"/></svg>', path: '/delay-query' },
  { label: '票价查询', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 000 7h5a3.5 3.5 0 010 7H6"/></svg>', path: '/price-query' },
  { label: '换乘时间', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><path d="M17 1l4 4-4 4"/><path d="M3 11V9a4 4 0 014-4h14"/><path d="M7 23l-4-4 4-4"/><path d="M21 13v2a4 4 0 01-4 4H3"/></svg>', path: '/transfer' },
  { label: '代售点查询', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z"/><circle cx="12" cy="10" r="3"/></svg>', path: '/outlets' },
  { label: '更多', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><circle cx="12" cy="12" r="1"/><circle cx="19" cy="12" r="1"/><circle cx="5" cy="12" r="1"/></svg>', path: null }
]

const commonFunctionItems = [
  { label: '人证核验', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>', path: '/verify' },
  { label: '指纹登录', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><path d="M2 12C2 6.5 6.5 2 12 2a10 10 0 018 4"/><path d="M5 19.5C5 18.1 6.6 17 8.5 17c.7 0 1.4.1 2 .3"/><path d="M12 12v6"/></svg>', path: '/fingerprint' },
  { label: '修改密码', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2"/><path d="M7 11V7a5 5 0 0110 0v4"/></svg>', path: '/change-password' },
  { label: '修改手机号', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><rect x="5" y="2" width="14" height="20" rx="2"/><line x1="12" y1="18" x2="12" y2="18"/></svg>', path: '/change-phone' },
  { label: '通知设置', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 01-3.46 0"/></svg>', path: '/notifications' },
  { label: '敬老版', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><path d="M20.84 4.61a5.5 5.5 0 00-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 00-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 000-7.78z"/></svg>', path: '/elderly' },
  { label: '快捷退票', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 002-1.61L23 6H6"/></svg>', path: '/refund' },
  { label: '学生资质核验', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><path d="M22 10v6M2 10l10-5 10 5-10 5z"/><path d="M6 12v5c3 3 9 3 12 0v-5"/></svg>', path: '/student-verify' },
  { label: '电子发票', icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/><polyline points="14,2 14,8 20,8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10,9 9,9 8,9"/></svg>', path: '/e-invoice' }
]

// ===== 下半部分数据 =====

const warmServiceItems = [
  {
    label: '临时身份证明',
    icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><rect x="3" y="4" width="18" height="16" rx="2"/><circle cx="9" cy="10" r="2"/><path d="M15 8h2"/><path d="M15 12h2"/></svg>',
    path: '/temp-id'
  },
  {
    label: '遗失物品',
    icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>',
    path: '/lost-items'
  },
  {
    label: '建议',
    icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z"/></svg>',
    path: '/suggestions'
  },
  {
    label: '投诉',
    icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 01-3.46 0"/></svg>',
    path: '/complaints'
  },
  {
    label: '重点旅客',
    icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><circle cx="12" cy="12" r="3"/><path d="M12 1v6m0 6v6m5.196-11.196l-4.242 4.242m0 5.656l4.242 4.242M23 12h-6m-6 0H5m11.196 5.196l-4.242-4.242m0-5.656L5.804 2.804"/></svg>',
    path: '/key-passenger'
  },
  {
    label: '客服电话',
    icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><path d="M22 16.92v3a2 2 0 01-2.18 2 19.79 19.79 0 01-8.63-3.07 19.5 19.5 0 01-6-6 19.79 19.79 0 01-3.07-8.67A2 2 0 014.11 2h3a2 2 0 012 1.72 12.84 12.84 0 00.7 2.81 2 2 0 01-.45 2.11L8.09 9.91a16 16 0 006 6l1.27-1.27a2 2 0 012.11-.45 12.84 12.84 0 002.81.7A2 2 0 0122 16.92z"/></svg>',
    path: '/customer-service'
  },
  {
    label: '外国商务人...',
    icon: '<svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 7V5a2 2 0 00-2-2h-4a2 2 0 00-2 2v2"/></svg>',
    path: '/foreign-business'
  }
]

const infoServiceItems = [
  { label: '公告', path: '/announcements' },
  { label: '常见问题', path: '/faq' },
  { label: '使用须知', path: '/usage-guide' },
  { label: '服务规章', path: '/service-rules' }
]
</script>

<style lang="scss" scoped>
.mobile-user-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 60px;
}

.header-bg {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: white;
  position: relative;
  padding-bottom: 30px;
}

.status-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 16px;
  font-size: 14px;
  font-weight: 600;

  .time {
    font-size: 15px;
  }

  .status-icons {
    display: flex;
    gap: 6px;
    align-items: center;

    .icon-battery::before {
      content: '';
    }
  }
}

.user-profile {
  position: relative;
  padding: 20px 16px 0;
}

.avatar-wrapper {
  margin-bottom: 12px;
}

.avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.25);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px solid rgba(255, 255, 255, 0.4);

  svg {
    width: 36px;
    height: 36px;
  }
}

.user-name {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 10px;
}

.verify-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;

  .tag {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    padding: 4px 10px;
    border-radius: 12px;
    font-size: 12px;
    background: rgba(255, 255, 255, 0.95);
    color: #333;

    &.success {
      .check-icon {
        color: #52c41a;
        font-weight: bold;
      }
    }
  }
}

.header-actions {
  position: absolute;
  top: -35px;
  right: 16px;
  display: flex;
  gap: 12px;

  .icon-btn {
    background: none;
    border: none;
    cursor: pointer;
    opacity: 0.9;
    transition: opacity 0.2s;

    &:hover {
      opacity: 1;
    }
  }
}

.main-content {
  margin-top: -15px;
  background: #f5f5f5;
  border-radius: 16px 16px 0 0;
  position: relative;
  z-index: 1;
  padding: 16px;
}

/* ===== 上半部分样式 ===== */

.feature-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 16px;
}

.feature-card {
  background: white;
  border-radius: 8px;
  padding: 16px 8px;
  text-align: center;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  text-decoration: none;
  transition: transform 0.2s, box-shadow 0.2s;

  &:active {
    transform: scale(0.98);
  }

  .card-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 8px;
    background: #e6f7ff;

    svg {
      width: 28px;
      height: 28px;
    }
  }

  .card-label {
    font-size: 13px;
    color: #333;
    font-weight: 500;
  }
}

.student-banner {
  background: linear-gradient(135deg, #69b1ff 0%, #1890ff 100%);
  border-radius: 10px;
  padding: 16px;
  margin-bottom: 20px;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.25);

  .banner-content {
    position: relative;
    z-index: 2;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .banner-text {
    h3 {
      color: white;
      font-size: 16px;
      font-weight: 600;
      margin: 0 0 6px;
    }

    p {
      color: rgba(255, 255, 255, 0.95);
      font-size: 13px;
      margin: 0;
    }
  }

  .action-btn {
    background: white;
    color: #1890ff;
    padding: 6px 14px;
    border-radius: 14px;
    font-size: 12px;
    font-weight: 600;
    display: inline-block;
  }

  .banner-illustration {
    position: absolute;
    right: 10px;
    bottom: 5px;
    z-index: 1;
    opacity: 0.9;
  }
}

.section {
  background: white;
  border-radius: 10px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);

  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin: 0 0 16px;
  }
}

.grid-menu {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px 8px;
}

.grid-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  transition: transform 0.2s;

  &:active {
    transform: scale(0.95);
  }

  .menu-icon {
    width: 44px;
    height: 44px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #fafafa;
    border-radius: 10px;

    :deep(svg) {
      width: 24px;
      height: 24px;
    }
  }

  .menu-label {
    font-size: 12px;
    color: #666;
    text-align: center;
    line-height: 1.3;
  }
}

/* ===== 下半部分样式 ===== */

.menu-section {
  margin-bottom: 12px;
  background: white;

  &.single-item {
    .menu-item {
      display: flex;
      align-items: center;
      padding: 16px;
      cursor: pointer;
      transition: background 0.2s;

      &:active {
        background: #f5f5f5;
      }

      .item-icon-wrapper {
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12px;
      }

      .item-label {
        font-size: 15px;
        color: #333;
        font-weight: 500;
      }
    }
  }

  &.warm-service {
    padding: 16px 12px 12px;

    .section-title {
      font-size: 15px;
      font-weight: 600;
      color: #333;
      margin: 0 0 16px 4px;
    }

    .service-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 16px 8px;
    }

    .service-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 6px;
      cursor: pointer;
      transition: transform 0.2s;

      &:active {
        transform: scale(0.95);
      }

      .service-icon {
        width: 44px;
        height: 44px;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #fafafa;
        border-radius: 10px;

        :deep(svg) {
          width: 24px;
          height: 24px;
        }
      }

      .service-label {
        font-size: 12px;
        color: #666;
        text-align: center;
        line-height: 1.3;
      }
    }
  }

  &.list-menu {
    .list-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px;
      cursor: pointer;
      transition: background 0.2s;
      border-bottom: 1px solid #f5f5f5;

      &:last-child {
        border-bottom: none;
      }

      &:active {
        background: #f5f5f5;
      }

      .list-label {
        font-size: 15px;
        color: #333;
        font-weight: 500;
      }

      .arrow {
        font-size: 20px;
        color: #ccc;
        font-weight: 300;
      }
    }
  }

  &.info-service {
    padding: 16px 0;

    .section-title {
      font-size: 15px;
      font-weight: 600;
      color: #333;
      margin: 0 0 4px;
      padding: 0 16px 12px;
    }

    .list-menu-group {
      .list-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 16px;
        cursor: pointer;
        transition: background 0.2s;
        border-bottom: 1px solid #f5f5f5;

        &:last-child {
          border-bottom: none;
        }

        &:active {
          background: #f5f5f5;
        }

        .list-label {
          font-size: 15px;
          color: #333;
          font-weight: 500;
        }

        .arrow {
          font-size: 20px;
          color: #ccc;
          font-weight: 300;
        }
      }
    }
  }
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: white;
  display: flex;
  justify-content: space-around;
  padding: 6px 0 calc(6px + env(safe-area-inset-bottom));
  border-top: 1px solid #e8e8e8;
  z-index: 1000;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.04);
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  text-decoration: none;
  color: #999;
  font-size: 10px;
  padding: 4px 12px;
  transition: color 0.2s;

  &.active {
    color: #1890ff;

    .nav-icon {
      color: #1890ff;
    }
  }

  .nav-icon {
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .nav-label {
    font-size: 10px;
    font-weight: 500;
  }
}
</style>
