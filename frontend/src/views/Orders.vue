<template>
  <div class="mobile-orders-page">
    <!-- 顶部导航栏 -->
    <header class="top-nav">
      <button class="back-btn" @click="goBack">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5">
          <polyline points="15,18 9,12 15,6"/>
        </svg>
      </button>
      <h1 class="nav-title">订单</h1>
      <div class="nav-placeholder"></div>
    </header>

    <!-- 火车票订单标题栏 -->
    <div class="section-header">
      <h2 class="section-title">火车票订单</h2>
      <a href="#" @click.prevent class="tips-link">温馨提示</a>
    </div>

    <!-- 订单分类标签 -->
    <div class="order-tabs">
      <div
        v-for="tab in orderTabs"
        :key="tab.key"
        class="order-tab-item"
        :class="{ active: activeTab === tab.key }"
        @click="switchTab(tab.key)"
      >
        <div class="tab-icon" v-html="tab.icon"></div>
        <span class="tab-label">{{ tab.label }}</span>
      </div>
    </div>

    <!-- 订单类型列表 -->
    <div class="order-types-list">
      <div
        v-for="item in orderTypes"
        :key="item.key"
        class="order-type-item"
        @click="handleOrderType(item.key)"
      >
        <div class="item-icon" v-html="item.icon"></div>
        <span class="item-label">{{ item.label }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const activeTab = ref('pending')

const orderTabs = [
  {
    key: 'pending',
    label: '待支付',
    icon: `<svg width="28" height="28" viewBox="0 0 24 24" fill="none">
      <rect x="3" y="4" width="18" height="16" rx="2" stroke="#1890ff" stroke-width="1.5"/>
      <line x1="3" y1="10" x2="21" y2="10" stroke="#1890ff" stroke-width="1.5"/>
      <circle cx="8" cy="6.5" r="0.5" fill="#1890ff"/>
      <circle cx="11" cy="6.5" r="0.5" fill="#1890ff"/>
    </svg>`
  },
  {
    key: 'paid',
    label: '已支付',
    icon: `<svg width="28" height="28" viewBox="0 0 24 24" fill="none">
      <path d="M9 11l3 3L22 4" stroke="#52c41a" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M21 12v7a2 2 0 01-2 2H5a2 2 0 01-2-2V5a2 2 0 012-2h11" stroke="#52c41a" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </svg>`
  },
  {
    key: 'waitlist',
    label: '候补订单',
    icon: `<svg width="28" height="28" viewBox="0 0 24 24" fill="none">
      <rect x="4" y="4" width="16" height="16" rx="2" stroke="#faad14" stroke-width="1.5"/>
      <path d="M9 12h6M12 9v6" stroke="#faad14" stroke-width="1.5" stroke-linecap="round"/>
    </svg>`
  },
  {
    key: 'mytickets',
    label: '本人车票',
    icon: `<svg width="28" height="28" viewBox="0 0 24 24" fill="none">
      <rect x="3" y="5" width="18" height="14" rx="2" stroke="#722ed1" stroke-width="1.5"/>
      <circle cx="8.5" cy="12" r="2" stroke="#722ed1" stroke-width="1.5"/>
      <path d="M13 10h5M13 13h3M13 16h4" stroke="#722ed1" stroke-width="1.5" stroke-linecap="round"/>
    </svg>`
  },
  {
    key: 'invoice',
    label: '电子发票',
    icon: `<svg width="28" height="28" viewBox="0 0 24 24" fill="none">
      <path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z" stroke="#13c2c2" stroke-width="1.5"/>
      <polyline points="14,2 14,8 20,8" stroke="#13c2c2" stroke-width="1.5"/>
      <line x1="16" y1="13" x2="8" y2="13" stroke="#13c2c2" stroke-width="1.5"/>
      <line x1="16" y1="17" x2="8" y2="17" stroke="#13c2c2" stroke-width="1.5"/>
      <polyline points="10,9 9,9 8,9" stroke="#13c2c2" stroke-width="1.5"/>
    </svg>`
  }
]

const orderTypes = [
  {
    key: 'food',
    label: '餐饮·特产订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <path d="M18 8h1a4 4 0 010 8h-1M2 8h16v9a4 4 0 01-4 4H6a4 4 0 01-4-4V8z" stroke="#52c41a" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <line x1="6" y1="1" x2="6" y2="4" stroke="#52c41a" stroke-width="1.5" stroke-linecap="round"/>
      <line x1="10" y1="1" x2="10" y2="4" stroke="#52c41a" stroke-width="1.5" stroke-linecap="round"/>
      <line x1="14" y1="1" x2="14" y2="4" stroke="#52c41a" stroke-width="1.5" stroke-linecap="round"/>
    </svg>`
  },
  {
    key: 'insurance',
    label: '保险订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" stroke="#fa8c16" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </svg>`
  },
  {
    key: 'hotel',
    label: '酒店订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <path d="M3 21V9m0 12h18M3 9l9-6 9 6M3 9v6m18-6v6M9 21v-6h6v6" stroke="#95de64" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </svg>`
  },
  {
    key: 'airrail',
    label: '空铁联运订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <path d="M21 16v-2l-8-5V3.5a1.5 1.5 0 00-3 0V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5l8 2.5z" stroke="#40a9ff" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </svg>`
  },
  {
    key: 'frequent',
    label: '计次·定期票订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <rect x="3" y="4" width="18" height="16" rx="2" stroke="#69c0ff" stroke-width="1.5"/>
      <line x1="3" y1="10" x2="21" y2="10" stroke="#69c0ff" stroke-width="1.5"/>
      <text x="7" y="16" font-size="8" fill="#69c0ff" font-weight="bold">N</text>
    </svg>`
  },
  {
    key: 'car',
    label: '租车·约车订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <path d="M5 17h14M5 17a2 2 0 01-2-2V9a2 2 0 012-2h14a2 2 0 012 2v6a2 2 0 01-2 2M5 17l1.5-6h11L19 17" stroke="#ffa940" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <circle cx="7.5" cy="17" r="1.5" stroke="#ffa940" stroke-width="1.5"/>
      <circle cx="16.5" cy="17" r="1.5" stroke="#ffa940" stroke-width="1.5"/>
    </svg>`
  },
  {
    key: 'business',
    label: '商务座服务订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <rect x="4" y="3" width="16" height="18" rx="2" stroke="#ffd666" stroke-width="1.5"/>
      <path d="M9 3v18M15 3v18" stroke="#ffd666" stroke-width="1.5"/>
      <circle cx="12" cy="12" r="2" stroke="#ffd666" stroke-width="1.5"/>
    </svg>`
  },
  {
    key: 'mall',
    label: '铁路商城订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <circle cx="9" cy="21" r="1" stroke="#597ef7" stroke-width="1.5"/>
      <circle cx="20" cy="21" r="1" stroke="#597ef7" stroke-width="1.5"/>
      <path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 002-1.61L23 6H6" stroke="#597ef7" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </svg>`
  },
  {
    key: 'bus',
    label: '汽车票订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <path d="M8 6v6M16 6v6M3 10h18M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" stroke="#73d13d" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <circle cx="7.5" cy="17.5" r="1.5" stroke="#73d13d" stroke-width="1.5"/>
      <circle cx="16.5" cy="17.5" r="1.5" stroke="#73d13d" stroke-width="1.5"/>
    </svg>`
  },
  {
    key: 'tourism',
    label: '门票·旅游订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <path d="M3 20.5V11l9-7 9 7v9.5a1.5 1.5 0 01-1.5 1.5H4.5A1.5 1.5 0 013 20.5z" stroke="#36cfc9" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <polyline points="10,20 10,14 14,14 14,20" stroke="#36cfc9" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </svg>`
  },
  {
    key: 'trainwater',
    label: '铁水联运订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <path d="M2 20h20M4 20V10l8-6 8 6v10M6 16h12M10 20v-4h4v4" stroke="#ff85c0" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M6 8c0-2 1-3 3-3s3 1 3 3M12 8c0-2 1-3 3-3s3 1 3 3" stroke="#ff85c0" stroke-width="1.5"/>
    </svg>`
  },
  {
    key: 'ship',
    label: '船票订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <path d="M2 20l4-16h12l4 16M6 20h12M12 4v16" stroke="#b37feb" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M8 12l4-2 4 2" stroke="#b37feb" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
    </svg>`
  },
  {
    key: 'pet',
    label: '宠物托运订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none">
      <rect x="3" y="6" width="18" height="14" rx="2" stroke="#91d5ff" stroke-width="1.5"/>
      <circle cx="8.5" cy="11" r="1.5" stroke="#91d5ff" stroke-width="1.5"/>
      <path d="M14 10c.5-.5 1.5-.5 2 0s.5 1.5 0 2-.5 1-1.5 1" stroke="#91d5ff" stroke-width="1.5" stroke-linecap="round"/>
      <path d="M8 15c1 1 2.5 1.5 4 1.5s3-.5 4-1.5" stroke="#91d5ff" stroke-width="1.5" stroke-linecap="round"/>
    </svg>`
  }
]

const goBack = () => {
  router.push('/user')
}

const switchTab = (key: string) => {
  activeTab.value = key
  const routeMap: Record<string, string> = {
    'pending': '/orders/pending',
    'paid': '/orders/paid',
    'waitlist': '/orders/waitlist',
    'mytickets': '/orders/mytickets'
  }
  if (routeMap[key]) {
    router.push(routeMap[key])
  }
}

const handleOrderType = (typeKey: string) => {
  ElMessage.info(`正在开发中：${orderTypes.find(t => t.key === typeKey)?.label}`)
}
</script>

<style lang="scss" scoped>
.mobile-orders-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.top-nav {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .back-btn {
    background: none;
    border: none;
    cursor: pointer;
    padding: 4px;
    display: flex;
    align-items: center;
    opacity: 0.95;

    &:active {
      opacity: 0.8;
    }
  }

  .nav-title {
    font-size: 17px;
    font-weight: 600;
    margin: 0;
    flex: 1;
    text-align: center;
  }

  .nav-placeholder {
    width: 32px;
  }
}

.section-header {
  background: white;
  padding: 14px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;

  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin: 0;
  }

  .tips-link {
    color: #1890ff;
    text-decoration: none;
    font-size: 14px;
    font-weight: 500;

    &:active {
      opacity: 0.7;
    }
  }
}

.order-tabs {
  background: white;
  padding: 16px 12px;
  display: flex;
  justify-content: space-around;
  border-bottom: 1px solid #f0f0f0;
}

.order-tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  padding: 8px 4px;
  transition: all 0.2s;
  opacity: 0.7;

  &:active {
    transform: scale(0.95);
  }

  &.active {
    opacity: 1;

    .tab-icon {
      :deep(svg) {
        [stroke] {
          filter: drop-shadow(0 0 3px currentColor);
        }
      }
    }

    .tab-label {
      color: #333;
      font-weight: 600;
    }
  }

  .tab-icon {
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .tab-label {
    font-size: 12px;
    color: #666;
    text-align: center;
    line-height: 1.2;
  }
}

.order-types-list {
  background: white;
  margin-top: 10px;
}

.order-type-item {
  display: flex;
  align-items: center;
  padding: 14px 16px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  transition: all 0.2s;

  &:last-child {
    border-bottom: none;
  }

  &:active {
    background: #f5f5f5;
  }

  .item-icon {
    width: 28px;
    height: 28px;
    flex-shrink: 0;
    margin-right: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .item-label {
    font-size: 15px;
    color: #333;
    font-weight: 500;
  }
}
</style>
