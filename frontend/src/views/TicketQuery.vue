<template>
  <div class="ticket-page">
    <!-- 顶部查询栏 -->
    <div class="top-query-bar">
      <div class="query-left">
        <label class="radio-label">
          <input type="radio" name="tripType" value="single" v-model="tripType" />
          <span>单程</span>
        </label>
        <label class="radio-label">
          <input type="radio" name="tripType" value="round" v-model="tripType" />
          <span>往返</span>
        </label>
      </div>
      <div class="query-center">
        <div class="input-group">
          <span class="input-label">出发地</span>
          <StationSelector
            :selected-id="startStationId"
            @change="handleStartStationChange"
            placeholder="请输入出发站"
          />
        </div>
        <button class="swap-btn" @click="handleSwap">↔</button>
        <div class="input-group">
          <span class="input-label">目的地</span>
          <StationSelector
            :selected-id="endStationId"
            @change="handleEndStationChange"
            placeholder="请输入到达站"
          />
        </div>
        <div class="input-group date-group">
          <span class="input-label">出发日</span>
          <el-date-picker
            v-model="selectedDate"
            type="date"
            placeholder="选择日期"
            :disabled-date="disabledDate"
            value-format="YYYY-MM-DD"
            @change="handleQuery"
          />
        </div>
        <div class="input-group date-group" v-if="tripType === 'round'">
          <span class="input-label">返程日</span>
          <el-date-picker
            v-model="returnDate"
            type="date"
            placeholder="选择日期"
            :disabled-date="disabledDate"
            value-format="YYYY-MM-DD"
          />
        </div>
      </div>
      <div class="query-right">
        <label class="radio-label">
          <input type="radio" name="passengerType" value="normal" checked />
          <span>普通</span>
        </label>
        <label class="radio-label">
          <input type="radio" name="passengerType" value="student" />
          <span>学生</span>
        </label>
        <button class="query-btn" @click="handleQuery">查询</button>
      </div>
    </div>

    <!-- 日期选择栏 -->
    <div class="date-bar">
      <div class="date-item" 
           :class="{ active: selectedDate === date }" 
           v-for="date in dateList" 
           :key="date" 
           @click="selectDate(date)">
        <div class="date-num">{{ formatDateNum(date) }}</div>
        <div class="date-week">{{ getWeekDay(date) }}</div>
      </div>
    </div>

    <!-- 筛选区域 -->
    <div class="filter-panel">
      <div class="filter-row">
        <div class="filter-group">
          <span class="filter-title">车次类型：</span>
          <label class="filter-checkbox">
            <input type="checkbox" checked />
            <span>全部</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>GC-高铁/城际</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>D-动车</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>Z-直达</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>T-特快</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>K-快速</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>其他</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>复兴号</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>智能动车组</span>
          </label>
        </div>
        <div class="time-filter">
          <span>发车时间：</span>
          <select>
            <option>00:00-24:00</option>
            <option>06:00-12:00</option>
            <option>12:00-18:00</option>
            <option>18:00-24:00</option>
          </select>
        </div>
      </div>

      <div class="filter-row">
        <div class="filter-group">
          <span class="filter-title">出发车站：</span>
          <label class="filter-checkbox">
            <input type="checkbox" checked />
            <span>全部</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>长沙</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>长沙南</span>
          </label>
        </div>
      </div>

      <div class="filter-row">
        <div class="filter-group">
          <span class="filter-title">到达车站：</span>
          <label class="filter-checkbox">
            <input type="checkbox" checked />
            <span>全部</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>徐州</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>徐州东</span>
          </label>
        </div>
      </div>

      <div class="filter-row">
        <div class="filter-group">
          <span class="filter-title">车次席别：</span>
          <label class="filter-checkbox">
            <input type="checkbox" checked />
            <span>全部</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>商务座</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>一等座</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>二等座</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>一等卧</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>二等卧</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>软卧</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>硬卧</span>
          </label>
          <label class="filter-checkbox">
            <input type="checkbox" />
            <span>硬座</span>
          </label>
        </div>
        <button class="filter-btn">筛选</button>
      </div>
    </div>

    <!-- 结果区域 -->
    <div class="result-panel">
      <div class="result-header">
        <span class="route-info">{{ startStationName }} --&gt; {{ endStationName }}</span>
        <span class="date-info">（{{ formatChineseDate(selectedDate) }} {{ getWeekDay(selectedDate) }}）</span>
        <span class="count-info">共计 {{ trains.length }} 个车次</span>
        <span class="tip">您可使用<span class="link">中转换乘</span>功能，查询途中换乘一次的部分列车余票情况。</span>
        <label class="filter-checkbox">
          <input type="checkbox" />
          <span>显示折扣车次</span>
        </label>
        <label class="filter-checkbox">
          <input type="checkbox" />
          <span>显示积分兑换车次</span>
        </label>
        <label class="filter-checkbox">
          <input type="checkbox" />
          <span>显示全部可预订车次</span>
        </label>
      </div>

      <div class="table-wrapper">
        <table class="train-table">
          <thead>
            <tr>
              <th class="col-train">车次</th>
              <th class="col-station">出发站<br/>到达站</th>
              <th class="col-time">出发时间<br/>到达时间</th>
              <th class="col-duration">历时</th>
              <th class="col-business">商务座<br/>特等座</th>
              <th class="col-preferred">优选<br/>一等座</th>
              <th class="col-first">一等座</th>
              <th class="col-second">二等座<br/>二等包座</th>
              <th class="col-senior">高级<br/>软卧</th>
              <th class="col-soft">软卧/动卧<br/>一等卧</th>
              <th class="col-hard">硬卧<br/>二等卧</th>
              <th class="col-soft-seat">软座</th>
              <th class="col-hard-seat">硬座</th>
              <th class="col-no-seat">无座</th>
              <th class="col-other">其他</th>
              <th class="col-remark">备注</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="train in trains" :key="train.trainId" class="train-row">
              <td class="train-col">
                <div class="train-header">
                  <span class="train-code">{{ train.trainCode }}</span>
                  <span class="expand-icon">▼</span>
                </div>
                <div class="train-tags">
                  <span v-if="train.isFuxing" class="tag fuxing">复</span>
                  <span v-if="train.isJing" class="tag jing">静</span>
                  <span v-if="train.isZhi" class="tag zhi">智</span>
                </div>
                <div class="train-stops">
                  <div class="stop">◎ {{ train.startStationName }}</div>
                  <div class="stop">◎ {{ train.endStationName }}</div>
                </div>
              </td>
              <td class="station-col">
                <div class="station-name">{{ train.startStationName }}</div>
                <div class="station-divider"></div>
                <div class="station-name">{{ train.endStationName }}</div>
              </td>
              <td class="time-col">
                <div class="time-value">{{ formatTime(train.startTime) }}</div>
                <div class="time-divider"></div>
                <div class="time-value">{{ formatTime(train.endTime) }}</div>
              </td>
              <td class="duration-col">
                <div class="duration-value">{{ train.totalTimeText }}</div>
                <div class="duration-label">当日到达</div>
              </td>
              <td>{{ getSeatStatus(train, '商务座') }}</td>
              <td>{{ getSeatStatus(train, '优选') }}</td>
              <td>{{ getSeatStatus(train, '一等座') }}</td>
              <td>{{ getSeatStatus(train, '二等座') }}</td>
              <td>{{ getSeatStatus(train, '高级软卧') }}</td>
              <td>{{ getSeatStatus(train, '软卧') }}</td>
              <td>{{ getSeatStatus(train, '硬卧') }}</td>
              <td>{{ getSeatStatus(train, '软座') }}</td>
              <td>{{ getSeatStatus(train, '硬座') }}</td>
              <td>{{ getSeatStatus(train, '无座') }}</td>
              <td>{{ getSeatStatus(train, '其他') }}</td>
              <td>
                <button class="book-btn" :disabled="!hasAvailableSeat(train)">预订</button>
              </td>
            </tr>
          </tbody>
        </table>
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
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { trainApi } from '@/api'
import StationSelector from '@/components/StationSelector.vue'

interface TrainVO {
  trainId: number
  trainCode: string
  startStationName: string
  endStationName: string
  startTime: string
  endTime: string
  totalTimeText: string
  isFuxing?: boolean
  isJing?: boolean
  isZhi?: boolean
  seats: SeatVO[]
}

interface SeatVO {
  seatTypeName: string
  availableSeats: number
  hasTicket: boolean
  canWaiting: boolean
  price: number
}

const route = useRoute()

const loading = ref(false)
const trains = ref<TrainVO[]>([])

const selectedDate = ref('')
const returnDate = ref('')
const tripType = ref('single')
const todayStr = new Date().toISOString().split('T')[0]

const startStationId = ref('')
const endStationId = ref('')
const startStationName = ref('')
const endStationName = ref('')

const disabledDate = (date: Date) => {
  return date < new Date(new Date().toDateString())
}

const dateList = computed(() => {
  const dates = []
  const today = new Date()
  for (let i = 0; i < 15; i++) {
    const date = new Date(today)
    date.setDate(today.getDate() + i)
    dates.push(date.toISOString().split('T')[0])
  }
  return dates
})

const formatDateNum = (date: string) => {
  const d = new Date(date)
  return `${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

const formatChineseDate = (date: string) => {
  const d = new Date(date)
  return `${d.getMonth() + 1}月${d.getDate()}日`
}

const getWeekDay = (date: string) => {
  const d = new Date(date)
  const weeks = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  return weeks[d.getDay()]
}

const selectDate = (date: string) => {
  selectedDate.value = date
  handleQuery()
}

const formatTime = (time: string) => {
  return time?.substring(0, 5) || ''
}

const getSeatStatus = (train: TrainVO, seatName: string) => {
  const seat = train.seats.find(s => s.seatTypeName.includes(seatName) || seatName.includes(s.seatTypeName))
  if (!seat) return '--'
  if (seat.hasTicket) {
    if (seat.availableSeats > 0) return seat.availableSeats
    return '有'
  }
  if (seat.canWaiting) return '<span class="waiting">候补</span>'
  return '无'
}

const hasAvailableSeat = (train: TrainVO) => {
  return train.seats.some(s => s.hasTicket)
}

const handleStartStationChange = (station: any) => {
  if (station) {
    startStationId.value = station.id
    startStationName.value = station.stationName
  } else {
    startStationId.value = ''
    startStationName.value = ''
  }
}

const handleEndStationChange = (station: any) => {
  if (station) {
    endStationId.value = station.id
    endStationName.value = station.stationName
  } else {
    endStationId.value = ''
    endStationName.value = ''
  }
}

const handleSwap = () => {
  const tempId = startStationId.value
  startStationId.value = endStationId.value
  endStationId.value = tempId
  
  const tempName = startStationName.value
  startStationName.value = endStationName.value
  endStationName.value = tempName
}

onMounted(() => {
  if (route.query.startStation) startStationId.value = route.query.startStation as string
  if (route.query.endStation) endStationId.value = route.query.endStation as string
  if (route.query.startStationName) startStationName.value = route.query.startStationName as string
  if (route.query.endStationName) endStationName.value = route.query.endStationName as string
  if (route.query.travelDate) selectedDate.value = route.query.travelDate as string

  if (!selectedDate.value) {
    selectedDate.value = todayStr
  }

  if (startStationId.value && endStationId.value) {
    handleQuery()
  }
})

const handleQuery = async () => {
  if (!startStationId.value || !endStationId.value || !selectedDate.value) {
    console.warn('查询条件不完整:', startStationId.value, endStationId.value, selectedDate.value)
    return
  }
  loading.value = true
  try {
    const res = await trainApi.query({
      startStation: startStationId.value,
      endStation: endStationId.value,
      travelDate: selectedDate.value
    })
    trains.value = res.data
  } catch (error) {
    console.error('查询车次失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.ticket-page {
  padding-bottom: 80px;
  background: #f5f5f5;
}

.top-query-bar {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 10px 15px;
  background: #e8ecef;
  border: 1px solid #d0d7de;
}

.query-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.query-center {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.query-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.radio-label {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  cursor: pointer;
}

.radio-label input[type="radio"] {
  width: 14px;
  height: 14px;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.input-label {
  font-size: 12px;
  color: #666;
}

.input-group :deep(.el-input__wrapper) {
  padding: 4px 8px;
  box-shadow: 0 0 0 1px #ccc inset;
  border-radius: 3px;
}

.input-group :deep(.el-input__inner) {
  font-size: 13px;
  height: 24px;
  line-height: 24px;
}

.date-group :deep(.el-date-editor) {
  width: 140px;
}

.swap-btn {
  margin-top: 18px;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border: 1px solid #ccc;
  border-radius: 3px;
  cursor: pointer;
  font-size: 14px;
}

.query-btn {
  margin-top: 12px;
  background: #f57c00;
  color: #fff;
  border: none;
  padding: 7px 24px;
  border-radius: 3px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
}

.date-bar {
  display: flex;
  background: #d0d7de;
  border-bottom: 1px solid #d0d7de;
}

.date-item {
  flex: 1;
  padding: 6px 2px;
  text-align: center;
  cursor: pointer;
  background: #e8ecef;
  border-right: 1px solid #d0d7de;
}

.date-item:last-child {
  border-right: none;
}

.date-item:hover {
  background: #dce4ec;
}

.date-item.active {
  background: #1a365d;
  color: #fff;
}

.date-num {
  font-size: 13px;
  font-weight: bold;
}

.date-week {
  font-size: 10px;
  margin-top: 1px;
}

.filter-panel {
  padding: 8px 12px;
  background: #fff;
  border-bottom: 1px solid #d0d7de;
}

.filter-row {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.filter-row:last-child {
  margin-bottom: 0;
}

.filter-group {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  flex: 1;
}

.filter-title {
  font-size: 12px;
  color: #333;
  white-space: nowrap;
}

.filter-checkbox {
  display: flex;
  align-items: center;
  gap: 3px;
  font-size: 12px;
  color: #333;
  cursor: pointer;
}

.filter-checkbox input {
  width: 13px;
  height: 13px;
}

.time-filter {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #333;
}

.time-filter select {
  padding: 3px 6px;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-size: 11px;
}

.filter-btn {
  background: #f57c00;
  color: #fff;
  border: none;
  padding: 5px 16px;
  border-radius: 3px;
  font-size: 12px;
  cursor: pointer;
}

.result-panel {
  padding: 8px 12px;
}

.result-header {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 6px;
  font-size: 13px;
  color: #333;
}

.route-info {
  font-weight: bold;
}

.date-info {
  color: #666;
}

.count-info {
  color: #666;
}

.tip {
  color: #666;
  font-size: 12px;
}

.tip .link {
  color: #1e88e5;
  cursor: pointer;
}

.table-wrapper {
  overflow-x: auto;
  background: #fff;
  border: 1px solid #d0d7de;
}

.train-table {
  width: 100%;
  min-width: 1400px;
  border-collapse: collapse;
}

.train-table thead {
  background: #1a365d;
  color: #fff;
}

.train-table th {
  padding: 7px 4px;
  font-size: 11px;
  text-align: center;
  font-weight: normal;
  white-space: nowrap;
}

.train-table td {
  padding: 6px 4px;
  font-size: 12px;
  text-align: center;
  border-bottom: 1px solid #eee;
}

.train-table tbody tr:hover {
  background: #f5f5f5;
}

.train-row {
  height: 60px;
}

.train-col {
  width: 120px;
  vertical-align: top;
  padding: 4px !important;
}

.train-header {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 2px;
}

.train-code {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.expand-icon {
  font-size: 10px;
  color: #666;
}

.train-tags {
  display: flex;
  gap: 2px;
  margin-bottom: 2px;
}

.tag {
  display: inline-block;
  padding: 0 3px;
  border-radius: 2px;
  font-size: 9px;
  font-weight: bold;
}

.tag.fuxing {
  background: #ff6b6b;
  color: #fff;
}

.tag.jing {
  background: #ffc107;
  color: #333;
}

.tag.zhi {
  background: #4caf50;
  color: #fff;
}

.train-stops {
  text-align: left;
}

.stop {
  font-size: 11px;
  color: #666;
}

.station-col {
  width: 100px;
  vertical-align: top;
}

.station-name {
  font-size: 13px;
}

.station-divider {
  height: 12px;
  border-left: 1px dashed #ddd;
  margin: 2px 0;
}

.time-col {
  width: 100px;
  vertical-align: top;
}

.time-value {
  font-size: 14px;
  font-weight: bold;
}

.time-divider {
  height: 12px;
  border-left: 1px dashed #ddd;
  margin: 2px 0;
}

.duration-col {
  width: 80px;
  vertical-align: top;
}

.duration-value {
  font-size: 13px;
}

.duration-label {
  font-size: 10px;
  color: #666;
}

.col-business { width: 70px; }
.col-preferred { width: 70px; }
.col-first { width: 60px; }
.col-second { width: 70px; }
.col-senior { width: 60px; }
.col-soft { width: 75px; }
.col-hard { width: 70px; }
.col-soft-seat { width: 50px; }
.col-hard-seat { width: 50px; }
.col-no-seat { width: 50px; }
.col-other { width: 50px; }
.col-remark { width: 60px; }

.waiting {
  color: #f57c00;
}

.book-btn {
  background: #1e88e5;
  color: #fff;
  border: none;
  padding: 5px 14px;
  border-radius: 3px;
  font-size: 12px;
  cursor: pointer;
}

.book-btn:hover:not(:disabled) {
  background: #1565c0;
}

.book-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
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