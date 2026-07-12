-- ============================================
-- 测试用户账号
-- 手机号: 13800138000
-- 密码:   123456
-- ============================================

INSERT IGNORE INTO users (phone, password, nickname, is_verified, status)
VALUES (
    '13800138000',
    '$2a$10$rDkPvvAFV8VB4UGRj6J0fuaQIBkUqFFBnsMqX3I7NOBw8ZJx3qILS',
    '测试用户',
    0,
    1
);
