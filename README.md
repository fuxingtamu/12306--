# 12306旅客管理系统

## 项目简介

本项目是一个功能完整的12306旅客管理系统，功能范围完全对标现有12306官方APP，包括用户管理、旅客管理、车票预订、订单管理等核心模块。

## 技术栈

### 前端
- Vue 3 + TypeScript
- Element Plus UI组件库
- Pinia 状态管理
- Vue Router 路由管理
- Axios 网络请求
- Vite 构建工具

### 后端
- Java 17
- Spring Boot 3.x
- Spring Security + JWT 认证
- MyBatis-Plus ORM
- MySQL 8.0 数据库
- Redis 缓存

## 项目结构

```
12306旅客管理/
├── docs/                    # 文档目录
│   ├── 开发计划.md          # 开发计划
│   ├── requirements/        # 需求文档
│   ├── design/             # 设计文档
│   ├── api/                 # API文档
│   ├── test/                # 测试文档
│   └── deploy/              # 部署文档
├── frontend/                # 前端项目
├── backend/                 # 后端项目
├── database/                # 数据库脚本
│   ├── schema.sql          # 数据库结构
│   └── data.sql            # 初始化数据
└── README.md
```

## 功能模块

### 用户模块
- 用户注册/登录
- JWT认证
- 实名认证
- 个人信息管理

### 旅客管理模块
- 常用联系人管理
- 添加/编辑/删除旅客
- 旅客类型（成人/儿童/学生/残军）

### 车票查询模块
- 站站查询
- 车次查询
- 余票查询
- 筛选排序

### 购票模块
- 选座购票
- 在线支付模拟
- 购票限制

### 订单管理模块
- 未完成/已完成订单
- 改签功能
- 退票功能

### 候补购票模块
- 候补下单
- 候补状态查询
- 自动兑现

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Redis 6.0+

### 数据库初始化
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p railway_system < database/data.sql
```

### 后端启动
```bash
cd backend
mvn spring-boot:run
```

### 前端启动
```bash
cd frontend
npm install
npm run dev
```

## 部署说明

- 前端部署：Gitee Pages
- 后端部署：云服务器

## 开发进度

详见 [开发计划.md](docs/开发计划.md)

## License

MIT License