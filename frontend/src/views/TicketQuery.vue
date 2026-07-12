<template>
  <div class="ticket-page">
    <!-- 头部 -->
    <header class="header">
      <router-link to="/" class="logo">中国铁路12306</router-link>
      <nav class="nav-links">
        <router-link to="/">首页</router-link>
        <router-link to="/passengers">乘车人</router-link>
      </nav>
    </header>

    <!-- 查询条件 -->
    <div class="query-info">
      <div class="form-row">
        <div class="form-item">
          <label>出发站</label>
          <StationSelector
            :selected-id="queryForm.startStation"
            placeholder="点击选择出发站"
            @change="onStartStationChange"
          />
        </div>
        <div class="swap-btn" @click="handleSwap">↔</div>
        <div class="form-item">
          <label>到达站</label>
          <StationSelector
            :selected-id="queryForm.endStation"
            placeholder="点击选择到达站"
            @change="onEndStationChange"
          />
        </div>
        <div class="form-item">
          <label>出发日期</label>
          <el-date-picker
            v-model="queryForm.travelDate"
            type="date"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            :disabled-date="disabledDate"
            style="width: 100%"
          />
        </div>
        <button class="search-btn" @click="handleQuery">查询</button>
      </div>
    </div>

    <!-- 车次列表 -->
    <div class="train-list" v-loading="loading">
      <div v-if="trains.length === 0 && !loading" class="no-result">
        <el-empty description="暂无符合条件的车次" />
      </div>
      <div v-else>
        <div class="train-item" v-for="train in trains" :key="train.trainId">
          <div class="train-info">
            <div class="train-code">{{ train.trainCode }}</div>
            <div class="station-info">
              <span class="station">{{ train.startStationName }}</span>
              <span class="time">{{ formatTime(train.startTime) }}</span>
              <span class="arrow">→</span>
              <span class="station">{{ train.endStationName }}</span>
              <span class="time">{{ formatTime(train.endTime) }}</span>
            </div>
            <div class="duration">历时: {{ train.totalTimeText }}</div>
          </div>
          <div class="seat-info">
            <div class="seat-type" v-for="seat in train.seats" :key="seat.seatTypeId">
              <div class="name">{{ seat.seatTypeName }}</div>
              <div class="price">¥{{ seat.price }}</div>
              <div class="count">
                <span v-if="seat.hasTicket">{{ seat.availableSeats }}张</span>
                <span v-else-if="seat.canWaiting" class="waiting">候补</span>
                <span v-else class="no-ticket">无票</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部导航 -->
    <nav class="bottom-nav">
      <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }">
        <span class="nav-icon">🏠</span>
        <span class="nav-text">首页</span>
      </router-link>
      <router-link to="/passengers" class="nav-item" :class="{ active: $route.path === '/passengers' }">
        <span class="nav-icon">👤</span>
        <span class="nav-text">乘车人</span>
      </router-link>
    </nav>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { trainApi } from '@/api'
import { ElMessage } from 'element-plus'
import StationSelector from '@/components/StationSelector.vue'

interface TrainVO {
  trainId: number
  trainCode: string
  startStationName: string
  endStationName: string
  startTime: string
  endTime: string
  totalTimeText: string
  travelDate: string
  seats: SeatVO[]
}

interface SeatVO {
  seatTypeId: number
  seatTypeName: string
  price: number
  availableSeats: number
  hasTicket: boolean
  canWaiting: boolean
}

const route = useRoute()

const loading = ref(false)
const trains = ref<TrainVO[]>([])

const queryForm = ref({
  startStation: '',
  endStation: '',
  travelDate: ''
})

const disabledDate = (date: Date) => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  return date < today
}

const onStartStationChange = (station: any) => {
  if (station) {
    queryForm.value.startStation = station.id
  }
}

const onEndStationChange = (station: any) => {
  if (station) {
    queryForm.value.endStation = station.id
  }
}

const handleSwap = () => {
  const temp = queryForm.value.startStation
  queryForm.value.startStation = queryForm.value.endStation
  queryForm.value.endStation = temp
}

onMounted(() => {
  if (route.query.startStation) queryForm.value.startStation = route.query.startStation as string
  if (route.query.endStation) queryForm.value.endStation = route.query.endStation as string
  if (route.query.travelDate) queryForm.value.travelDate = route.query.travelDate as string

  if (!queryForm.value.travelDate) {
    const today = new Date()
    queryForm.value.travelDate = today.toISOString().split('T')[0]
  }

  if (queryForm.value.startStation && queryForm.value.endStation) {
    handleQuery()
  }
})

const handleQuery = async () => {
  if (!queryForm.value.startStation || !queryForm.value.endStation || !queryForm.value.travelDate) {
    ElMessage.warning('请选择出发站、到达站和出发日期')
    return
  }

  loading.value = true
  try {
    const res = await trainApi.query({
      startStation: String(queryForm.value.startStation),
      endStation: String(queryForm.value.endStation),
      travelDate: queryForm.value.travelDate
    })
    trains.value = res.data
  } catch (error: any) {
    console.error('查询车次失败:', error)
  } finally {
    loading.value = false
  }
}

const formatTime = (time: string) => {
  return time?.substring(0, 5) || ''
}
</script>

<style scoped>
.ticket-page {
  padding: 20px;
  padding-bottom: 80px;
}

.header {
  background: #1a365d;
  padding: 12px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: -20px -20px 20px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 20px;
}

.nav-links a {
  color: #fff;
  text-decoration: none;
  font-size: 14px;
}

.query-info {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.form-row {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.form-item {
  flex: 1;
  min-width: 150px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-item label {
  font-size: 12px;
  color: #666;
}

.swap-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 50%;
  cursor: pointer;
  font-size: 18px;
  color: #666;
}

.search-btn {
  background: #f57c00;
  color: #fff;
  border: none;
  padding: 10px 24px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  white-space: nowrap;
}

.search-btn:hover {
  background: #e65100;
}

.train-list {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.train-item {
  display: flex;
  justify-content: space-between;
  padding: 16px 0;
  border-bottom: 1px solid #eee;
}

.train-item:last-child {
  border-bottom: none;
}

.train-info {
  .train-code {
    font-size: 20px;
    font-weight: bold;
    color: #3b99fc;
  }
  .station-info {
    margin-top: 10px;
    .station {
      font-size: 14px;
      color: #333;
    }
    .time {
      font-size: 16px;
      font-weight: bold;
      margin: 0 8px;
    }
    .arrow {
      color: #999;
      margin: 0 15px;
    }
  }
  .duration {
    color: #999;
    font-size: 12px;
    margin-top: 5px;
  }
}

.seat-info {
  display: flex;
  gap: 16px;
}

.seat-type {
  text-align: center;
  min-width: 70px;
  .name {
    font-size: 12px;
    color: #666;
  }
  .price {
    font-size: 14px;
    color: #ff7e00;
    font-weight: bold;
  }
  .count {
    font-size: 12px;
    color: #67c23a;
    .waiting {
      color: #ff7e00;
    }
    .no-ticket {
      color: #f56c6c;
    }
  }
}

.no-result {
  padding: 40px;
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  display: flex;
  justify-content: space-around;
  padding: 12px 0;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.bottom-nav .nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  text-decoration: none;
  color: #666;
}

.bottom-nav .nav-item .nav-icon {
  font-size: 22px;
}

.bottom-nav .nav-item .nav-text {
  font-size: 12px;
}

.bottom-nav .nav-item.active {
  color: #1e88e5;
}
</style>