-- ========================================
-- 检查和修复数据表结构（适配成长曲线功能）
-- ========================================

USE aiwriting;

-- 1. 检查 writing_record 表结构
DESC writing_record;

-- 2. 检查 writing_progress 表结构  
DESC writing_progress;

-- ========================================
-- 如果 writing_record 表缺少 score 字段，执行以下语句：
-- ========================================

-- 添加 score 字段（如果不存在）
ALTER TABLE writing_record 
ADD COLUMN IF NOT EXISTS score INT NULL COMMENT 'AI评分(1-100分)';

-- ========================================
-- 检查 writing_progress 表是否存在，如果不存在则创建
-- ========================================

CREATE TABLE IF NOT EXISTS writing_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    avg_score FLOAT NULL COMMENT '平均得分',
    improvement_rate FLOAT NULL COMMENT '提升率(%)',
    date DATETIME NULL COMMENT '记录日期',
    INDEX idx_student_id (student_id),
    INDEX idx_date (date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生写作进度表';

-- ========================================
-- 验证表结构
-- ========================================

-- 查看 writing_record 表结构（应该包含 score 字段）
SHOW FULL COLUMNS FROM writing_record;

-- 查看 writing_progress 表结构
SHOW FULL COLUMNS FROM writing_progress;

-- 查看所有表
SHOW TABLES;






















