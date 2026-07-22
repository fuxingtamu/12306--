# API设计文档

> 版本：1.0 | 日期：2026-07-22 | 项目：12306旅客管理系统

## 1. 概述

系统提供RESTful API，所有接口（除登录/注册外）需携带JWT令牌。基础路径：`/api`。响应格式统一为 `Result<T>`：

```json
{ "code": 200, "message": "success", "data": {...} }
```

## 2. 端点列表

### 2.1 车票查询

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/trains/query | 查询车票 |

**请求体** (TrainQueryDTO)：
```json
{
  "startStation": "北京南",
  "endStation": "上海虹桥",
  "travelDate": "2026-07-25",
  "trainType": "G",
  "onlyAvailable": true
}
```

**响应** (`Result<List<TrainVO>>`)：
```json
{
  "code": 200,
  "data": [{
    "trainId": 1,
    "trainCode": "G101",
    "trainType": "G",
    "startStation": "北京南",
    "endStation": "上海虹桥",
    "startTime": "08:00",
    "endTime": "12:30",
    "duration": "4时30分",
    "seatList": [{
      "seatTypeCode": "FIRST",
      "seatTypeName": "一等座",
      "available": 23,
      "price": 933.00
    }]
  }]
}
```

**校验规则**：
- startStation / endStation / travelDate 必填
- travelDate 不可为过去日期
- onlyAvailable=true 时仅返回有余票的车次

---

### 2.2 乘车人管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/passengers | 分页查询乘车人列表 |
| GET | /api/passengers/{id} | 查询乘车人详情 |
| POST | /api/passengers | 添加乘车人 |
| PUT | /api/passengers/{id} | 修改乘车人 |
| DELETE | /api/passengers/{id} | 删除乘车人 |
| PUT | /api/passengers/{id}/default | 设为默认乘车人 |

**添加乘车人请求体** (PassengerDTO)：
```json
{
  "name": "张三",
  "idType": "ID_CARD",
  "idCard": "110101199001011234",
  "passengerType": "ADULT",
  "phone": "13800138000",
  "nationality": "中国",
  "birthDate": "1990-01-01",
  "gender": 1,
  "expireDate": "2030-01-01",
  "email": ""
}
```

**列表响应**（字段脱敏后）：
```json
{
  "code": 200,
  "data": {
    "records": [{
      "id": 1,
      "name": "张三",
      "idType": "ID_CARD",
      "idCard": "1101********1234",
      "phone": "138****8000",
      "passengerType": "ADULT",
      "isDefault": true,
      "isVerified": true
    }],
    "total": 5,
    "page": 1,
    "size": 10
  }
}
```

**业务约束**：
- 最多30名乘车人
- 同用户不可重复添加相同证件号
- 删除时检查未完成订单（PENDING/PAID状态），有则拒绝
- 证件号和手机号返回前脱敏（后端统一处理）

---

### 2.3 车站查询

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/stations | 查询车站列表 |
| GET | /api/stations/search?keyword={} | 模糊搜索车站 |

---

### 2.4 用户认证

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/auth/login | 用户登录 |
| POST | /api/auth/register | 用户注册 |

---

## 3. OpenAPI规范

完整OpenAPI 3.0规范文件参见 `passenger-api-v2.yaml`

## 4. 修订记录

| 版本 | 日期 | 修改内容 |
|------|------|---------|
| 1.0 | 2026-07-22 | 初版 |
