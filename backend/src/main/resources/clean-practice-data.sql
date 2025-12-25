-- 删除所有答案记录和题目，保留练习册、题单、题库结构

-- 1. 删除所有答案记录（包括AI批改记录）
DELETE FROM practice_answer;
ALTER TABLE practice_answer AUTO_INCREMENT = 1;

-- 2. 删除所有题目
DELETE FROM practice_question;
ALTER TABLE practice_question AUTO_INCREMENT = 1;

-- 验证清理结果
SELECT '✅ 清理完成 - 统计结果' as 状态;
SELECT 'practice_book（练习册）' as 表名, COUNT(*) as 记录数 FROM practice_book
UNION ALL
SELECT 'practice_library（题库）', COUNT(*) FROM practice_library
UNION ALL
SELECT 'practice_set（题单）', COUNT(*) FROM practice_set
UNION ALL
SELECT 'practice_question（题目）', COUNT(*) FROM practice_question
UNION ALL
SELECT 'practice_answer（答案）', COUNT(*) FROM practice_answer;
