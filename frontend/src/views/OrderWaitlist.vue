<template>
  <div class="mobile-order-list-page">
    <!-- 顶部导航栏 -->
    <header class="top-nav">
      <button class="back-btn" @click="goBack">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5">
          <polyline points="15,18 9,12 15,6"/>
        </svg>
      </button>
      <h1 class="nav-title">候补订单</h1>
      <div class="nav-placeholder"></div>
    </header>

    <!-- 候补说明 -->
    <div class="tips-banner">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#faad14" stroke-width="2">
        <circle cx="12" cy="12" r="10"/>
        <path d="M9.09 9a3 3 0 015.83 1c0 2-3 3-3 3"/>
        <line x1="12" y1="17" x2="12.01" y2="17"/>
      </svg>
      <span>候补订单将在有余票时自动兑现，请耐心等待</span>
    </div>

    <!-- 订单列表 -->
    <div class="order-list">
      <div
        v-for="order in waitlistOrders"
        :key="order.orderId"
        class="order-card"
        @click="viewOrderDetail(order)"
      >
        <!-- 订单头部 -->
        <div class="order-header">
          <div class="train-info">
            <span class="train-number">{{ order.trainNumber }}</span>
            <span class="travel-date">{{ order.travelDate }}</span>
          </div>
          <span class="status-badge waitlist">候补中</span>
        </div>

        <!-- 车站信息 -->
        <div class="station-info">
          <div class="station-item start">
            <span class="time">{{ order.startTime }}</span>
            <span class="station-name">{{ order.fromStation }}</span>
          </div>
          <div class="duration-info">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#faad14" stroke-width="2">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
            <span class="duration">{{ order.duration }}</span>
          </div>
          <div class="station-item end">
            <span class="time">{{ order.endTime }}</span>
            <span class="station-name">{{ order.toStation }}</span>
          </div>
        </div>

        <!-- 候补详情 -->
        <div class="waitlist-detail">
          <div class="detail-row">
            <span class="label">候补席别：</span>
            <span class="value">{{ order.seatType }}</span>
          </div>
          <div class="detail-row">
            <span class="label">乘车人：</span>
            <span class="value">{{ order.passengers.join('、') }}</span>
          </div>
          <div class="detail-row">
            <span class="label">候补人数：</span>
            <span class="value highlight">{{ order.waitlistCount }} 人</span>
          </div>
          <div class="detail-row">
            <span class="label">当前排队：</span>
            <span class="value highlight">第 {{ order.queuePosition }} 位</span>
          </div>
        </div>

        <!-- 价格和操作 -->
        <div class="price-action">
          <span class="price">¥{{ order.totalPrice }}</span>
          <button class="btn btn-cancel-waitlist" @click.stop="cancelWaitlist(order)">取消候补</button>
        </div>

        <!-- 候补时间提示 -->
        <div class="waitlist-time-tip">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#faad14" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <polyline points="12,6 12,12 16,14"/>
          </svg>
          提交时间：{{ order.submitTime }} | 预计等待：{{ order.estimatedWait }}
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="waitlistOrders.length === 0" class="empty-state">
        <div class="empty-icon">
          <svg width="80" height="80" viewBox="0 0 120 100" fill="none">
            <rect x="20" y="30" width="80" height="50" rx="4" fill="#fffbe6" stroke="#faad14" stroke-width="2"/>
            <path d="M50 45h20M60 55v-10" stroke="#faad14" stroke-width="2" stroke-linecap="round"/>
            <line x1="35" y1="65" x2="85" y2="65" stroke="#faad14" stroke-width="2"/>
          </svg>
        </div>
        <p class="empty-text">暂无候补订单</p>
        <p class="empty-desc">热门车次可使用候补功能提高购票成功率</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

interface WaitlistOrder {
  orderId: number
  trainNumber: string
  travelDate: string
  fromStation: string
  toStation: string
  startTime: string
  endTime: string
  duration: string
  passengers: string[]
  seatType: string
  totalPrice: number
  waitlistCount: number
  queuePosition: number
  submitTime: string
  estimatedWait: string
}

const waitlistOrders = ref<WaitlistOrder[]>([
  {
    orderId: 20240116001,
    trainNumber: 'G1',
    travelDate: '2024-02-10',
    fromStation: '北京南',
    toStation: '上海虹桥',
    startTime: '09:00',
    endTime: '13:28',
    duration: '4小时28分',
    passengers: ['张三', '李四'],
    seatType: '二等座',
    totalPrice: 1310,
    waitlistCount: 2,
    queuePosition: 15,
    submitTime: '2024-01-16 08:30:00',
    estimatedWait: '约2-3天'
  },
  {
    orderId: 20240116002,
    trainNumber: 'G99',
    travelDate: '2024-02-11',
    fromStation: '广州南',
    toStation: '北京西',
    startTime: '07:00',
    endTime: '15:38',
    duration: '8小时38分',
    passengers: ['王五'],
    seatType: '一等座',
    totalPrice: 1878,
    waitlistCount: 1,
    queuePosition: 8,
    submitTime: '2024-01-16 10:15:22',
    estimatedWait: '约1-2天'
  },
  {
    orderId: 20240116003,
    trainNumber: 'D888',
    travelDate: '2024-02-12',
    fromStation: '成都东',
    toStation: '重庆北',
    startTime: '19:00',
    endTime: '21:30',
    duration: '2小时30分',
    passengers: ['赵六'],
    seatType: '二等座',
    totalPrice: 146,
    waitlistCount: 1,
    queuePosition: 23,
    submitTime: '2024-01-16 14:42:55',
    estimatedWait: '约3-5天'
  }
])

const goBack = () => {
  router.back()
}

const viewOrderDetail = (order: WaitlistOrder) => {
  router.push(`/order/${order.orderId}`)
}

const cancelWaitlist = async (order: WaitlistOrder) => {
  try {
    await ElMessageBox.confirm(
      `确认取消候补？取消后需要重新提交`,
      '取消确认',
      { type: 'warning', confirmButtonText: '确认取消' }
    )
    ElMessage.success('候补已取消')
    waitlistOrders.value = waitlistOrders.value.filter(o => o.orderId !== order.orderId)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}
</script>

<style lang="scss" scoped>
.mobile-order-list-page {
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

.tips-banner {
  margin: 12px;
  padding: 12px 14px;
  background: #fffbe6;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #d48806;
  line-height: 1.5;

  svg {
    flex-shrink: 0;
  }

  span {
    flex: 1;
  }
}

.order-list {
  padding: 0 12px 12px;
}

.order-card {
  background: white;
  border-radius: 10px;
  margin-bottom: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.2s;

  &:active {
    transform: scale(0.98);
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  }

  .order-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 14px;
    padding-bottom: 12px;
    border-bottom: 1px solid #f0f0f0;

    .train-info {
      display: flex;
      align-items: center;
      gap: 10px;

      .train-number {
        font-size: 18px;
        font-weight: 700;
        color: #1890ff;
      }

      .travel-date {
        font-size: 13px;
        color: #999;
      }
    }

    .status-badge {
      padding: 4px 10px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 600;

      &.pending {
        background: #fff7e6;
        color: #ff7e00;
      }

      &.paid {
        background: #f6ffed;
        color: #52c41a;
      }

      &.waitlist {
        background: #fffbe6;
        color: #faad14;
      }
    }
  }

  .station-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 14px;
    position: relative;
    padding: 0 4px;

    .station-item {
      flex: 1;

      .time {
        display: block;
        font-size: 18px;
        font-weight: 700;
        color: #333;
        margin-bottom: 4px;
      }

      .station-name {
        display: block;
        font-size: 14px;
        color: #666;
      }

      &.end {
        text-align: right;
      }
    }

    .duration-info {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 4px;
      padding: 0 12px;

      svg {
        opacity: 0.6;
      }

      .duration {
        font-size: 11px;
        color: #999;
      }
    }
  }

  .waitlist-detail {
    margin-bottom: 14px;
    padding: 12px;
    background: #fafafa;
    border-radius: 8px;

    .detail-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 8px;

      &:last-child {
        margin-bottom: 0;
      }

      .label {
        font-size: 13px;
        color: #999;
      }

      .value {
        font-size: 13px;
        color: #333;
        font-weight: 500;

        &.highlight {
          color: #faad14;
          font-weight: 700;
        }
      }
    }
  }

  .price-action {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    padding-top: 12px;
    border-top: 1px solid #f0f0f0;

    .price {
      font-size: 20px;
      font-weight: 700;
      color: #faad14;
    }

    .btn-cancel-waitlist {
      padding: 8px 16px;
      border-radius: 16px;
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      background: #fff1f0;
      color: #ff4d4f;
      border: 1px solid #ffa39e;
      transition: all 0.2s;

      &:active {
        opacity: 0.8;
        transform: scale(0.95);
      }
    }
  }

  .waitlist-time-tip {
    padding: 8px 12px;
    background: #fffbe6;
    border-radius: 6px;
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
    color: #d48806;
    line-height: 1.4;
  }
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 10px;
  margin-top: 20px;

  .empty-icon {
    margin-bottom: 16px;

    svg {
      width: 120px;
      height: auto;
      margin: 0 auto;
      display: block;
    }
  }

  .empty-text {
    font-size: 16px;
    color: #333;
    font-weight: 600;
    margin: 0 0 8px;
  }

  .empty-desc {
    font-size: 13px;
    color: #999;
    margin: 0;
  }
}
</style>
