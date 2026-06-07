-- ============================================
-- 旅客表(passengers)增量升级脚本
-- 用于将旧版旅客表结构升级到支持8种证件类型
-- ============================================

-- 如果是全新数据库，请使用 schema.sql
-- 本脚本用于已有数据库的增量升级

-- 1. 添加新字段
ALTER TABLE passengers
    ADD COLUMN id_type VARCHAR(50) NOT NULL DEFAULT 'ID_CARD' COMMENT '证件类型' AFTER name,
    ADD COLUMN nationality VARCHAR(50) DEFAULT NULL COMMENT '国籍（外国人永久居留身份证、外国护照需要）' AFTER phone,
    ADD COLUMN expire_date DATE DEFAULT NULL COMMENT '证件有效期截止日期（仅复杂证件类型需要）' AFTER birth_date,
    ADD COLUMN email VARCHAR(100) DEFAULT NULL COMMENT '电子邮箱（仅外国护照需要）' AFTER expire_date,
    ADD COLUMN is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认旅客 0-否 1-是' AFTER student_valid_end;

-- 2. 修改id_card字段长度（兼容更长证件号）
ALTER TABLE passengers MODIFY COLUMN id_card VARCHAR(30) NOT NULL COMMENT '证件号码';

-- 3. 修改phone字段长度（兼容国际号码）
ALTER TABLE passengers MODIFY COLUMN phone VARCHAR(20) DEFAULT NULL COMMENT '手机号';

-- 4. 添加索引
ALTER TABLE passengers ADD INDEX idx_id_type (id_type);
ALTER TABLE passengers ADD INDEX idx_user_default (user_id, is_default);

-- 5. 更新现有数据（将旧数据设置为身份证类型）
UPDATE passengers SET id_type = 'ID_CARD' WHERE id_type IS NULL OR id_type = '';

-- 6. 将第一个旅客设为默认（如果存在数据）
UPDATE passengers p
SET p.is_default = 1
WHERE p.id = (
    SELECT tmp.id FROM (
        SELECT MIN(id) as id FROM passengers WHERE status = 1 GROUP BY user_id HAVING COUNT(*) = 1
        UNION
        SELECT MIN(id) as id FROM passengers WHERE status = 1 AND user_id IN (
            SELECT user_id FROM passengers WHERE status = 1 GROUP BY user_id HAVING COUNT(*) > 1
        )
    ) tmp
);

-- 7. 为每个用户的第一个旅客设置默认标记
UPDATE passengers p
SET p.is_default = 1
WHERE p.status = 1
AND p.id = (
    SELECT tmp.min_id FROM (
        SELECT user_id, MIN(id) as min_id FROM passengers WHERE status = 1 GROUP BY user_id
    ) tmp
    WHERE tmp.user_id = p.user_id
);

-- ============================================
-- 回滚脚本（如果需要回滚）
-- ============================================
/*
ALTER TABLE passengers
    DROP COLUMN id_type,
    DROP COLUMN nationality,
    DROP COLUMN expire_date,
    DROP COLUMN email,
    DROP COLUMN is_default;
*/
