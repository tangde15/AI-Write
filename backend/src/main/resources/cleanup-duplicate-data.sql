-- 清理重复数据并重新组织
-- 删除重复的题库（保留ID最大的，因为它们有数据）

-- 先查看当前状态
SELECT 'Current libraries:' as info;
SELECT id, title, total_count FROM practice_library ORDER BY id;

-- 删除重复的基础写作能力训练题库（保留ID=4）
DELETE FROM practice_library WHERE id IN (2, 3) AND title = '基础写作能力训练';

-- 更新练习册的 library_id（将 library_id=2,3 的改为4）
UPDATE practice_book SET library_id = 4 WHERE library_id IN (2, 3);

-- 查看更新后的状态
SELECT 'After cleanup:' as info;
SELECT id, title, total_count FROM practice_library ORDER BY id;

SELECT 'Books distribution:' as info;
SELECT library_id, COUNT(*) as book_count FROM practice_book GROUP BY library_id ORDER BY library_id;

SELECT 'Questions distribution:' as info;
SELECT book_id, COUNT(*) as question_count FROM practice_question GROUP BY book_id ORDER BY book_id;
