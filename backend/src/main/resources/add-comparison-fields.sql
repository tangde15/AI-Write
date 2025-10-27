-- 添加作文对比分析相关字段到writing_record表
-- 如果字段不存在才添加

-- 添加score字段（如果不存在）
ALTER TABLE writing_record 
ADD COLUMN IF NOT EXISTS score INT;

-- 添加previous_record_id字段（如果不存在）
ALTER TABLE writing_record 
ADD COLUMN IF NOT EXISTS previous_record_id BIGINT;

-- 添加comparison_analysis字段（如果不存在）
ALTER TABLE writing_record 
ADD COLUMN IF NOT EXISTS comparison_analysis TEXT;

-- 添加外键约束（如果不存在）
-- 由于MySQL可能不支持IF NOT EXISTS，这里先检查是否存在
-- 如果previous_record_id列已经存在外键，会报错但不会影响数据

-- 尝试添加外键，如果已存在会报错，可以忽略
-- ALTER TABLE writing_record 
-- ADD CONSTRAINT fk_record_previous FOREIGN KEY (previous_record_id) REFERENCES writing_record(id);




