<template>
  <div class="mobile-order-list-page">
    <!-- 顶部导航栏 -->
    <header class="top-nav">
      <button class="back-btn" @click="goBack">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5">
          <polyline points="15,18 9,12 15,6"/>
        </svg>
      </button>
      <h1 class="nav-title">待支付订单</h1>
      <div class="nav-placeholder"></div>
    </header>

    <!-- 订单列表 -->
    <div class="order-list">
      <div
        v-for="order in pendingOrders"
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
          <span class="status-badge pending">待支付</span>
        </div>

        <!-- 车站信息 -->
        <div class="station-info">
          <div class="station-item start">
            <span class="time">{{ order.startTime }}</span>
            <span class="station-name">{{ order.fromStation }}</span>
          </div>
          <div class="duration-info">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#1890ff" stroke-width="2">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
            <span class="duration">{{ order.duration }}</span>
          </div>
          <div class="station-item end">
            <span class="time">{{ order.endTime }}</span>
            <span class="station-name">{{ order.toStation }}</span>
          </div>
        </div>

        <!-- 乘客信息 -->
        <div class="passenger-info">
          <span class="label">乘车人：</span>
          <span class="names">{{ order.passengers.join('、') }}</span>
        </div>

        <!-- 座位信息 -->
        <div class="seat-info">
          <span class="seat-type">{{ order.seatType }}</span>
          <span class="price">¥{{ order.totalPrice }}</span>
        </div>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <button class="btn btn-cancel" @click.stop="cancelOrder(order)">取消订单</button>
          <button class="btn btn-pay" @click.stop="payOrder(order)">立即支付</button>
        </div>

        <!-- 倒计时提示 -->
        <div class="countdown-tip" v-if="order.countdown > 0">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#ff7e00" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <polyline points="12,6 12,12 16,14"/>
          </svg>
          剩余支付时间：{{ formatCountdown(order.countdown) }}
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="pendingOrders.length === 0" class="empty-state">
        <div class="empty-icon">
          <svg width="80" height="80" viewBox="0 0 120 100" fill="none">
            <rect x="20" y="30" width="80" height="50" rx="4" fill="#fffbe6" stroke="#faad14" stroke-width="2"/>
            <circle cx="45" cy="50" r="8" fill="#faad14" opacity="0.3"/>
            <path d="M35 65c0-3 2-5 5-5s5 2 5 5" stroke="#faad14" stroke-width="2"/>
            <line x1="60" y1="42" x2="75" y2="42" stroke="#faad14" stroke-width="2"/>
            <line x1="60" y1="50" x2="72" y2="50" stroke="#faad14" stroke-width="2"/>
            <line x1="60" y1="58" x2="68" y2="58" stroke="#faad14" stroke-width="2"/>
          </svg>
        </div>
        <p class="empty-text">暂无待支付订单</p>
        <p class="empty-desc">您可以去购票体验完整流程</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

interface PendingOrder {
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
  countdown: number
}

const pendingOrders = ref<PendingOrder[]>([
  {
    orderId: 20240115001,
    trainNumber: 'G123',
    travelDate: '2024-01-20',
    fromStation: '北京南',
    toStation: '上海虹桥',
    startTime: '08:00',
    endTime: '13:28',
    duration: '5小时28分',
    passengers: ['张三', '李四'],
    seatType: '二等座',
    totalPrice: 1310,
    countdown: 1800
  },
  {
    orderId: 20240115002,
    trainNumber: 'D456',
    travelDate: '2024-01-21',
    fromStation: '广州南',
    toStation: '深圳北',
    startTime: '09:15',
    endTime: '10:32',
    duration: '1小时17分',
    passengers: ['王五'],
    seatType: '一等座',
    totalPrice: 99.5,
    countdown: 2400
  },
  {
    orderId: 20240115003,
    trainNumber: 'K789',
    travelDate: '2024-01-22',
    fromStation: '成都东',
    toStation: '重庆西',
    startTime: '14:30',
    endTime: '17:45',
    duration: '3小时15分',
    passengers: ['赵六', '钱七', '孙八'],
    seatType: '硬卧',
    totalPrice: 286.5,
    countdown: 3600
  }
])

const goBack = () => {
  router.back()
}

const viewOrderDetail = (order: PendingOrder) => {
  router.push(`/order/${order.orderId}`)
}

const payOrder = async (order: PendingOrder) => {
  try {
    await ElMessageBox.confirm(
      `确认支付订单 ¥${order.totalPrice}？`,
      '支付确认',
      { type: 'info', confirmButtonText: '确认支付' }
    )
    ElMessage.success('支付成功！')
    pendingOrders.value = pendingOrders.value.filter(o => o.orderId !== order.orderId)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('支付失败，请重试')
    }
  }
}

const cancelOrder = async (order: PendingOrder) => {
  try {
    await ElMessageBox.confirm(
      `确认取消订单 ${order.trainNumber}？`,
      '取消确认',
      { type: 'warning', confirmButtonText: '确认取消' }
    )
    ElMessage.success('订单已取消')
    pendingOrders.value = pendingOrders.value.filter(o => o.orderId !== order.orderId)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const formatCountdown = (seconds: number): string => {
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:00`
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

.order-list {
  padding: 12px;
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

  .passenger-info {
    font-size: 13px;
    color: #666;
    margin-bottom: 10px;
    padding: 8px 0;
    border-top: 1px dashed #f0f0f0;

    .label {
      color: #999;
    }

    .names {
      color: #333;
    }
  }

  .seat-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 14px;

    .seat-type {
      font-size: 13px;
      color: #666;
      background: #f5f7fa;
      padding: 4px 10px;
      border-radius: 4px;
    }

    .price {
      font-size: 20px;
      font-weight: 700;
      color: #ff7e00;
    }
  }

  .action-buttons {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    padding-top: 12px;
    border-top: 1px solid #f0f0f0;

    .btn {
      padding: 8px 16px;
      border-radius: 16px;
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      border: none;
      transition: all 0.2s;

      &:active {
        opacity: 0.8;
        transform: scale(0.95);
      }

      &.btn-cancel {
        background: #f5f7fa;
        color: #666;
      }

      &.btn-pay {
        background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
        color: white;
      }
    }
  }

  .countdown-tip {
    margin-top: 12px;
    padding: 8px 12px;
    background: #fffbe6;
    border-radius: 6px;
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
    color: #d48806;
    font-weight: 500;
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
