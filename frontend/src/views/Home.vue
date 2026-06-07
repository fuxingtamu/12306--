<template>
  <div class="mobile-home-page">
    <!-- 顶部城市选择和搜索 -->
    <div class="top-header">
      <div class="city-selector" @click="showCityPicker = true">
        <span class="city-name">{{ currentCity }}</span>
        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2">
          <polyline points="6,9 12,15 18,9"/>
        </svg>
      </div>
      <div class="search-box">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#999" stroke-width="2">
          <circle cx="11" cy="11" r="8"/>
          <path d="M21 21l-4.35-4.35"/>
        </svg>
        <input type="text" placeholder="积分会员" class="search-input" />
      </div>
      <div class="header-icons">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2">
          <path d="M12 1v22M17 5H9.5a3.5 3.5 0 000 7h5a3.5 3.5 0 010 7H6"/>
        </svg>
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2">
          <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
          <path d="M7 11V7a5 5 0 0110 0v4"/>
        </svg>
      </div>
    </div>

    <!-- 广告横幅 -->
    <div class="banner-section">
      <div class="banner-card">
        <div class="banner-content">
          <div class="banner-text">
            <span class="banner-tag">用白条</span>
            <span class="banner-title">抽大奖</span>
            <p class="banner-desc">全场免息享不停</p>
            <p class="banner-date">2026.5.28-2026.5.31</p>
          </div>
          <div class="banner-image">
            <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cute%20yellow%20duck%20character%20holding%20gift%20box%20with%20confetti%20celebration%20cartoon%20style%203D&image_size=square" alt="活动" class="banner-img" />
          </div>
        </div>
        <div class="banner-badge">广告</div>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 标签切换 -->
      <div class="tab-bar">
        <div
          v-for="tab in tabs"
          :key="tab.key"
          class="tab-item"
          :class="{ active: activeTab === tab.key }"
          @click="switchTab(tab.key)"
        >
          <div class="tab-icon" v-html="tab.icon"></div>
          <span class="tab-label">{{ tab.label }}</span>
        </div>
      </div>

      <!-- 车票查询表单 -->
      <div class="query-form" v-if="activeTab === 'train'">
        <div class="station-row">
          <div class="station-input start">
            <StationSelector
              v-model="queryForm.startStation"
              placeholder="出发地"
              @change="onStartStationChange"
            />
          </div>
          <button class="swap-btn" @click="swapStations">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#1890ff" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <path d="M8 12h8M13 9l3 3-3 3"/>
            </svg>
          </button>
          <div class="station-input end">
            <StationSelector
              v-model="queryForm.endStation"
              placeholder="目的地"
              @change="onEndStationChange"
            />
          </div>
        </div>

        <div class="date-row">
          <div class="date-input" @click="showDatePicker = true">
            <span class="date-value">{{ queryForm.travelDate || '请选择日期' }}</span>
            <span class="date-tag" v-if="queryForm.travelDate === todayDate">今天</span>
          </div>
          <label class="student-checkbox">
            <input type="checkbox" v-model="queryForm.isStudent" />
            <span>学生票</span>
          </label>
        </div>

        <button class="query-btn" @click="handleQuery">查询车票</button>

        <!-- 历史记录 -->
        <div class="history-row">
          <span
            v-for="(record, index) in historyRecords"
            :key="index"
            class="history-item"
            @click="useHistory(record)"
          >
            {{ record.start }}—{{ record.end }}
          </span>
          <span class="clear-history" @click="clearHistory">清除历史</span>
        </div>
      </div>

      <!-- 功能图标网格 -->
      <div class="service-grid">
        <div
          v-for="service in services"
          :key="service.key"
          class="service-item"
          @click="handleService(service.key)"
        >
          <div class="service-icon-wrapper">
            <div class="service-icon" v-html="service.icon"></div>
            <span v-if="service.badge" class="service-badge">{{ service.badge }}</span>
          </div>
          <span class="service-label">{{ service.label }}</span>
        </div>
      </div>

      <!-- 热门资讯 -->
      <div class="news-section">
        <div class="news-header">
          <h3 class="news-title">热门资讯</h3>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#999" stroke-width="2">
            <polyline points="9,18 15,12 9,6"/>
          </svg>
        </div>
        <div class="news-item" @click="viewNews(0)">
          <span class="news-highlight">"穿越南北八小时"！</span>
          <span class="news-desc">乘坐京广高铁，纵享沿途风光！【乘火车...</span>
        </div>
      </div>
    </div>

    <!-- 底部导航栏 -->
    <nav class="bottom-nav">
      <div
        v-for="item in navItems"
        :key="item.key"
        class="nav-item"
        :class="{ active: currentNav === item.key }"
        @click="switchNav(item.key)"
      >
        <div class="nav-icon" v-html="item.icon"></div>
        <span class="nav-label">{{ item.label }}</span>
      </div>
    </nav>

    <!-- 日期选择弹窗 -->
    <div v-if="showDatePicker" class="picker-overlay" @click="showDatePicker = false">
      <div class="picker-modal date-picker-modal" @click.stop>
        <div class="picker-header">
          <span class="picker-title">选择日期</span>
          <button class="picker-close" @click="showDatePicker = false">取消</button>
        </div>
        <div class="date-picker-content">
          <div class="date-month-section">
            <div class="date-nav">
              <button class="nav-btn" @click="changeMonth(-1)">&lt;</button>
              <span class="current-month">{{ pickerYear }}年{{ pickerMonth }}月</span>
              <button class="nav-btn" @click="changeMonth(1)">&gt;</button>
            </div>
            <div class="date-weekday">
              <span v-for="day in ['日','一','二','三','四','五','六']" :key="day">{{ day }}</span>
            </div>
            <div class="date-grid">
              <div
                v-for="(day, index) in calendarDays"
                :key="index"
                class="date-cell"
                :class="{
                  empty: !day,
                  today: day && isToday(day),
                  selected: day && isSelectedDate(day),
                  disabled: day && isPastDate(day)
                }"
                @click="day && !isPastDate(day) && selectDate(day)"
              >
                {{ day || '' }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import StationSelector from '@/components/StationSelector.vue'

const router = useRouter()
const currentCity = ref('北京')
const showCityPicker = ref(false)
const activeTab = ref('train')
const currentNav = ref('home')
const showDatePicker = ref(false)

// 车站选择后的回调 - 保存车站名称
const onStartStationChange = (station: any) => {
  if (station) {
    queryForm.value.startStation = station.stationName
  }
}

const onEndStationChange = (station: any) => {
  if (station) {
    queryForm.value.endStation = station.stationName
  }
}

// 日期选择器状态
const pickerYear = ref(new Date().getFullYear())
const pickerMonth = ref(new Date().getMonth() + 1)
const selectedDate = ref<Date | null>(null)

// 日期选择器计算属性
const calendarDays = computed(() => {
  const year = pickerYear.value
  const month = pickerMonth.value
  const firstDay = new Date(year, month - 1, 1).getDay()
  const daysInMonth = new Date(year, month, 0).getDate()
  const days: (number | null)[] = []

  // 填充空白
  for (let i = 0; i < firstDay; i++) {
    days.push(null)
  }
  // 填充日期
  for (let i = 1; i <= daysInMonth; i++) {
    days.push(i)
  }
  return days
})

const isToday = (day: number) => {
  const today = new Date()
  return day === today.getDate() && pickerMonth.value === today.getMonth() + 1 && pickerYear.value === today.getFullYear()
}

const isSelectedDate = (day: number) => {
  if (!selectedDate.value) return false
  return day === selectedDate.value.getDate() &&
    pickerMonth.value === selectedDate.value.getMonth() + 1 &&
    pickerYear.value === selectedDate.value.getFullYear()
}

const isPastDate = (day: number) => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  const checkDate = new Date(pickerYear.value, pickerMonth.value - 1, day)
  return checkDate < today
}

const changeMonth = (delta: number) => {
  let newMonth = pickerMonth.value + delta
  let newYear = pickerYear.value

  if (newMonth > 12) {
    newMonth = 1
    newYear++
  } else if (newMonth < 1) {
    newMonth = 12
    newYear--
  }

  pickerYear.value = newYear
  pickerMonth.value = newMonth
}

const selectDate = (day: number) => {
  selectedDate.value = new Date(pickerYear.value, pickerMonth.value - 1, day)
  handleDateConfirm(selectedDate.value)
}

const todayDate = computed(() => {
  const now = new Date()
  return `${now.getMonth() + 1}月${now.getDate()}日`
})

const queryForm = ref({
  startStation: '',
  endStation: '',
  travelDate: todayDate.value,
  isStudent: false
})

const historyRecords = ref([
  { start: '长沙', end: '乌鲁木齐' },
  { start: '徐州东', end: '长沙' },
  { start: '徐州东', end: '' }
])

const tabs = [
  {
    key: 'train',
    label: '火车票',
    icon: `<svg width="28" height="28" viewBox="0 0 24 24" fill="none">
      <rect x="4" y="3" width="16" height="14" rx="2" stroke="#1890ff" stroke-width="1.5"/>
      <path d="M4 10h16M8 17l-2 4M16 17l2 4" stroke="#1890ff" stroke-width="1.5"/>
    </svg>`
  },
  {
    key: 'flight',
    label: '飞机票',
    icon: `<svg width="28" height="28" viewBox="0 0 24 24" fill="none">
      <path d="M21 16v-2l-8-5V3.5a1.5 1.5 0 00-3 0V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5l8 2.5z" stroke="#666" stroke-width="1.5"/>
    </svg>`
  },
  {
    key: 'bus',
    label: '汽车票',
    icon: `<svg width="28" height="28" viewBox="0 0 24 24" fill="none">
      <path d="M8 6v6M16 6v6M3 10h18M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" stroke="#666" stroke-width="1.5"/>
      <circle cx="7.5" cy="17.5" r="1.5" stroke="#666" stroke-width="1.5"/>
      <circle cx="16.5" cy="17.5" r="1.5" stroke="#666" stroke-width="1.5"/>
    </svg>`
  }
]

const services = [
  {
    key: 'screen',
    label: '车站大屏',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><rect x="2" y="3" width="20" height="14" rx="2" stroke="#1890ff" stroke-width="1.5"/><line x1="8" y1="21" x2="16" y2="21" stroke="#1890ff" stroke-width="1.5"/><line x1="12" y1="17" x2="12" y2="21" stroke="#1890ff" stroke-width="1.5"/></svg>`,
    badge: ''
  },
  {
    key: 'frequent',
    label: '计次·定期票',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><rect x="3" y="4" width="18" height="16" rx="2" stroke="#52c41a" stroke-width="1.5"/><line x1="3" y1="10" x2="21" y2="10" stroke="#52c41a" stroke-width="1.5"/><text x="7" y="16" font-size="8" fill="#52c41a" font-weight="bold">N</text></svg>`,
    badge: ''
  },
  {
    key: 'ecard',
    label: '铁路e卡通',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><rect x="2" y="5" width="20" height="14" rx="2" stroke="#faad14" stroke-width="1.5"/><circle cx="8.5" cy="12" r="2" stroke="#faad14" stroke-width="1.5"/><path d="M14 10h4M14 13h2" stroke="#faad14" stroke-width="1.5"/></svg>`,
    badge: ''
  },
  {
    key: 'schedule',
    label: '时刻表',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><rect x="3" y="4" width="18" height="16" rx="2" stroke="#722ed1" stroke-width="1.5"/><line x1="3" y1="10" x2="21" y2="10" stroke="#722ed1" stroke-width="1.5"/><text x="7" y="16" font-size="7" fill="#722ed1" font-weight="bold">时刻</text></svg>`,
    badge: ''
  },
  {
    key: 'service',
    label: '温馨服务',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><path d="M20.84 4.61a5.5 5.5 0 00-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 00-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 000-7.78z" stroke="#eb2f96" stroke-width="1.5"/></svg>`,
    badge: ''
  },
  {
    key: 'airrail',
    label: '空铁联运',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><path d="M21 16v-2l-8-5V3.5a1.5 1.5 0 00-3 0V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5l8 2.5z" stroke="#40a9ff" stroke-width="1.5"/></svg>`,
    badge: ''
  },
  {
    key: 'elderly',
    label: '敬老版',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><path d="M20.84 4.61a5.5 5.5 0 00-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 00-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 000-7.78z" stroke="#52c41a" stroke-width="1.5"/></svg>`,
    badge: '适老'
  },
  {
    key: 'hotel',
    label: '酒店住宿',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><path d="M3 21V9m0 12h18M3 9l9-6 9 6M3 9v6m18-6v6M9 21v-6h6v6" stroke="#95de64" stroke-width="1.5"/></svg>`,
    badge: ''
  },
  {
    key: 'car',
    label: '租车·约车',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><path d="M5 17h14M5 17a2 2 0 01-2-2V9a2 2 0 012-2h14a2 2 0 012 2v6a2 2 0 01-2 2M5 17l1.5-6h11L19 17" stroke="#ffa940" stroke-width="1.5"/><circle cx="7.5" cy="17" r="1.5" stroke="#ffa940" stroke-width="1.5"/><circle cx="16.5" cy="17" r="1.5" stroke="#ffa940" stroke-width="1.5"/></svg>`,
    badge: ''
  },
  {
    key: 'tourism',
    label: '铁路旅游',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><path d="M3 20.5V11l9-7 9 7v9.5a1.5 1.5 0 01-1.5 1.5H4.5A1.5 1.5 0 013 20.5z" stroke="#36cfc9" stroke-width="1.5"/><polyline points="10,20 10,14 14,14 14,20" stroke="#36cfc9" stroke-width="1.5"/></svg>`,
    badge: '推荐'
  },
  {
    key: 'food',
    label: '餐饮·特产',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><path d="M18 8h1a4 4 0 010 8h-1M2 8h16v9a4 4 0 01-4 4H6a4 4 0 01-4-4V8z" stroke="#73d13d" stroke-width="1.5"/><line x1="6" y1="1" x2="6" y2="4" stroke="#73d13d" stroke-width="1.5"/><line x1="10" y1="1" x2="10" y2="4" stroke="#73d13d" stroke-width="1.5"/><line x1="14" y1="1" x2="14" y2="4" stroke="#73d13d" stroke-width="1.5"/></svg>`,
    badge: ''
  },
  {
    key: 'ship',
    label: '汽车·船票',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><path d="M8 6v6M16 6v6M3 10h18M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" stroke="#69c0ff" stroke-width="1.5"/><circle cx="7.5" cy="17.5" r="1.5" stroke="#69c0ff" stroke-width="1.5"/><circle cx="16.5" cy="17.5" r="1.5" stroke="#69c0ff" stroke-width="1.5"/></svg>`,
    badge: ''
  },
  {
    key: 'mall',
    label: '铁路商城',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><circle cx="9" cy="21" r="1" stroke="#597ef7" stroke-width="1.5"/><circle cx="20" cy="21" r="1" stroke="#597ef7" stroke-width="1.5"/><path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 002-1.61L23 6H6" stroke="#597ef7" stroke-width="1.5"/></svg>`,
    badge: ''
  },
  {
    key: 'invoice',
    label: '电子发票',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z" stroke="#13c2c2" stroke-width="1.5"/><polyline points="14,2 14,8 20,8" stroke="#13c2c2" stroke-width="1.5"/><line x1="16" y1="13" x2="8" y2="13" stroke="#13c2c2" stroke-width="1.5"/><line x1="16" y1="17" x2="8" y2="17" stroke="#13c2c2" stroke-width="1.5"/></svg>`,
    badge: ''
  },
  {
    key: 'insurance',
    label: '出行保险',
    icon: `<svg width="32" height="32" viewBox="0 0 24 24" fill="none"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" stroke="#ff85c0" stroke-width="1.5"/></svg>`,
    badge: ''
  }
]

const navItems = [
  {
    key: 'home',
    label: '首页',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none"><path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z" stroke="currentColor" stroke-width="2"/><polyline points="9,22 9,12 15,12 15,22" stroke="currentColor" stroke-width="2"/></svg>`
  },
  {
    key: 'travel',
    label: '出行服务',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none"><rect x="4" y="3" width="16" height="14" rx="2" stroke="currentColor" stroke-width="2"/><path d="M4 10h16M8 17l-2 4M16 17l2 4" stroke="currentColor" stroke-width="2"/></svg>`
  },
  {
    key: 'orders',
    label: '订单',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z" stroke="currentColor" stroke-width="2"/><polyline points="14,2 14,8 20,8" stroke="currentColor" stroke-width="2"/></svg>`
  },
  {
    key: 'member',
    label: '铁路会员',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none"><polygon points="12,2 15.09,8.26 22,9.27 17,14.14 18.18,21.02 12,17.77 5.82,21.02 7,14.14 2,9.27 8.91,8.26 12,2" stroke="currentColor" stroke-width="2"/></svg>`
  },
  {
    key: 'my',
    label: '我的',
    icon: `<svg width="24" height="24" viewBox="0 0 24 24" fill="none"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" stroke="currentColor" stroke-width="2"/><circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2"/></svg>`
  }
]

const switchTab = (tab: string) => {
  activeTab.value = tab
}

const swapStations = () => {
  const temp = queryForm.value.startStation
  queryForm.value.startStation = queryForm.value.endStation
  queryForm.value.endStation = temp
}

const handleQuery = () => {
  if (!queryForm.value.startStation) {
    ElMessage.warning('请选择出发地')
    return
  }
  if (!queryForm.value.endStation) {
    ElMessage.warning('请选择目的地')
    return
  }

  router.push({
    path: '/ticket',
    query: {
      start: queryForm.value.startStation,
      end: queryForm.value.endStation,
      date: formatDateForQuery(queryForm.value.travelDate)
    }
  })
}

// 格式化日期为 YYYY-MM-DD
const formatDateForQuery = (dateStr: string) => {
  // dateStr 格式可能是 "6月1日" 或 "06月01日"
  const match = dateStr.match(/(\d+)月(\d+)日/)
  if (match) {
    const month = match[1].padStart(2, '0')
    const day = match[2].padStart(2, '0')
    const year = new Date().getFullYear()
    return `${year}-${month}-${day}`
  }
  return dateStr
}

const useHistory = (record: any) => {
  queryForm.value.startStation = record.start
  queryForm.value.endStation = record.end
}

const clearHistory = () => {
  historyRecords.value = []
  ElMessage.success('历史记录已清除')
}

const handleService = (key: string) => {
  const serviceMap: Record<string, string> = {
    screen: '车站大屏',
    frequent: '计次定期票',
    ecard: '铁路e卡通',
    schedule: '时刻表',
    service: '温馨服务',
    airrail: '空铁联运',
    elderly: '敬老版',
    hotel: '酒店住宿',
    car: '租车约车',
    tourism: '铁路旅游',
    food: '餐饮特产',
    ship: '汽车船票',
    mall: '铁路商城',
    invoice: '电子发票',
    insurance: '出行保险'
  }

  if (key === 'orders') {
    router.push('/orders')
  } else {
    ElMessage.info(`${serviceMap[key] || key} 功能开发中...`)
  }
}

const viewNews = (index: number) => {
  ElMessage.info('正在打开资讯详情...')
}

const switchNav = (key: string) => {
  currentNav.value = key

  const routeMap: Record<string, string> = {
    home: '/',
    travel: '/',
    orders: '/orders',
    member: '/user',
    my: '/user'
  }

  if (routeMap[key]) {
    router.push(routeMap[key])
  }
}

const handleDateConfirm = (date: Date) => {
  const month = date.getMonth() + 1
  const day = date.getDate()
  queryForm.value.travelDate = `${month}月${day}日`
  showDatePicker.value = false
}
</script>

<style lang="scss" scoped>
.mobile-home-page {
  min-height: 100vh;
  background: #f5f7fa;
  padding-bottom: 70px;
}

.top-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: white;
  padding: 10px 12px;
  display: flex;
  align-items: center;
  gap: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

  .city-selector {
    display: flex;
    align-items: center;
    gap: 2px;
    cursor: pointer;
    padding: 4px 8px;

    .city-name {
      font-size: 15px;
      color: #333;
      font-weight: 600;
    }
  }

  .search-box {
    flex: 1;
    display: flex;
    align-items: center;
    background: #f5f7fa;
    border-radius: 18px;
    padding: 8px 14px;
    gap: 8px;

    svg {
      flex-shrink: 0;
      opacity: 0.6;
    }

    .search-input {
      flex: 1;
      border: none;
      outline: none;
      background: transparent;
      font-size: 14px;
      color: #333;

      &::placeholder {
        color: #c0c4cc;
      }
    }
  }

  .header-icons {
    display: flex;
    gap: 12px;

    svg {
      cursor: pointer;
      opacity: 0.7;

      &:active {
        opacity: 1;
      }
    }
  }
}

.banner-section {
  margin: 10px 12px;

  .banner-card {
    background: linear-gradient(135deg, #ff6b35 0%, #ff4444 50%, #ff1493 100%);
    border-radius: 12px;
    padding: 16px;
    position: relative;
    overflow: hidden;

    .banner-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .banner-text {
      color: white;

      .banner-tag {
        display: inline-block;
        background: rgba(255, 255, 255, 0.25);
        padding: 3px 8px;
        border-radius: 4px;
        font-size: 12px;
        font-weight: 500;
        margin-right: 6px;
      }

      .banner-title {
        font-size: 22px;
        font-weight: 700;
        letter-spacing: 1px;
      }

      .banner-desc {
        font-size: 13px;
        margin: 6px 0 4px;
        opacity: 0.95;
      }

      .banner-date {
        font-size: 11px;
        opacity: 0.85;
      }
    }

    .banner-image {
      width: 90px;
      height: 80px;

      .banner-img {
        width: 100%;
        height: 100%;
        object-fit: contain;
      }
    }

    .banner-badge {
      position: absolute;
      top: 8px;
      right: 8px;
      background: rgba(0, 0, 0, 0.2);
      color: white;
      padding: 2px 8px;
      border-radius: 8px;
      font-size: 11px;
    }
  }
}

.main-content {
  background: white;
  margin: 10px 12px;
  border-radius: 12px;
  overflow: hidden;
}

.tab-bar {
  display: flex;
  border-bottom: 1px solid #f0f0f0;

  .tab-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 12px 8px;
    gap: 4px;
    cursor: pointer;
    transition: all 0.2s;
    position: relative;

    &.active {
      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 40px;
        height: 3px;
        background: #1890ff;
        border-radius: 2px;
      }

      .tab-icon {
        :deep(svg [stroke]) {
          stroke: #1890ff;
        }
      }

      .tab-label {
        color: #1890ff;
        font-weight: 600;
      }
    }

    .tab-icon {
      width: 28px;
      height: 28px;
    }

    .tab-label {
      font-size: 13px;
      color: #666;
    }
  }
}

.query-form {
  padding: 16px;

  .station-row {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 12px;

    .station-input {
      flex: 1;
      background: #fafafa;
      border-radius: 8px;
      padding: 14px 12px;

      .station-field {
        width: 100%;
        border: none;
        outline: none;
        background: transparent;
        font-size: 18px;
        font-weight: 600;
        color: #333;

        &::placeholder {
          color: #ccc;
          font-weight: 400;
        }
      }
    }

    .swap-btn {
      width: 36px;
      height: 36px;
      border-radius: 50%;
      background: #f0f7ff;
      border: none;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s;

      &:active {
        transform: rotate(180deg);
        background: #e6f7ff;
      }
    }
  }

  .date-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    padding: 12px;
    background: #fafafa;
    border-radius: 8px;

    .date-input {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;

      .date-value {
        font-size: 16px;
        color: #333;
        font-weight: 500;
      }

      .date-tag {
        background: #fff7e6;
        color: #ff7e00;
        padding: 2px 8px;
        border-radius: 10px;
        font-size: 12px;
        font-weight: 500;
      }
    }

    .student-checkbox {
      display: flex;
      align-items: center;
      gap: 6px;
      cursor: pointer;
      font-size: 14px;
      color: #666;

      input[type="checkbox"] {
        width: 18px;
        height: 18px;
        accent-color: #1890ff;
      }
    }
  }

  .query-btn {
    width: 100%;
    height: 48px;
    background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
    color: white;
    border: none;
    border-radius: 24px;
    font-size: 17px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s;

    &:active {
      opacity: 0.9;
      transform: scale(0.98);
    }
  }

  .history-row {
    margin-top: 12px;
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    font-size: 13px;

    .history-item {
      color: #999;
      cursor: pointer;
      padding: 4px 0;

      &:hover {
        color: #1890ff;
      }
    }

    .clear-history {
      color: #ff4d4f;
      cursor: pointer;
      padding: 4px 0;
    }
  }
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px 8px;
  padding: 20px 12px;
  background: white;
  margin: 10px 12px;
  border-radius: 12px;

  .service-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 6px;
    cursor: pointer;
    transition: all 0.2s;

    &:active {
      transform: scale(0.95);
    }

    .service-icon-wrapper {
      position: relative;
      width: 44px;
      height: 44px;
      display: flex;
      align-items: center;
      justify-content: center;

      .service-icon {
        width: 100%;
        height: 100%;
      }

      .service-badge {
        position: absolute;
        top: -4px;
        right: -4px;
        background: linear-gradient(135deg, #ff4444, #ff6b35);
        color: white;
        font-size: 9px;
        padding: 1px 4px;
        border-radius: 6px;
        font-weight: 600;
      }
    }

    .service-label {
      font-size: 12px;
      color: #333;
      text-align: center;
      line-height: 1.3;
    }
  }
}

.news-section {
  background: white;
  margin: 10px 12px;
  border-radius: 12px;
  padding: 16px;

  .news-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    .news-title {
      font-size: 16px;
      font-weight: 700;
      color: #333;
      margin: 0;
    }

    svg {
      cursor: pointer;
    }
  }

  .news-item {
    display: flex;
    align-items: flex-start;
    gap: 8px;
    cursor: pointer;
    padding: 8px 0;

    .news-highlight {
      color: #333;
      font-size: 14px;
      font-weight: 600;
      flex-shrink: 0;
    }

    .news-desc {
      color: #999;
      font-size: 13px;
      line-height: 1.4;
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
  padding: 8px 0;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.08);
  z-index: 1000;

  .nav-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 2px;
    cursor: pointer;
    padding: 4px 12px;
    transition: all 0.2s;

    &.active {
      .nav-icon {
        color: #1890ff;
      }

      .nav-label {
        color: #1890ff;
        font-weight: 600;
      }
    }

    .nav-icon {
      width: 24px;
      height: 24px;
      color: #999;
    }

    .nav-label {
      font-size: 11px;
      color: #999;
    }
  }
}

.picker-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 2000;
  display: flex;
  align-items: flex-end;
  animation: fadeIn 0.2s ease;
}

.picker-modal {
  width: 100%;
  background: white;
  border-radius: 16px 16px 0 0;
  animation: slideUp 0.3s ease;
  max-height: 60vh;
  overflow-y: auto;

  .picker-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
    border-bottom: 1px solid #f0f0f0;

    .picker-title {
      font-size: 16px;
      font-weight: 600;
      color: #333;
    }

    .picker-close {
      background: none;
      border: none;
      color: #999;
      font-size: 14px;
      cursor: pointer;
      padding: 4px 8px;
    }
  }

  .city-list {
    padding: 8px 0;

    .city-item {
      padding: 14px 16px;
      font-size: 15px;
      color: #333;
      cursor: pointer;
      transition: all 0.2s;

      &:active {
        background: #f5f5f5;
      }

      &.selected {
        color: #1890ff;
        font-weight: 600;
        background: #f0f7ff;
      }
    }
  }
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from { transform: translateY(100%); }
  to { transform: translateY(0); }
}

// 日期选择器样式
.date-picker-modal {
  padding-bottom: 20px;

  .date-picker-content {
    padding: 0 16px;
  }

  .date-month-section {
    .date-nav {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 12px 0;

      .nav-btn {
        width: 36px;
        height: 36px;
        border: none;
        background: #f5f7fa;
        border-radius: 50%;
        cursor: pointer;
        font-size: 16px;
        color: #666;

        &:active {
          background: #e6f7ff;
        }
      }

      .current-month {
        font-size: 16px;
        font-weight: 600;
        color: #333;
      }
    }

    .date-weekday {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      text-align: center;
      padding: 8px 0;
      border-bottom: 1px solid #f0f0f0;

      span {
        font-size: 12px;
        color: #999;
      }
    }

    .date-grid {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      gap: 4px;
      padding: 8px 0;

      .date-cell {
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #333;
        border-radius: 50%;
        cursor: pointer;

        &.empty {
          cursor: default;
        }

        &.today {
          color: #1890ff;
          font-weight: 600;
        }

        &.selected {
          background: #1890ff;
          color: white;
          font-weight: 600;
        }

        &.disabled {
          color: #ccc;
          cursor: not-allowed;
        }

        &:active:not(.empty):not(.disabled) {
          opacity: 0.7;
        }
      }
    }
  }
}
</style>
