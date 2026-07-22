import { request } from '@/utils/request'

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
