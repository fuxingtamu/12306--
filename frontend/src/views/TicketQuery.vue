<template>
  <div class="ticket-page">
    <!-- 头部 -->
    <header class="header">
      <router-link to="/" class="logo">12306旅客管理系统</router-link>
      <router-link to="/" class="back-link">返回首页</router-link>
    </header>

    <!-- 查询条件 -->
    <div class="query-info card">
      <el-row :gutter="20">
        <el-col :span="7">
          <el-form-item label="出发站">
            <StationSelector
              v-model="queryForm.startStation"
              placeholder="点击选择出发站"
              @change="onStartStationChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="1" class="swap-col">
          <el-button text @click="handleSwap" class="swap-btn" title="交换出发地和目的地">
            <el-icon :size="20"><ArrowRight /></el-icon>
          </el-button>
        </el-col>
        <el-col :span="7">
          <el-form-item label="到达站">
            <StationSelector
              v-model="queryForm.endStation"
              placeholder="点击选择到达站"
              @change="onEndStationChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="出发日期">
            <el-date-picker
              v-model="queryForm.travelDate"
              type="date"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              :disabled-date="disabledDate"
              @change="handleQuery"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <el-form-item label=" ">
            <el-button type="primary" @click="handleQuery" :disabled="!canQuery" class="query-btn">
              <el-icon><Search /></el-icon> 查询
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </div>

    <!-- 车次列表 -->
    <div class="train-list card" v-loading="loading">
      <div v-if="trains.length === 0 && !loading" class="no-result">
        <el-empty description="暂无符合条件的车次" />
      </div>
      <div v-else>
        <div class="train-item" v-for="train in trains" :key="train.trainId">
          <div class="train-info">
            <div class="train-code">{{ train.trainCode }}</div>
            <div class="train-type">{{ train.trainTypeName }}</div>
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
              <el-button
                type="primary"
                size="small"
                :disabled="!seat.hasTicket && !seat.canWaiting"
                @click="handleBuy(train, seat)"
              >
                {{ seat.hasTicket ? '预订' : '候补' }}
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 购票对话框 -->
    <el-dialog v-model="buyDialogVisible" title="购票" width="600px">
      <div v-if="selectedTrain">
        <p><strong>车次:</strong> {{ selectedTrain.trainCode }}</p>
        <p><strong>出发:</strong> {{ selectedTrain.startStationName }} {{ formatTime(selectedTrain.startTime) }}</p>
        <p><strong>到达:</strong> {{ selectedTrain.endStationName }} {{ formatTime(selectedTrain.endTime) }}</p>
        <p><strong>座位类型:</strong> {{ selectedSeat?.seatTypeName }}</p>
        <p><strong>票价:</strong> ¥{{ selectedSeat?.price }}</p>
        <hr />
        <h4>选择乘客</h4>
        <el-checkbox-group v-model="selectedPassengers">
          <el-checkbox
            v-for="passenger in passengers"
            :key="passenger.id"
            :label="passenger.id"
          >
            {{ passenger.name }} ({{ getPassengerTypeName(passenger.passengerType) }})
          </el-checkbox>
        </el-checkbox-group>
      </div>
      <template #footer>
        <el-button @click="buyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmBuy">确认购票</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { trainApi, passengerApi, orderApi } from '@/api'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { ArrowRight } from '@element-plus/icons-vue'
import StationSelector from '@/components/StationSelector.vue'

interface TrainVO {
  trainId: number
  trainCode: string
  trainTypeName: string
  startStationName: string
  endStationName: string
  startTime: string
  endTime: string
  totalTimeText: string
  travelDate: string
  hasSeatSelection: boolean
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
const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const trains = ref<TrainVO[]>([])
const passengers = ref<any[]>([])
const buyDialogVisible = ref(false)
const selectedTrain = ref<TrainVO | null>(null)
const selectedSeat = ref<SeatVO | null>(null)
const selectedPassengers = ref<number[]>([])

const queryForm = ref({
  startStation: '' as number | string,
  endStation: '' as number | string,
  travelDate: '',
  onlyHighSpeed: false
})

// 是否可以查询
const canQuery = computed(() => {
  return queryForm.value.startStation && queryForm.value.endStation && queryForm.value.travelDate
})

// 禁用今天之前的日期
const disabledDate = (date: Date) => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  return date < today
}

// 出发站变化
const onStartStationChange = () => {
  if (queryForm.value.startStation && queryForm.value.endStation && queryForm.value.travelDate) {
    handleQuery()
  }
}

// 到达站变化
const onEndStationChange = () => {
  if (queryForm.value.startStation && queryForm.value.endStation && queryForm.value.travelDate) {
    handleQuery()
  }
}

// 交换出发地和目的地
const handleSwap = () => {
  const temp = queryForm.value.startStation
  queryForm.value.startStation = queryForm.value.endStation
  queryForm.value.endStation = temp
}

// 初始化查询
onMounted(async () => {
  // 从URL参数获取查询条件
  if (route.query.start) queryForm.value.startStation = route.query.start as string
  if (route.query.end) queryForm.value.endStation = route.query.end as string
  if (route.query.date) queryForm.value.travelDate = route.query.date as string

  // 设置默认日期为今天
  if (!queryForm.value.travelDate) {
    const today = new Date()
    queryForm.value.travelDate = today.toISOString().split('T')[0]
  }

  // 加载乘客列表
  if (userStore.isLoggedIn()) {
    try {
      const res = await passengerApi.getList()
      passengers.value = res.data
    } catch (error) {
      console.error('加载乘客列表失败')
    }
  }

  // 如果已有出发地和目的地则查询
  if (queryForm.value.startStation && queryForm.value.endStation) {
    handleQuery()
  }
})

// 查询车次
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
      travelDate: queryForm.value.travelDate,
      onlyHighSpeed: queryForm.value.onlyHighSpeed
    })
    trains.value = res.data
  } catch (error: any) {
    // 响应拦截器已显示错误提示，这里额外记录详情
    console.error('查询车次失败:', error)
  } finally {
    loading.value = false
  }
}

// 格式化时间
const formatTime = (time: string) => {
  return time?.substring(0, 5) || ''
}

// 获取乘客类型名称
const getPassengerTypeName = (type: number) => {
  const types = ['', '成人', '儿童', '学生', '残军']
  return types[type] || '成人'
}

// 点击购票
const handleBuy = (train: TrainVO, seat: SeatVO) => {
  if (!userStore.isLoggedIn()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  if (passengers.value.length === 0) {
    ElMessage.warning('请先添加乘客信息')
    router.push('/passengers')
    return
  }

  selectedTrain.value = train
  selectedSeat.value = seat
  selectedPassengers.value = []
  buyDialogVisible.value = true
}

// 确认购票
const confirmBuy = async () => {
  if (selectedPassengers.value.length === 0) {
    ElMessage.warning('请选择乘客')
    return
  }

  try {
    const res = await orderApi.buy({
      trainId: selectedTrain.value!.trainId,
      travelDate: selectedTrain.value!.travelDate,
      startStation: selectedTrain.value!.startStationName,
      endStation: selectedTrain.value!.endStationName,
      seatTypeId: selectedSeat.value!.seatTypeId,
      passengers: selectedPassengers.value.map(id => ({ passengerId: id }))
    })
    buyDialogVisible.value = false
    ElMessage.success('下单成功，请在30分钟内完成支付')
    router.push(`/order/${res.data.orderId}`)
  } catch (error) {
    console.error('购票失败')
  }
}
</script>

<style lang="scss" scoped>
.ticket-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .query-info {
    margin-bottom: 20px;

    .swap-col {
      display: flex;
      align-items: flex-end;
      justify-content: center;
      padding-bottom: 0;

      .swap-btn {
        color: #409eff;
        &:hover {
          color: #66b1ff;
        }
      }
    }

    .query-btn {
      width: 100%;
    }
  }

  .train-list {
    .train-item {
      display: flex;
      justify-content: space-between;
      padding: 20px 0;
      border-bottom: 1px solid #eee;

      &:last-child {
        border-bottom: none;
      }

      .train-info {
        .train-code {
          font-size: 20px;
          font-weight: bold;
          color: #3b99fc;
        }
        .train-type {
          font-size: 12px;
          color: #999;
          margin-left: 10px;
        }
        .station-info {
          margin-top: 10px;
          .station {
            font-size: 16px;
          }
          .time {
            font-size: 18px;
            font-weight: bold;
            margin: 0 5px;
          }
          .arrow {
            color: #999;
            margin: 0 20px;
          }
        }
        .duration {
          color: #999;
          margin-top: 5px;
        }
      }

      .seat-info {
        display: flex;
        gap: 20px;

        .seat-type {
          text-align: center;
          min-width: 80px;

          .name {
            font-size: 14px;
          }
          .price {
            font-size: 16px;
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
      }
    }
  }
}
</style>