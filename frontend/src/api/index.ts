import { request } from '@/utils/request'

// 用户相关API
export const authApi = {
  // 登录
  login(data: { phone: string; password: string }) {
    return request.post('/auth/login', data)
  },
  // 注册
  register(data: { phone: string; password: string; verifyCode: string; nickname?: string }) {
    return request.post('/auth/register', data)
  },
  // 发送验证码
  sendCode(phone: string) {
    return request.post('/auth/send-code', null, { params: { phone } })
  },
  // 获取用户信息
  getUserInfo() {
    return request.get('/auth/user-info')
  },
  // 更新用户信息
  updateUserInfo(data: { nickname?: string; avatar?: string; password?: string }) {
    return request.put('/auth/user-info', data)
  },
  // 实名认证
  verifyRealName(realName: string, idCard: string) {
    return request.post('/auth/verify', null, { params: { realName, idCard } })
  }
}

// 旅客相关API
export const passengerApi = {
  // 获取旅客列表
  getList() {
    return request.get('/passengers')
  },
  // 添加旅客
  add(data: {
    lastName: string
    firstName: string
    idType: string
    idCard: string
    passengerType?: number
    phone?: string
    gender?: number
  }) {
    return request.post('/passengers', data)
  },
  // 更新旅客
  update(id: number, data: object) {
    return request.put(`/passengers/${id}`, data)
  },
  // 删除旅客
  delete(id: number) {
    return request.delete(`/passengers/${id}`)
  },
  // 获取旅客详情
  getDetail(id: number) {
    return request.get(`/passengers/${id}`)
  }
}

// 车站相关API
export const stationApi = {
  // 搜索车站
  search(keyword: string) {
    return request.get('/stations/search', { keyword })
  },
  // 获取热门车站
  getHot() {
    return request.get('/stations/hot')
  },
  // 获取所有车站
  getAll() {
    return request.get('/stations/all')
  }
}

// 车次相关API
export const trainApi = {
  // 查询车次
  query(data: {
    startStation: string
    endStation: string
    travelDate: string
    trainType?: number
    onlyHighSpeed?: boolean
  }) {
    return request.post('/trains/query', data)
  },
  // 获取车次详情
  getDetail(trainId: number, travelDate: string) {
    return request.get(`/trains/${trainId}`, { travelDate })
  },
  // 根据车次号查询
  queryByCode(trainCode: string) {
    return request.get(`/trains/code/${trainCode}`)
  }
}

// 订单相关API
export const orderApi = {
  // 购票
  buy(data: {
    trainId: number
    travelDate: string
    startStation: string
    endStation: string
    seatTypeId: number
    passengers: { passengerId: number; seatNumber?: string }[]
  }) {
    return request.post('/orders/buy', data)
  },
  // 支付订单
  pay(orderId: number, payType: number) {
    return request.post(`/orders/${orderId}/pay`, null, { params: { payType } })
  },
  // 取消订单
  cancel(orderId: number) {
    return request.post(`/orders/${orderId}/cancel`)
  },
  // 获取订单列表
  getList(status?: number) {
    return request.get('/orders', { status })
  },
  // 获取订单详情
  getDetail(orderId: number) {
    return request.get(`/orders/${orderId}`)
  },
  // 退票
  refund(orderId: number, itemId: number) {
    return request.post(`/orders/${orderId}/refund/${itemId}`)
  },
  // 改签
  change(orderId: number, data: object) {
    return request.post(`/orders/${orderId}/change`, data)
  }
}