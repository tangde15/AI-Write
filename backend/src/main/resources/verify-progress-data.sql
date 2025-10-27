-- ========================================
-- 验证成长曲线数据
-- ========================================

USE aiwriting;

-- 查看所有学生的进度数据
SELECT 
    wp.id,
    wp.student_id,
    ua.username AS '学生姓名',
    wp.avg_score AS '平均分',
    wp.improvement_rate AS '提升率(%)',
    wp.date AS '记录日期'
FROM writing_progress wp
LEFT JOIN user_account ua ON wp.student_id = ua.id
ORDER BY wp.student_id, wp.date;

-- 查看每个学生的进度记录数量
SELECT 
    wp.student_id,
    ua.username AS '学生姓名',
    COUNT(*) AS '进度记录数',
    MIN(wp.avg_score) AS '最低分',
    MAX(wp.avg_score) AS '最高分',
    MAX(wp.avg_score) - MIN(wp.avg_score) AS '进步幅度'
FROM writing_progress wp
LEFT JOIN user_account ua ON wp.student_id = ua.id
GROUP BY wp.student_id, ua.username;

-- 查看每个学生的作文数量
SELECT 
    ua.id,
    ua.username AS '学生姓名',
    COUNT(wr.id) AS '作文数量'
FROM user_account ua
LEFT JOIN writing_record wr ON ua.id = wr.user_id
WHERE ua.role = 'student'
GROUP BY ua.id, ua.username;

-- 检查是否有学生ID不匹配的问题
SELECT 
    'student_id not in user_account' AS '问题',
    wp.student_id
FROM writing_progress wp
WHERE wp.student_id NOT IN (SELECT id FROM user_account WHERE role = 'student');






















