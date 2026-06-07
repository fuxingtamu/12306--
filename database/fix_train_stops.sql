-- 修复 train_stops 表的 train_id 字段
-- 根据 train_code 关联 trains 表的 id

UPDATE train_stops ts
INNER JOIN trains t ON ts.train_code = t.train_code
SET ts.train_id = t.id
WHERE ts.train_id IS NULL OR ts.train_id = 0;

-- 验证修复结果
SELECT COUNT(*) as total,
       SUM(CASE WHEN train_id IS NOT NULL AND train_id > 0 THEN 1 ELSE 0 END) as with_train_id
FROM train_stops;
