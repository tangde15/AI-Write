-- 清空已有数据
DELETE FROM writing_progress;
DELETE FROM encouragement_message;
DELETE FROM writing_record;
DELETE FROM student_teacher;
DELETE FROM student_parent;
DELETE FROM user_account;

-- 插入测试用户（密码：123456，已使用BCrypt加密）
INSERT INTO user_account (username, password, role) VALUES
('student1', '$2a$10$N.zmdr9k7uOIkbTNKxfMueKd3uZvPCVhx/uDhGVHWNLbqMaHRZrGK', 'student'),
('student2', '$2a$10$N.zmdr9k7uOIkbTNKxfMueKd3uZvPCVhx/uDhGVHWNLbqMaHRZrGK', 'student'),
('teacher1', '$2a$10$N.zmdr9k7uOIkbTNKxfMueKd3uZvPCVhx/uDhGVHWNLbqMaHRZrGK', 'teacher'),
('parent1', '$2a$10$N.zmdr9k7uOIkbTNKxfMueKd3uZvPCVhx/uDhGVHWNLbqMaHRZrGK', 'parent');

-- 建立师生关系
INSERT INTO student_teacher (student_id, teacher_id) VALUES
(1, 3),
(2, 3);

-- 建立家长-孩子关系
INSERT INTO student_parent (student_id, parent_id) VALUES
(1, 4);

-- 插入作文记录（带评分）
INSERT INTO writing_record (user_id, topic, essay, ai_response, score, created_at, updated_at) VALUES
(1, '春天的故事', '春天来了，万物复苏。小草从地里探出头来，花儿绽放出美丽的笑容。小鸟在树枝上欢快地歌唱，蝴蝶在花丛中翩翩起舞。', 
'评分：65分

内容点评：
文章描写了春天的景象，但内容较为简单，缺乏深度。建议增加具体的感受和细节描写。

语言点评：
用词较为基础，句式单一。建议多使用修辞手法，如比喻、拟人等，让文章更生动。

改进建议：
1. 增加个人的感受和体验
2. 使用更多的修辞手法
3. 丰富词汇和句式', 
65, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 30 DAY),

(1, '我的朋友', '我有一个好朋友叫小明。他很善良，总是帮助别人。有一次，我忘记带课本了，他把他的课本借给了我。我们经常一起玩耍，一起学习。我很珍惜我们的友谊。', 
'评分：72分

内容点评：
文章通过具体事例展现了朋友的善良品质，内容较为充实。建议进一步深化情感表达。

语言点评：
语言流畅，叙事清晰。但可以增加一些描写朋友外貌和性格的细节。

亮点：
- 举例具体
- 情感真挚

改进建议：
1. 增加人物外貌和性格描写
2. 深化情感表达', 
72, NOW() - INTERVAL 25 DAY, NOW() - INTERVAL 25 DAY),

(1, '难忘的一天', '今天是我最难忘的一天。早上，我和爸爸妈妈一起去动物园。我们看到了许多可爱的动物，有憨态可掬的大熊猫，威风凛凛的狮子，还有调皮可爱的小猴子。其中，我最喜欢的是大熊猫，它黑白相间的毛色，圆滚滚的身体，吃竹子时的样子特别可爱。这一天让我学到了很多关于动物的知识，也让我更加热爱大自然。', 
'评分：78分

内容点评：
文章内容充实，描写了动物园的见闻和感受。重点突出，详略得当。

语言点评：
运用了多种修辞手法，如"憨态可掬"、"威风凛凛"等词语使用准确。句式有所变化，语言较为生动。

亮点：
- 详略得当，重点突出
- 词语运用准确生动
- 有感受和收获

改进建议：
1. 可以增加一些心理活动描写
2. 结尾可以更升华主题', 
78, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 20 DAY),

(1, '秋天的落叶', '秋天到了，树叶渐渐变黄了。一阵秋风吹过，金黄的树叶像一只只蝴蝶从树上飘落下来，在空中翩翩起舞。我走在铺满落叶的小路上，脚下发出"沙沙"的声音，仿佛在演奏一首秋天的乐曲。我捡起一片树叶，细细端详，它的叶脉清晰可见，就像一幅精美的图画。秋天虽然萧瑟，但也有它独特的美丽。', 
'评分：82分

内容点评：
文章描写细腻，抓住了秋天落叶的特点。运用多种感官描写（视觉、听觉），让读者仿佛身临其境。

语言点评：
运用了比喻、拟人等多种修辞手法，语言优美生动。"沙沙"的象声词使用恰当。

亮点：
- 修辞手法运用娴熟
- 多感官描写
- 语言优美，意境深远

改进建议：
1. 可以加入一些哲理思考，提升文章深度', 
82, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 15 DAY),

(1, '我的梦想', '每个人都有自己的梦想，我的梦想是成为一名作家。我喜欢阅读各种书籍，从书中我学到了很多知识，也感受到了文字的力量。我希望有一天，我也能用我的笔，写出感动人心的故事，让更多的人感受到阅读的快乐。

为了实现这个梦想，我每天都坚持写作，记录生活中的点点滴滴。我相信，只要我不懈努力，总有一天，我的梦想会实现。老师说过："有梦想谁都了不起"，我要为我的梦想而奋斗！', 
'评分：85分

内容点评：
文章主题明确，表达了对写作的热爱和追求梦想的决心。内容充实，有理想有行动。

结构点评：
层次清晰，先说梦想是什么，再说为什么有这个梦想，最后说如何实现梦想。

语言点评：
语言流畅，情感真挚。引用老师的话增强了说服力。

亮点：
- 主题明确，立意积极
- 结构完整，层次清晰
- 有具体的行动计划

改进建议：
1. 可以增加一些具体的写作经历
2. 进一步阐述为什么文字有力量', 
85, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 10 DAY),

(1, '感恩母亲', '母爱是世界上最伟大的爱。我的妈妈每天早起为我准备可口的早餐，晚上陪我学习到深夜。她总是把最好的留给我，自己却从不说累。

记得有一次我生病了，妈妈整夜守在我身边，给我量体温，喂我吃药。那一夜，我看到妈妈眼中布满了血丝，心里特别感动。妈妈为我付出了太多太多，而我有时却不够懂事，还惹她生气。

现在我长大了，我要用实际行动来报答妈妈。我要好好学习，让妈妈为我骄傲；我要帮妈妈做家务，减轻她的负担；我要常常对妈妈说"我爱你"，让她知道她的付出都被我记在心里。

妈妈，谢谢您！我爱您！', 
'评分：88分

内容点评：
文章情感真挚，通过具体事例表现了母爱的伟大和自己的感恩之心。内容充实，详略得当。

结构点评：
结构完整，从总述母爱，到具体事例，再到感恩行动，层次分明，逻辑清晰。

语言点评：
语言朴实真挚，不华丽但感人。细节描写生动，"眼中布满了血丝"让人印象深刻。

亮点：
- 情感真挚，感人至深
- 有具体事例支撑
- 有反思和行动计划
- 结尾升华主题

改进建议：
1. 可以再增加一两个细节描写
2. 语言可以更凝练一些', 
88, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY);

-- 插入 student2 的作文记录
INSERT INTO writing_record (user_id, topic, essay, ai_response, score, created_at, updated_at) VALUES
(2, '我的爱好', '我的爱好是画画。每当我拿起画笔，我就感到特别开心。我喜欢画各种各样的东西，花草树木、山川河流、人物动物，都是我喜欢的题材。', 
'评分：60分

内容点评：
简单介绍了爱好，但内容较为单薄，缺乏具体事例和深入描写。

改进建议：
1. 增加具体的绘画经历
2. 描写画画带来的感受', 
60, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 8 DAY),

(2, '校园一角', '我们学校有一个美丽的小花园。花园里种着各种各样的花，春天有桃花、杏花，夏天有月季、蔷薇，秋天有菊花、桂花。每到课间，同学们都喜欢到这里玩耍。小花园给我们的校园增添了无限生机。', 
'评分：70分

内容点评：
描写了校园花园的四季变化，内容较为充实。

亮点：
- 按季节顺序描写
- 列举了多种花卉

改进建议：
1. 增加细节描写，让画面更生动
2. 加入自己的感受', 
70, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY);

-- 插入成长曲线数据（基于上述作文评分）
-- student1 的进度数据
INSERT INTO writing_progress (student_id, avg_score, improvement_rate, date) VALUES
(1, 65.0, 0.0, NOW() - INTERVAL 30 DAY),
(1, 72.0, 10.77, NOW() - INTERVAL 25 DAY),
(1, 78.0, 8.33, NOW() - INTERVAL 20 DAY),
(1, 82.0, 5.13, NOW() - INTERVAL 15 DAY),
(1, 85.0, 3.66, NOW() - INTERVAL 10 DAY),
(1, 88.0, 3.53, NOW() - INTERVAL 5 DAY);

-- student2 的进度数据
INSERT INTO writing_progress (student_id, avg_score, improvement_rate, date) VALUES
(2, 60.0, 0.0, NOW() - INTERVAL 8 DAY),
(2, 70.0, 16.67, NOW() - INTERVAL 3 DAY);

-- 插入激励语数据
INSERT INTO encouragement_message (student_id, sender_id, sender_type, content, created_at) VALUES
(1, 3, 'teacher', '你的作文进步很大！继续保持这样的学习态度，你一定会写出更优秀的文章！', NOW() - INTERVAL 7 DAY),
(1, 4, 'parent', '宝贝，妈妈看到你的作文写得越来越好了，妈妈为你骄傲！', NOW() - INTERVAL 5 DAY),
(1, 3, 'teacher', '最近一篇《感恩母亲》写得非常好，情感真挚，细节生动。希望你继续努力！', NOW() - INTERVAL 2 DAY),
(2, 3, 'teacher', '你的作文有进步，但还需要在细节描写上多下功夫。加油！', NOW() - INTERVAL 1 DAY);










