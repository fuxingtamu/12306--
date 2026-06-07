-- 12306旅客管理系统数据库结构
-- MySQL 8.0

-- 创建数据库
CREATE DATABASE IF NOT EXISTS railway_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE railway_system;

-- ============================================
-- 1. 用户表 (users)
-- ============================================
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    phone VARCHAR(11) NOT NULL UNIQUE COMMENT '手机号',
    password VARCHAR(100) NOT NULL COMMENT '密码(加密)',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    real_name VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    id_card VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
    is_verified TINYINT(1) DEFAULT 0 COMMENT '是否实名认证 0-未认证 1-已认证',
    verify_time DATETIME DEFAULT NULL COMMENT '认证时间',
    status TINYINT(1) DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 2. 旅客表 (passengers)
-- ============================================
DROP TABLE IF EXISTS passengers;
CREATE TABLE passengers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '旅客ID',
    user_id BIGINT NOT NULL COMMENT '所属用户ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    id_type VARCHAR(50) NOT NULL COMMENT '证件类型',
    id_card VARCHAR(30) NOT NULL COMMENT '证件号码',
    passenger_type TINYINT NOT NULL DEFAULT 1 COMMENT '旅客类型 1-成人 2-儿童 3-学生 4-残军',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    nationality VARCHAR(50) DEFAULT NULL COMMENT '国籍（外国人永久居留身份证、外国护照需要）',
    gender TINYINT DEFAULT 1 COMMENT '性别 1-男 2-女',
    birth_date DATE DEFAULT NULL COMMENT '出生日期',
    expire_date DATE DEFAULT NULL COMMENT '证件有效期截止日期（仅复杂证件类型需要）',
    email VARCHAR(100) DEFAULT NULL COMMENT '电子邮箱（仅外国护照需要）',
    student_school VARCHAR(100) DEFAULT NULL COMMENT '学生学校',
    student_no VARCHAR(50) DEFAULT NULL COMMENT '学生学号',
    student_valid_start DATE DEFAULT NULL COMMENT '学生优惠有效期开始',
    student_valid_end DATE DEFAULT NULL COMMENT '学生优惠有效期结束',
    is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认旅客 0-否 1-是',
    is_verified TINYINT(1) DEFAULT 0 COMMENT '是否核验 0-未核验 1-已核验',
    verify_time DATETIME DEFAULT NULL COMMENT '核验时间',
    status TINYINT(1) DEFAULT 1 COMMENT '状态 0-删除 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_id_type (id_type),
    INDEX idx_id_card (id_card),
    INDEX idx_user_default (user_id, is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旅客表';

-- ============================================
-- 3. 车站表 (stations)
-- ============================================
DROP TABLE IF EXISTS stations;
CREATE TABLE stations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '车站ID',
    station_code VARCHAR(10) NOT NULL UNIQUE COMMENT '车站代码(电报码)',
    station_name VARCHAR(50) NOT NULL COMMENT '车站名称',
    city_name VARCHAR(50) NOT NULL COMMENT '所属城市',
    city_code VARCHAR(10) DEFAULT NULL COMMENT '城市代码',
    province VARCHAR(50) DEFAULT NULL COMMENT '所属省份',
    station_pinyin VARCHAR(100) DEFAULT NULL COMMENT '车站拼音',
    station_short_pinyin VARCHAR(20) DEFAULT NULL COMMENT '车站拼音缩写',
    station_level TINYINT DEFAULT 1 COMMENT '车站等级 1-特等站 2-一等站 3-二等站 4-三等站 5-四等站',
    is_high_speed TINYINT(1) DEFAULT 0 COMMENT '是否高铁站 0-否 1-是',
    longitude DECIMAL(10,6) DEFAULT NULL COMMENT '经度',
    latitude DECIMAL(10,6) DEFAULT NULL COMMENT '纬度',
    status TINYINT(1) DEFAULT 1 COMMENT '状态 0-停用 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_station_code (station_code),
    INDEX idx_city_name (city_name),
    INDEX idx_pinyin (station_pinyin),
    INDEX idx_short_pinyin (station_short_pinyin)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车站表';

-- ============================================
-- 4. 车次表 (trains)
-- ============================================
DROP TABLE IF EXISTS trains;
CREATE TABLE trains (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '车次ID',
    train_code VARCHAR(20) NOT NULL UNIQUE COMMENT '车次号',
    train_type TINYINT NOT NULL COMMENT '车次类型 1-高铁(G) 2-动车(D) 3-城际(C) 4-直达(Z) 5-特快(T) 6-快速(K) 7-普快',
    start_station_id BIGINT NOT NULL COMMENT '始发站ID',
    end_station_id BIGINT NOT NULL COMMENT '终点站ID',
    start_station_name VARCHAR(50) NOT NULL COMMENT '始发站名称',
    end_station_name VARCHAR(50) NOT NULL COMMENT '终点站名称',
    start_time TIME NOT NULL COMMENT '始发时间',
    end_time TIME NOT NULL COMMENT '终到时间',
    total_time INT NOT NULL COMMENT '总耗时(分钟)',
    total_distance INT DEFAULT NULL COMMENT '总里程(公里)',
    total_stops INT DEFAULT 0 COMMENT '总停靠站数',
    has_seat_selection TINYINT(1) DEFAULT 1 COMMENT '是否支持选座 0-不支持 1-支持',
    status TINYINT(1) DEFAULT 1 COMMENT '状态 0-停运 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_train_code (train_code),
    INDEX idx_start_station (start_station_id),
    INDEX idx_end_station (end_station_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车次表';

-- ============================================
-- 5. 车次经停站表 (train_stops)
-- ============================================
DROP TABLE IF EXISTS train_stops;
CREATE TABLE train_stops (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '经停站ID',
    train_id BIGINT NOT NULL COMMENT '车次ID',
    train_code VARCHAR(20) NOT NULL COMMENT '车次号',
    station_id BIGINT NOT NULL COMMENT '车站ID',
    station_name VARCHAR(50) NOT NULL COMMENT '车站名称',
    stop_order INT NOT NULL COMMENT '停靠顺序',
    arrive_time TIME DEFAULT NULL COMMENT '到达时间',
    depart_time TIME DEFAULT NULL COMMENT '发车时间',
    stop_duration INT DEFAULT 2 COMMENT '停靠时长(分钟)',
    distance INT DEFAULT NULL COMMENT '距离始发站里程(公里)',
    is_start TINYINT(1) DEFAULT 0 COMMENT '是否始发站',
    is_end TINYINT(1) DEFAULT 0 COMMENT '是否终点站',
    status TINYINT(1) DEFAULT 1 COMMENT '状态',
    INDEX idx_train_id (train_id),
    INDEX idx_train_code (train_code),
    INDEX idx_station_id (station_id),
    UNIQUE KEY uk_train_stop (train_id, stop_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车次经停站表';

-- ============================================
-- 6. 座位类型表 (seat_types)
-- ============================================
DROP TABLE IF EXISTS seat_types;
CREATE TABLE seat_types (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '座位类型ID',
    seat_type_code VARCHAR(10) NOT NULL UNIQUE COMMENT '座位类型代码',
    seat_type_name VARCHAR(20) NOT NULL COMMENT '座位类型名称',
    seat_type_desc VARCHAR(100) DEFAULT NULL COMMENT '座位类型描述',
    base_price DECIMAL(10,2) DEFAULT 0 COMMENT '基础票价(参考)',
    discount_rate DECIMAL(5,2) DEFAULT 100 COMMENT '折扣率(百分比)',
    has_seat_number TINYINT(1) DEFAULT 1 COMMENT '是否有座位号 0-无(如无座) 1-有',
    status TINYINT(1) DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位类型表';

-- 初始化座位类型数据
INSERT INTO seat_types (seat_type_code, seat_type_name, seat_type_desc, base_price, has_seat_number) VALUES
('SWZ', '商务座', '商务座，最高等级座位', 100.00, 1),
('ZY', '一等座', '一等座，舒适度较高', 60.00, 1),
('ZE', '二等座', '二等座，经济实惠', 40.00, 1),
('GR', '高级软卧', '高级软卧，双人包间', 80.00, 1),
('RW', '软卧', '软卧，四人包间', 60.00, 1),
('YW_H', '硬卧上', '硬卧上铺', 30.00, 1),
('YW_Z', '硬卧中', '硬卧中铺', 30.00, 1),
('YW_X', '硬卧下', '硬卧下铺', 30.00, 1),
('YZ', '硬座', '硬座，最经济', 20.00, 1),
('WZ', '无座', '无座站立', 20.00, 0);

-- ============================================
-- 7. 车票库存表 (ticket_inventory)
-- ============================================
DROP TABLE IF EXISTS ticket_inventory;
CREATE TABLE ticket_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库存ID',
    train_id BIGINT NOT NULL COMMENT '车次ID',
    train_code VARCHAR(20) NOT NULL COMMENT '车次号',
    travel_date DATE NOT NULL COMMENT '出行日期',
    seat_type_id BIGINT NOT NULL COMMENT '座位类型ID',
    seat_type_code VARCHAR(10) NOT NULL COMMENT '座位类型代码',
    total_seats INT NOT NULL DEFAULT 0 COMMENT '总座位数',
    available_seats INT NOT NULL DEFAULT 0 COMMENT '可售座位数',
    sold_seats INT NOT NULL DEFAULT 0 COMMENT '已售座位数',
    price DECIMAL(10,2) NOT NULL COMMENT '票价',
    status TINYINT(1) DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_train_date (train_id, travel_date),
    INDEX idx_train_code_date (train_code, travel_date),
    UNIQUE KEY uk_inventory (train_id, travel_date, seat_type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车票库存表';

-- ============================================
-- 8. 订单表 (orders)
-- ============================================
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    train_id BIGINT NOT NULL COMMENT '车次ID',
    train_code VARCHAR(20) NOT NULL COMMENT '车次号',
    travel_date DATE NOT NULL COMMENT '出行日期',
    start_station_id BIGINT NOT NULL COMMENT '出发站ID',
    start_station_name VARCHAR(50) NOT NULL COMMENT '出发站名称',
    end_station_id BIGINT NOT NULL COMMENT '到达站ID',
    end_station_name VARCHAR(50) NOT NULL COMMENT '到达站名称',
    start_time TIME NOT NULL COMMENT '发车时间',
    end_time TIME NOT NULL COMMENT '到达时间',
    passenger_count INT NOT NULL COMMENT '乘客数量',
    total_price DECIMAL(12,2) NOT NULL COMMENT '订单总价',
    pay_price DECIMAL(12,2) DEFAULT NULL COMMENT '实付金额',
    pay_time DATETIME DEFAULT NULL COMMENT '支付时间',
    pay_type TINYINT DEFAULT NULL COMMENT '支付方式 1-微信 2-支付宝 3-银行卡',
    order_status TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态 0-待支付 1-已支付 2-已取消 3-已完成 4-已退票 5-已改签',
    cancel_time DATETIME DEFAULT NULL COMMENT '取消时间',
    cancel_reason VARCHAR(200) DEFAULT NULL COMMENT '取消原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_train_date (train_id, travel_date),
    INDEX idx_order_status (order_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ============================================
-- 9. 订单明细表 (order_items)
-- ============================================
DROP TABLE IF EXISTS order_items;
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL COMMENT '订单号',
    passenger_id BIGINT NOT NULL COMMENT '旅客ID',
    passenger_name VARCHAR(50) NOT NULL COMMENT '旅客姓名',
    passenger_id_card VARCHAR(30) NOT NULL COMMENT '旅客证件号',
    passenger_type TINYINT NOT NULL COMMENT '旅客类型',
    seat_type_id BIGINT NOT NULL COMMENT '座位类型ID',
    seat_type_name VARCHAR(20) NOT NULL COMMENT '座位类型名称',
    seat_number VARCHAR(10) DEFAULT NULL COMMENT '座位号',
    carriage_number INT DEFAULT NULL COMMENT '车厢号',
    price DECIMAL(10,2) NOT NULL COMMENT '票价',
    ticket_no VARCHAR(32) DEFAULT NULL COMMENT '电子票号',
    ticket_status TINYINT NOT NULL DEFAULT 0 COMMENT '车票状态 0-待支付 1-已支付 2-已退票 3-已改签',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_passenger_id (passenger_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- ============================================
-- 10. 退票记录表 (refunds)
-- ============================================
DROP TABLE IF EXISTS refunds;
CREATE TABLE refunds (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '退票ID',
    refund_no VARCHAR(32) NOT NULL UNIQUE COMMENT '退票单号',
    order_id BIGINT NOT NULL COMMENT '原订单ID',
    order_no VARCHAR(32) NOT NULL COMMENT '原订单号',
    order_item_id BIGINT NOT NULL COMMENT '订单明细ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    train_code VARCHAR(20) NOT NULL COMMENT '车次号',
    travel_date DATE NOT NULL COMMENT '原出行日期',
    passenger_name VARCHAR(50) NOT NULL COMMENT '旅客姓名',
    original_price DECIMAL(10,2) NOT NULL COMMENT '原票价',
    refund_rate DECIMAL(5,2) NOT NULL COMMENT '退票手续费率(百分比)',
    refund_fee DECIMAL(10,2) NOT NULL COMMENT '退票手续费',
    refund_amount DECIMAL(10,2) NOT NULL COMMENT '退款金额',
    refund_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '退票时间',
    refund_reason VARCHAR(200) DEFAULT NULL COMMENT '退票原因',
    refund_status TINYINT NOT NULL DEFAULT 0 COMMENT '退票状态 0-申请中 1-已退款',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退票记录表';

-- ============================================
-- 11. 改签记录表 (changes)
-- ============================================
DROP TABLE IF EXISTS changes;
CREATE TABLE changes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '改签ID',
    change_no VARCHAR(32) NOT NULL UNIQUE COMMENT '改签单号',
    order_id BIGINT NOT NULL COMMENT '原订单ID',
    order_no VARCHAR(32) NOT NULL COMMENT '原订单号',
    order_item_id BIGINT NOT NULL COMMENT '订单明细ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    old_train_code VARCHAR(20) NOT NULL COMMENT '原车次号',
    old_travel_date DATE NOT NULL COMMENT '原出行日期',
    old_start_station VARCHAR(50) NOT NULL COMMENT '原出发站',
    old_end_station VARCHAR(50) NOT NULL COMMENT '原到达站',
    old_seat_type VARCHAR(20) NOT NULL COMMENT '原座位类型',
    new_train_code VARCHAR(20) NOT NULL COMMENT '新车次号',
    new_travel_date DATE NOT NULL COMMENT '新出行日期',
    new_start_station VARCHAR(50) NOT NULL COMMENT '新出发站',
    new_end_station VARCHAR(50) NOT NULL COMMENT '新到达站',
    new_seat_type VARCHAR(20) NOT NULL COMMENT '新座位类型',
    passenger_name VARCHAR(50) NOT NULL COMMENT '旅客姓名',
    original_price DECIMAL(10,2) NOT NULL COMMENT '原票价',
    new_price DECIMAL(10,2) NOT NULL COMMENT '新票价',
    price_diff DECIMAL(10,2) NOT NULL COMMENT '票价差额',
    change_fee DECIMAL(10,2) DEFAULT 0 COMMENT '改签手续费',
    change_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '改签时间',
    change_reason VARCHAR(200) DEFAULT NULL COMMENT '改签原因',
    change_status TINYINT NOT NULL DEFAULT 0 COMMENT '改签状态 0-申请中 1-已改签',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='改签记录表';

-- ============================================
-- 12. 候补订单表 (waiting_list)
-- ============================================
DROP TABLE IF EXISTS waiting_list;
CREATE TABLE waiting_list (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '候补ID',
    waiting_no VARCHAR(32) NOT NULL UNIQUE COMMENT '候补单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    train_id BIGINT NOT NULL COMMENT '车次ID',
    train_code VARCHAR(20) NOT NULL COMMENT '车次号',
    travel_date DATE NOT NULL COMMENT '出行日期',
    start_station_id BIGINT NOT NULL COMMENT '出发站ID',
    start_station_name VARCHAR(50) NOT NULL COMMENT '出发站名称',
    end_station_id BIGINT NOT NULL COMMENT '到达站ID',
    end_station_name VARCHAR(50) NOT NULL COMMENT '到达站名称',
    seat_type_id BIGINT NOT NULL COMMENT '座位类型ID',
    seat_type_name VARCHAR(20) NOT NULL COMMENT '座位类型名称',
    passenger_count INT NOT NULL COMMENT '候补乘客数量',
    max_price DECIMAL(10,2) DEFAULT NULL COMMENT '最高可接受价格',
    waiting_status TINYINT NOT NULL DEFAULT 0 COMMENT '候补状态 0-候补中 1-已兑现 2-已取消',
    success_time DATETIME DEFAULT NULL COMMENT '兑现时间',
    order_id BIGINT DEFAULT NULL COMMENT '兑现后的订单ID',
    expire_time DATETIME NOT NULL COMMENT '候补截止时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_train_date (train_id, travel_date),
    INDEX idx_waiting_status (waiting_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='候补订单表';

-- ============================================
-- 13. 候补乘客表 (waiting_passengers)
-- ============================================
DROP TABLE IF EXISTS waiting_passengers;
CREATE TABLE waiting_passengers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    waiting_id BIGINT NOT NULL COMMENT '候补订单ID',
    passenger_id BIGINT NOT NULL COMMENT '旅客ID',
    passenger_name VARCHAR(50) NOT NULL COMMENT '旅客姓名',
    passenger_id_card VARCHAR(30) NOT NULL COMMENT '旅客证件号',
    passenger_type TINYINT NOT NULL COMMENT '旅客类型',
    priority INT DEFAULT 0 COMMENT '优先级',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_waiting_id (waiting_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='候补乘客表';

-- ============================================
-- 14. 公告表 (announcements)
-- ============================================
DROP TABLE IF EXISTS announcements;
CREATE TABLE announcements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    announcement_type TINYINT NOT NULL DEFAULT 1 COMMENT '类型 1-系统公告 2-购票须知 3-公告 4-温馨提示',
    is_top TINYINT(1) DEFAULT 0 COMMENT '是否置顶',
    is_active TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    expire_time DATETIME DEFAULT NULL COMMENT '过期时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_type (announcement_type),
    INDEX idx_publish_time (publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';
