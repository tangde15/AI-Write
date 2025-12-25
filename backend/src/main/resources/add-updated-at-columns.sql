-- ============================================================
-- 为 practice 相关表添加 updated_at 字段
-- ============================================================

-- 为 practice_set 添加 updated_at 字段
ALTER TABLE practice_set 
ADD COLUMN updated_at DATETIME DEFAULT NULL AFTER created_at;

-- 为 practice_book 添加 updated_at 字段
ALTER TABLE practice_book 
ADD COLUMN updated_at DATETIME DEFAULT NULL AFTER created_at;

-- 为 practice_question 添加 updated_at 字段
ALTER TABLE practice_question 
ADD COLUMN updated_at DATETIME DEFAULT NULL AFTER created_at;

-- 显示修改结果
SELECT 'practice_set 表结构已更新' AS message;
DESCRIBE practice_set;

SELECT 'practice_book 表结构已更新' AS message;
DESCRIBE practice_book;

SELECT 'practice_question 表结构已更新' AS message;
DESCRIBE practice_question;
