<template>
  <div class="order-detail-page">
    <header class="header">
      <router-link to="/" class="logo">12306旅客管理系统</router-link>
      <router-link to="/orders" class="back-link">返回订单列表</router-link>
    </header>

    <div class="detail-card card" v-loading="loading">
      <h2>订单详情</h2>
      <div v-if="order">
        <div class="order-info">
          <p><strong>订单号:</strong> {{ order.orderNo }}</p>
          <p><strong>车次:</strong> {{ order.trainCode }}</p>
          <p><strong>出发:</strong> {{ order.startStationName }} {{ order.startTime }}</p>
          <p><strong>到达:</strong> {{ order.endStationName }} {{ order.endTime }}</p>
          <p><strong>出行日期:</strong> {{ order.travelDate }}</p>
          <p>
            <strong>订单状态:</strong>
            <span class="status" :class="getStatusClass(order.orderStatus)">{{ order.orderStatusName }}</span>
          </p>
          <p><strong>订单金额:</strong> ¥{{ order.totalPrice }}</p>
          <p v-if="order.payPrice"><strong>实付金额:</strong> ¥{{ order.payPrice }}</p>
        </div>

        <hr />

        <div class="passengers-info">
          <h3>乘客信息</h3>
          <el-table :data="order.items" stripe>
            <el-table-column prop="passengerName" label="姓名" />
            <el-table-column prop="passengerIdCard" label="身份证号" />
            <el-table-column prop="seatTypeName" label="座位类型" />
            <el-table-column prop="seatNumber" label="座位号" />
            <el-table-column prop="price" label="票价">
              <template #default="{ row }">¥{{ row.price }}</template>
            </el-table-column>
            <el-table-column label="状态">
              <template #default="{ row }">
                {{ getTicketStatusName(row.ticketStatus) }}
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="{ row }">
                <el-button size="small" type="warning" v-if="order.orderStatus === 1 && row.ticketStatus === 1" @click="handleRefund(row)">
                  退票
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="actions">
          <el-button type="primary" v-if="order.orderStatus === 0" @click="handlePay">立即支付</el-button>
          <el-button type="danger" v-if="order.orderStatus === 0" @click="handleCancel">取消订单</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { orderApi } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'

interface OrderDetail {
  orderId: number
  orderNo: string
  trainCode: string
  startStationName: string
  endStationName: string
  startTime: string
  endTime: string
  travelDate: string
  orderStatus: number
  orderStatusName: string
  totalPrice: number
  payPrice: number
  items: any[]
}

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const order = ref<OrderDetail | null>(null)

onMounted(async () => {
  await loadOrder()
})

// 加载订单详情
const loadOrder = async () => {
  loading.value = true
  try {
    const res = await orderApi.getDetail(Number(route.params.id))
    order.value = res.data
  } catch (error) {
    ElMessage.error('加载订单失败')
    router.push('/orders')
  } finally {
    loading.value = false
  }
}

// 支付订单
const handlePay = async () => {
  try {
    await ElMessageBox.confirm('确认支付？', '支付确认', { type: 'info' })
    await orderApi.pay(order.value!.orderId, 1)
    ElMessage.success('支付成功')
    await loadOrder()
  } catch (error) {
    console.error('支付失败')
  }
}

// 取消订单
const handleCancel = async () => {
  try {
    await ElMessageBox.confirm('确认取消订单？', '取消确认', { type: 'warning' })
    await orderApi.cancel(order.value!.orderId)
    ElMessage.success('订单已取消')
    router.push('/orders')
  } catch (error) {
    console.error('取消失败')
  }
}

// 退票
const handleRefund = async (item: any) => {
  try {
    await ElMessageBox.confirm('确认退票？退票手续费为5%', '退票确认', { type: 'warning' })
    await orderApi.refund(order.value!.orderId, item.itemId)
    ElMessage.success('退票成功')
    await loadOrder()
  } catch (error) {
    console.error('退票失败')
  }
}

// 状态样式
const getStatusClass = (status: number) => {
  const classes = ['pending', 'paid', 'cancelled', '', 'refunded']
  return classes[status] || ''
}

// 车票状态名称
const getTicketStatusName = (status: number) => {
  const names = ['待支付', '已支付', '已退票', '已改签']
  return names[status] || '未知'
}
</script>

<style lang="scss" scoped>
.order-detail-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;

  .detail-card {
    h2 {
      color: #3b99fc;
      margin-bottom: 20px;
    }

    .order-info {
      p {
        margin-bottom: 10px;
      }
      .status {
        font-weight: bold;
        &.pending { color: #ff7e00; }
        &.paid { color: #67c23a; }
        &.cancelled { color: #909399; }
        &.refunded { color: #f56c6c; }
      }
    }

    .passengers-info {
      h3 {
        margin-bottom: 15px;
      }
    }

    .actions {
      margin-top: 20px;
      display: flex;
      gap: 10px;
    }
  }
}
</style>