<template>
  <div class="mobile-order-list-page">
    <!-- 顶部导航栏 -->
    <header class="top-nav">
      <button class="back-btn" @click="goBack">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5">
          <polyline points="15,18 9,12 15,6"/>
        </svg>
      </button>
      <h1 class="nav-title">已支付订单</h1>
      <div class="nav-placeholder"></div>
    </header>

    <!-- 订单列表 -->
    <div class="order-list">
      <div
        v-for="order in paidOrders"
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
          <span class="status-badge paid">已支付</span>
        </div>

        <!-- 车站信息 -->
        <div class="station-info">
          <div class="station-item start">
            <span class="time">{{ order.startTime }}</span>
            <span class="station-name">{{ order.fromStation }}</span>
          </div>
          <div class="duration-info">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#52c41a" stroke-width="2">
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
          <button class="btn btn-view" @click.stop="viewTicket(order)">查看车票</button>
          <button class="btn btn-refund" v-if="order.canRefund" @click.stop="refundOrder(order)">申请退票</button>
        </div>

        <!-- 支付时间 -->
        <div class="pay-time-tip">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#52c41a" stroke-width="2">
            <path d="M9 11l3 3L22 4"/>
            <path d="M21 12v7a2 2 0 01-2 2H5a2 2 0 01-2-2V5a2 2 0 012-2h11"/>
          </svg>
          支付时间：{{ order.payTime }}
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="paidOrders.length === 0" class="empty-state">
        <div class="empty-icon">
          <svg width="80" height="80" viewBox="0 0 120 100" fill="none">
            <rect x="20" y="30" width="80" height="50" rx="4" fill="#f6ffed" stroke="#52c41a" stroke-width="2"/>
            <circle cx="45" cy="50" r="8" fill="#52c41a" opacity="0.3"/>
            <path d="M35 65c0-3 2-5 5-5s5 2 5 5" stroke="#52c41a" stroke-width="2"/>
            <line x1="60" y1="42" x2="75" y2="42" stroke="#52c41a" stroke-width="2"/>
            <line x1="60" y1="50" x2="72" y2="50" stroke="#52c41a" stroke-width="2"/>
            <line x1="60" y1="58" x2="68" y2="58" stroke="#52c41a" stroke-width="2"/>
          </svg>
        </div>
        <p class="empty-text">暂无已支付订单</p>
        <p class="empty-desc">支付成功的订单将显示在这里</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

interface PaidOrder {
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
  payTime: string
  canRefund: boolean
}

const paidOrders = ref<PaidOrder[]>([
  {
    orderId: 20240114001,
    trainNumber: 'G101',
    travelDate: '2024-01-19',
    fromStation: '北京南',
    toStation: '上海虹桥',
    startTime: '07:00',
    endTime: '11:38',
    duration: '4小时38分',
    passengers: ['张三'],
    seatType: '商务座',
    totalPrice: 1748,
    payTime: '2024-01-18 20:35:22',
    canRefund: true
  },
  {
    orderId: 20240113002,
    trainNumber: 'G234',
    travelDate: '2024-01-25',
    fromStation: '杭州东',
    toStation: '南京南',
    startTime: '10:30',
    endTime: '13:45',
    duration: '3小时15分',
    passengers: ['李四', '王五'],
    seatType: '一等座',
    totalPrice: 579,
    payTime: '2024-01-17 15:20:10',
    canRefund: true
  },
  {
    orderId: 20240112003,
    trainNumber: 'D567',
    travelDate: '2024-01-18',
    fromStation: '武汉',
    toStation: '长沙南',
    startTime: '16:00',
    endTime: '18:32',
    duration: '2小时32分',
    passengers: ['赵六'],
    seatType: '二等座',
   总价: 184.5,
    payTime: '2024-01-16 09:15:33',
    canRefund: false
  },
  {
    orderId: 20240111004,
    trainNumber: 'K890',
    travelDate: '2024-02-01',
    fromStation: '西安北',
    toStation: '郑州东',
    startTime: '08:15',
    endTime: '13:28',
    duration: '5小时13分',
    passengers: ['钱七', '孙八', '周九'],
    seatType: '硬卧',
    totalPrice: 342,
    payTime: '2024-01-15 14:42:18',
    canRefund: true
  }
])

const goBack = () => {
  router.back()
}

const viewOrderDetail = (order: PaidOrder) => {
  router.push(`/order/${order.orderId}`)
}

const viewTicket = (order: PaidOrder) => {
  ElMessage.success('正在打开电子车票...')
}

const refundOrder = async (order: PaidOrder) => {
  try {
    await ElMessageBox.confirm(
      `确认退票？退票后将按规则收取手续费`,
      '退票确认',
      { type: 'warning', confirmButtonText: '确认退票' }
    )
    ElMessage.success('退票申请已提交，请等待审核')
    order.canRefund = false
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
      color: #52c41a;
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

      &.btn-view {
        background: #f5f7fa;
        color: #666;
      }

      &.btn-refund {
        background: linear-gradient(135deg, #ff7e00 0%, #d46b08 100%);
        color: white;
      }
    }
  }

  .pay-time-tip {
    margin-top: 12px;
    padding: 8px 12px;
    background: #f6ffed;
    border-radius: 6px;
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
    color: #389e0d;
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
