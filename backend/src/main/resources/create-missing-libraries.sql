-- 步骤1：创建缺失的题库（校园生活、家庭生活、社会与自然）
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET @teacher_id = 1;

-- 注意：数据文件中有8个题库，但数据库中只对应了5个（ID 4,5,6,9,10）
-- ID 7和8在数据库中存在但数据文件中没有对应内容
-- 现在我们要创建题库4、5、6对应的数据库记录

-- 查询当前最大ID
SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM practice_library;

-- 创建题库：校园生活主题训练（对应数据文件题库四）
INSERT INTO practice_library (title, description, created_at) 
VALUES ('校园生活主题训练', '涵盖课堂内外、师生情谊、同学情谊、校园活动、校园景物、校园成长、校园规则与文明、毕业与回忆八大主题', NOW());

-- 创建题库：家庭生活主题训练（对应数据文件题库五）  
INSERT INTO practice_library (title, description, created_at)
VALUES ('家庭生活主题训练', '包含亲情暖心、家庭趣事、家庭责任、家庭变迁、家庭学习、节日与传统、家庭梦想、家庭感悟八大主题', NOW());

-- 创建题库：社会与自然主题训练（对应数据文件题库六）
INSERT INTO practice_library (title, description, created_at)
VALUES ('社会与自然主题训练', '涵盖自然之美、环境保护、社会观察、传统文化、科技与生活、志愿服务、家乡发展、自然与生命八大主题', NOW());

-- 查看创建后的题库列表
SELECT id, title, description FROM practice_library ORDER BY id;
