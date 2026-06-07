<template>
  <div class="mobile-order-list-page">
    <!-- 顶部导航栏 -->
    <header class="top-nav">
      <button class="back-btn" @click="goBack">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2.5">
          <polyline points="15,18 9,12 15,6"/>
        </svg>
      </button>
      <h1 class="nav-title">本人车票</h1>
      <div class="nav-placeholder"></div>
    </header>

    <!-- 筛选标签 -->
    <div class="filter-tabs">
      <div
        v-for="tab in filterTabs"
        :key="tab.key"
        class="filter-tab"
        :class="{ active: activeFilter === tab.key }"
        @click="activeFilter = tab.key"
      >
        {{ tab.label }}
      </div>
    </div>

    <!-- 车票列表 -->
    <div class="ticket-list">
      <div
        v-for="ticket in filteredTickets"
        :key="ticket.ticketId"
        class="ticket-card"
        @click="viewTicketDetail(ticket)"
      >
        <!-- 车票头部 -->
        <div class="ticket-header">
          <span class="train-number">{{ ticket.trainNumber }}</span>
          <span class="ticket-status" :class="ticket.statusClass">{{ ticket.statusText }}</span>
        </div>

        <!-- 车站信息 -->
        <div class="station-info">
          <div class="station-item start">
            <span class="time">{{ ticket.startTime }}</span>
            <span class="station-name">{{ ticket.fromStation }}</span>
            <span class="platform"> {{ ticket.fromPlatform }} 站台</span>
          </div>
          <div class="duration-info">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#722ed1" stroke-width="2">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
            <span class="duration">{{ ticket.duration }}</span>
          </div>
          <div class="station-item end">
            <span class="time">{{ ticket.endTime }}</span>
            <span class="station-name">{{ ticket.toStation }}</span>
            <span class="platform"> {{ ticket.toPlatform }} 站台</span>
          </div>
        </div>

        <!-- 座位信息 -->
        <div class="seat-info-row">
          <div class="seat-detail">
            <span class="seat-type">{{ ticket.seatType }}</span>
            <span class="seat-number">{{ ticket.carriage }}车{{ ticket.seatNumber }}号</span>
          </div>
          <span class="passenger-name">{{ ticket.passengerName }}</span>
        </div>

        <!-- 日期和价格 -->
        <div class="price-date-row">
          <span class="travel-date">{{ ticket.travelDate }} 出发</span>
          <span class="price">¥{{ ticket.price }}</span>
        </div>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <button
            v-if="ticket.status === 'upcoming'"
            class="btn btn-change"
            @click.stop="changeTicket(ticket)"
          >改签</button>
          <button
            v-if="ticket.status === 'upcoming'"
            class="btn btn-refund"
            @click.stop="refundTicket(ticket)"
          >退票</button>
          <button
            class="btn btn-view"
            @click.stop="showQRCode(ticket)"
          >查看二维码</button>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="filteredTickets.length === 0" class="empty-state">
        <div class="empty-icon">
          <svg width="80" height="80" viewBox="0 0 120 100" fill="none">
            <rect x="25" y="30" width="70" height="45" rx="4" fill="#f9f0ff" stroke="#722ed1" stroke-width="2"/>
            <circle cx="50" cy="52" r="8" fill="#722ed1" opacity="0.3"/>
            <circle cx="60" cy="48" r="2" fill="#722ed1"/>
            <line x1="40" y1="65" x2="80" y2="65" stroke="#722ed1" stroke-width="2"/>
          </svg>
        </div>
        <p class="empty-text">暂无{{ getFilterLabel() }}车票</p>
        <p class="empty-desc">您的车票将显示在这里</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const activeFilter = ref('all')

interface Ticket {
  ticketId: number
  trainNumber: string
  travelDate: string
  fromStation: string
  toStation: string
  startTime: string
  endTime: string
  duration: string
  fromPlatform: string
  toPlatform: string
  seatType: string
  carriage: string
  seatNumber: string
  passengerName: string
  price: number
  status: 'upcoming' | 'completed' | 'refunded'
  statusText: string
  statusClass: string
}

const tickets = ref<Ticket[]>([
  {
    ticketId: 10001,
    trainNumber: 'G123',
    travelDate: '2024-02-05',
    fromStation: '北京南',
    toStation: '上海虹桥',
    startTime: '08:00',
    endTime: '13:28',
    duration: '5小时28分',
    fromPlatform: '08',
    toPlatform: '12',
    seatType: '二等座',
    carriage: '05',
    seatNumber: '12A',
    passengerName: '张三',
    price: 553,
    status: 'upcoming',
    statusText: '未出行',
    statusClass: 'status-upcoming'
  },
  {
    ticketId: 10002,
    trainNumber: 'D456',
    travelDate: '2024-01-20',
    fromStation: '广州南',
    toStation: '深圳北',
    startTime: '09:15',
    endTime: '10:32',
    duration: '1小时17分',
    fromPlatform: '03',
    toPlatform: '06',
    seatType: '一等座',
    carriage: '03',
    seatNumber: '05F',
    passengerName: '张三',
    price: 99.5,
    status: 'completed',
    statusText: '已出行',
    statusClass: 'status-completed'
  },
  {
    ticketId: 10003,
    trainNumber: 'G789',
    travelDate: '2024-01-15',
    fromStation: '杭州东',
    toStation: '南京南',
    startTime: '14:30',
    endTime: '17:45',
    duration: '3小时15分',
    fromPlatform: '11',
    toPlatform: '07',
    seatType: '商务座',
    carriage: '02',
    seatNumber: '03A',
    passengerName: '张三',
    price: 479,
    status: 'refunded',
    statusText: '已退票',
    statusClass: 'status-refunded'
  },
  {
    ticketId: 10004,
    trainNumber: 'K101',
    travelDate: '2024-02-10',
    fromStation: '成都东',
    toStation: '重庆西',
    startTime: '19:00',
    endTime: '21:30',
    duration: '2小时30分',
    fromPlatform: '05',
    toPlatform: '04',
    seatType: '硬卧',
    carriage: '08',
    seatNumber: '下铺23',
    passengerName: '张三',
    price: 98,
    status: 'upcoming',
    statusText: '未出行',
    statusClass: 'status-upcoming'
  }
])

const filterTabs = [
  { key: 'all', label: '全部' },
  { key: 'upcoming', label: '未出行' },
  { key: 'completed', label: '已出行' },
  { key: 'refunded', label: '已退票' }
]

const filteredTickets = computed(() => {
  if (activeFilter.value === 'all') {
    return tickets.value
  }
  return tickets.value.filter(t => t.status === activeFilter.value)
})

const goBack = () => {
  router.back()
}

const viewTicketDetail = (ticket: Ticket) => {
  ElMessage.info(`查看车票详情：${ticket.trainNumber}`)
}

const changeTicket = async (ticket: Ticket) => {
  try {
    await ElMessageBox.confirm(
      `确认改签 ${ticket.trainNumber} 次列车？`,
      '改签确认',
      { type: 'info' }
    )
    ElMessage.success('正在为您查询可改签的车次...')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const refundTicket = async (ticket: Ticket) => {
  try {
    await ElMessageBox.confirm(
      `确认退票？将收取 ${Math.round(ticket.price * 0.2)} 元手续费`,
      '退票确认',
      { type: 'warning' }
    )
    ticket.status = 'refunded'
    ticket.statusText = '已退票'
    ticket.statusClass = 'status-refunded'
    ElMessage.success('退票成功，退款将在3-7个工作日内到账')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const showQRCode = (ticket: Ticket) => {
  ElMessage.success('正在生成乘车二维码...')
}

const getFilterLabel = (): string => {
  const tab = filterTabs.find(t => t.key === activeFilter.value)
  return tab ? tab.label : ''
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

.filter-tabs {
  display: flex;
  gap: 8px;
  padding: 12px;
  background: white;

  .filter-tab {
    flex: 1;
    padding: 8px 12px;
    text-align: center;
    font-size: 13px;
    color: #666;
    background: #f5f7fa;
    border-radius: 16px;
    cursor: pointer;
    transition: all 0.2s;

    &.active {
      background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
      color: white;
      font-weight: 600;
    }

    &:active {
      transform: scale(0.95);
    }
  }
}

.ticket-list {
  padding: 0 12px 12px;
}

.ticket-card {
  background: white;
  border-radius: 10px;
  margin-bottom: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.2s;
  border-left: 4px solid #722ed1;

  &:active {
    transform: scale(0.98);
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  }

  .ticket-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 14px;

    .train-number {
      font-size: 18px;
      font-weight: 700;
      color: #1890ff;
    }

    .ticket-status {
      padding: 4px 10px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 600;

      &.status-upcoming {
        background: #e6f7ff;
        color: #1890ff;
      }

      &.status-completed {
        background: #f6ffed;
        color: #52c41a;
      }

      &.status-refunded {
        background: #fff1f0;
        color: #ff4d4f;
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
        margin-bottom: 2px;
      }

      .platform {
        display: block;
        font-size: 12px;
        color: #999;
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

  .seat-info-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    padding: 10px 0;
    border-top: 1px dashed #f0f0f0;
    border-bottom: 1px dashed #f0f0f0;

    .seat-detail {
      display: flex;
      gap: 8px;

      .seat-type {
        font-size: 13px;
        color: #666;
        background: #f5f7fa;
        padding: 4px 8px;
        border-radius: 4px;
      }

      .seat-number {
        font-size: 13px;
        color: #333;
        font-weight: 500;
      }
    }

    .passenger-name {
      font-size: 14px;
      color: #722ed1;
      font-weight: 600;
    }
  }

  .price-date-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    .travel-date {
      font-size: 13px;
      color: #999;
    }

    .price {
      font-size: 20px;
      font-weight: 700;
      color: #722ed1;
    }
  }

  .action-buttons {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
    padding-top: 12px;
    border-top: 1px solid #f0f0f0;

    .btn {
      padding: 6px 14px;
      border-radius: 14px;
      font-size: 13px;
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

      &.btn-change {
        background: #e6f7ff;
        color: #1890ff;
      }

      &.btn-refund {
        background: #fff1f0;
        color: #ff4d4f;
      }
    }
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
