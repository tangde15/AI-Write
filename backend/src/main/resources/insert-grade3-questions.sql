-- 插入"小学三年级作文精选"题库的真实题目数据

-- 假设已有的练习册ID（请根据实际情况调整）
SET @book_id = 1;  -- 假设练习册ID为1，请根据实际情况修改

-- 插入8道作文题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES
(@book_id, '我的"宝藏"同桌', '写一位你的同桌，重点描述他/她的1-2个特点（比如爱看书、会讲笑话），用1件具体的小事体现这个特点，不少于300字。', 'SUBJECTIVE', 100, NOW()),
(@book_id, '难忘的一次课间', '回忆一次有趣的课间活动（比如拔河、跳皮筋），写清楚活动的过程，以及你当时的心情，注意用上"先……然后……最后"的顺序，不少于300字。', 'SUBJECTIVE', 100, NOW()),
(@book_id, '我家的"小调皮"', '写家里的小动物（小猫、小狗、小金鱼都可以），描述它的样子，再写一件它调皮的事，让读者感受到它的可爱，不少于300字。', 'SUBJECTIVE', 100, NOW()),
(@book_id, '秋天的校园', '观察校园里的秋天景色（比如树叶、花坛、操场），用上"看一看""闻一闻""摸一摸"的感官描写，写出秋天的特点，不少于300字。', 'SUBJECTIVE', 100, NOW()),
(@book_id, '第一次____', '先把题目补充完整（比如第一次洗碗、第一次上台演讲），写清楚这件事的经过，以及你从中学到了什么，不少于300字。', 'SUBJECTIVE', 100, NOW()),
(@book_id, '我的"神奇"文具', '选一件你最喜欢的文具（铅笔盒、钢笔、橡皮都可以），描述它的样子，写清楚你为什么喜欢它，比如它帮过你什么忙，不少于300字。', 'SUBJECTIVE', 100, NOW()),
(@book_id, '下雨啦', '写一写下雨时的场景，可以写路上的行人、雨中的植物，也可以写你在雨中的经历，注意用上比喻的句子（比如"雨点儿像珍珠"），不少于300字。', 'SUBJECTIVE', 100, NOW()),
(@book_id, '给妈妈的一封信', '以书信的形式给妈妈写几句话，告诉她你最近的一件小事（比如学会了系鞋带、考了好成绩），再表达你对她的感谢，注意书信的格式（称呼、正文、落款），不少于300字。', 'SUBJECTIVE', 100, NOW());

-- 验证插入结果
SELECT 
    id,
    title,
    LEFT(requirement, 50) as requirement_preview,
    max_score,
    created_at
FROM practice_question
WHERE book_id = @book_id
ORDER BY id;

-- 统计信息
SELECT 
    CONCAT('练习册ID ', book_id, ' 共有 ', COUNT(*), ' 道题目') as 统计结果
FROM practice_question
WHERE book_id = @book_id
GROUP BY book_id;
