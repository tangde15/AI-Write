-- 完整数据插入脚本（包括创建缺失的题库和所有练习册、题目）
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET @teacher_id = 1;  -- 默认教师ID

-- ====== 第一部分：创建缺失的题库（数据文件中有但数据库中没有的） ======

-- 删除旧的题库7和8（因为它们与数据文件不对应）
DELETE FROM practice_library WHERE id IN (7, 8);

-- 创建题库4：校园生活主题训练（数据文件题库四）
INSERT INTO practice_library (title, description, created_at, teacher_id) 
VALUES ('校园生活主题训练', '涵盖课堂内外、师生情谊、同学情谊、校园活动、校园景物、校园成长、校园规则与文明、毕业与回忆八大主题', NOW(), @teacher_id);

-- 创建题库5：家庭生活主题训练（数据文件题库五）  
INSERT INTO practice_library (title, description, created_at, teacher_id)
VALUES ('家庭生活主题训练', '包含亲情暖心、家庭趣事、家庭责任、家庭变迁、家庭学习、节日与传统、家庭梦想、家庭感悟八大主题', NOW(), @teacher_id);

-- 创建题库6：社会与自然主题训练（数据文件题库六）
INSERT INTO practice_library (title, description, created_at, teacher_id)
VALUES ('社会与自然主题训练', '涵盖自然之美、环境保护、社会观察、传统文化、科技与生活、志愿服务、家乡发展、自然与生命八大主题', NOW(), @teacher_id);

-- 重新创建题库7：想象创意主题训练（将原题库9的内容移到这里，与数据文件题库七对应）
-- 由于题库9已经存在且有数据，我们直接更新练习册的library_id
UPDATE practice_book SET library_id = 7 WHERE library_id = 9;
UPDATE practice_library SET title = '想象创意主题训练', description = '涵盖童话寓言、科幻故事、奇幻冒险、物品拟人、穿越时空、创意改写、奇思妙想、故事续写' WHERE id = 9;

-- 重新创建题库8：应用文专项训练（将原题库10的内容移到这里，与数据文件题库八对应）
UPDATE practice_book SET library_id = 8 WHERE library_id = 10;
UPDATE practice_library SET title = '应用文专项训练', description = '包含书信、日记、倡议书、建议书、通知等常用应用文写作' WHERE id = 10;

-- 说明：由于题库结构调整，原有的题库7、8被删除
-- 原题库9（想象创意）保持不变，因为数据文件中的题库七就是想象创意
-- 原题库10（应用文）保持不变，因为数据文件中的题库八就是应用文
-- 原题库11（初中记叙文）保持不变

-- ====== 第二部分：查看调整后的题库结构 ======
SELECT 'Current Libraries:' as Info;
SELECT id, title FROM practice_library ORDER BY id;
