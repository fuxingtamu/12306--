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

## 项目结构（按SDLC生命周期组织）

```
12306旅客管理/
├── 01-requirements/              # 需求阶段
│   ├── 软件需求规格说明书.md       # SRS
│   └── user-stories/             # User Story
├── 02-architecture/              # 架构设计
│   ├── 软件架构设计文档.md         # SAD
│   └── adr/                      # 架构决策记录
├── 03-detailed-design/           # 详细设计
│   ├── 数据库设计文档.md
│   ├── API设计文档.md
│   └── uml/                      # UML图集（用例/顺序/类/状态/ER/组件/架构/部署/活动）
├── 04-implementation/            # 实现阶段
├── 05-testing/                   # 测试阶段
│   ├── 测试计划.md
│   ├── 测试用例/
│   └── 测试报告.md
├── 06-deployment/                # 部署阶段
│   └── 部署手册.md
│   ├── backend/                  # 后端源码（Spring Boot）
│   └── frontend/                 # 前端源码（Vue 3）
├── database/                     # 数据库脚本与原始数据
├── cases/                        # AI辅助开发教学案例
└── README.md
```

## 功能模块

### 已实现
- 车票查询（站站查询、余票查询、筛选排序）
- 乘车人管理（添加/修改/删除/查询，8种证件类型支持）
- 车站查询（模糊搜索）
- 用户认证（注册/登录、JWT令牌）
- 订单管理（创建/查询）

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Redis 7.0+

### 数据库初始化
```bash
mysql -u root -p -e "CREATE DATABASE railway DEFAULT CHARSET utf8mb4;"
mysql -u root -p railway < database/schema.sql
mysql -u root -p railway < database/stations_data.sql
mysql -u root -p railway < database/trains_import.sql
mysql -u root -p railway < database/train_stops_import.sql
```

### 后端启动
```bash
cd 04-implementation/backend
mvn spring-boot:run
```

### 前端启动
```bash
cd 04-implementation/frontend
npm install
npm run dev
# 访问 http://localhost:3000
```

## 部署

详见 [06-deployment/部署手册.md](06-deployment/部署手册.md)

## License

MIT License