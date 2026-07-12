-- ============================================
-- 旅客表: name → last_name + first_name 迁移
-- 针对 railway_system 数据库
-- 执行方式：复制到 MySQL 客户端（Workbench/DBeaver/DataGrip/IDE数据库面板）中执行
-- ============================================

USE railway_system;

-- 1. 添加 last_name 和 first_name 字段
ALTER TABLE passengers
    ADD COLUMN last_name VARCHAR(50) NOT NULL DEFAULT '' COMMENT '姓' AFTER user_id,
    ADD COLUMN first_name VARCHAR(50) NOT NULL DEFAULT '' COMMENT '名' AFTER last_name;

-- 2. 拆分旧 name 数据（中文名：首字符=姓，其余=名）
UPDATE passengers
SET
    last_name = LEFT(name, 1),
    first_name = SUBSTRING(name, 2)
WHERE name IS NOT NULL AND name != '';

-- 3. 删除旧 name 字段
ALTER TABLE passengers DROP COLUMN name;

-- 验证结果
SELECT id, user_id, last_name, first_name, id_type FROM passengers LIMIT 5;
