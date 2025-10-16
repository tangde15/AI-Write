-- =============================================
-- AI五感作文训练平台 - 重新创建数据库
-- =============================================

-- 1. 删除旧数据库（如果存在）
DROP DATABASE IF EXISTS `ai writing`;

-- 2. 创建新数据库（无空格）
CREATE DATABASE IF NOT EXISTS aiwriting 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 3. 验证
USE aiwriting;
SELECT DATABASE() AS current_database;

-- 4. 完成提示
SELECT '✅ 数据库创建成功！现在可以启动后端服务了。' AS status;














