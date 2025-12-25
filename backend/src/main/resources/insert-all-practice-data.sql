-- ============================================================
-- 中小学作文题库完整数据插入脚本
-- 包含：8个题库 × 多个专题练习册 × 6+道题目
-- ============================================================
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 清空现有数据（可选）
-- DELETE FROM practice_question;
-- DELETE FROM practice_book;
-- DELETE FROM practice_set;
-- DELETE FROM practice_library;

-- 教师名单随机生成
SET @teachers = 'JSON_ARRAY(
  \"张老师\", \"李老师\", \"王老师\", \"陈老师\", \"罗老师\", \"周老师\", \"吴老师\", \"郑老师\", 
  \"孙老师\", \"马老师\", \"朱老师\", \"林老师\", \"何老师\", \"高老师\", \"尹老师\", \"杨老师\"
)';

-- ============================================================
-- 题库一：基础写作能力训练
-- ============================================================
INSERT INTO practice_library (title, description, author, total_count, created_at) 
VALUES (
  '基础写作能力训练',
  '涵盖写人、记事、写景、状物、想象、感悟、应用文、看图写作八大专题，全面提升学生基础写作能力',
  '张老师',
  48,
  NOW()
);
SET @lib1_id = LAST_INSERT_ID();

-- 专题练习册1-8（逐个插入以便获取准确ID）
INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib1_id, '专题1：写人篇', NOW());
SET @book1_1_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib1_id, '专题2：记事篇', NOW());
SET @book1_2_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib1_id, '专题3：写景篇', NOW());
SET @book1_3_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib1_id, '专题4：状物篇', NOW());
SET @book1_4_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib1_id, '专题5：想象篇', NOW());
SET @book1_5_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib1_id, '专题6：感悟篇', NOW());
SET @book1_6_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib1_id, '专题7：应用篇', NOW());
SET @book1_7_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib1_id, '专题8：看图写作篇', NOW());
SET @book1_8_id = LAST_INSERT_ID();

-- 题库一 专题1：写人篇（6题）
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at, updated_at) VALUES
  (@book1_1_id, '我的专属"超人"', '写一位熟悉的人（家人、老师、同学），突出他/她的一个核心特点（如勇敢、细心、幽默），用1-2件具体事例支撑，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_1_id, '班级里的"小能手"', '聚焦同学的一项特长（学习、运动、艺术等），描写他/她发挥特长的场景，加入动作、语言细节，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_1_id, '我最想感谢的人', '明确感谢对象，说明感谢的原因，通过具体事件体现对方的付出，表达真情实感，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_1_id, '陌生的温暖', '写一位陌生人给予的帮助或善意，突出相遇的场景和内心的感受，细节描写真实自然，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_1_id, '我的"反差萌"老师', '写出老师平时的样子与特殊场景下的反差（如严厉与温柔），用具体事例展现，语言生动有趣，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_1_id, '家庭"喜剧人"', '写家人中爱搞笑的一员，记录他/她的经典言行或搞笑事件，让读者感受到家庭的欢乐，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW());

-- 题库一 专题2：记事篇（6题）
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at, updated_at) VALUES
  (@book1_2_id, '一次难忘的尝试', '写一件第一次做的事（如做饭、演讲、手工），按"尝试前-尝试中-尝试后"的顺序，写出遇到的困难和收获，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_2_id, '一场激烈的较量', '记录一次比赛或竞争（体育、学习、游戏等），突出过程的紧张激烈，描写自己的动作、心理变化，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_2_id, '意外的惊喜', '写一件突然发生的美好事情，交代清楚事情的起因、经过、结果，重点刻画收到惊喜时的心情，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_2_id, '我学会了坚持', '通过一件需要毅力才能完成的事（如长跑、练字、完成复杂任务），体现自己从放弃到坚持的过程，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_2_id, '班级里的小风波', '记录班级中发生的一次矛盾或意外事件，写出事件的解决过程，体现同学间的包容或成长，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_2_id, '难忘的节日瞬间', '选取一个节日里印象最深的场景（如团圆饭、庙会、公益活动），描写环境、人物和自己的感受，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW());

-- 题库一 专题3：写景篇（6题）
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at, updated_at) VALUES
  (@book1_3_id, '校园的四季', '选取校园中不同季节的典型景物，用感官描写（看、听、闻、触）展现四季之美，结构清晰，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_3_id, '家乡的特色风光', '写家乡的一处自然景观（如山林、河流、田野），突出其独特之处，融入对家乡的热爱，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_3_id, '雨中即景', '描写一场雨（春雨、夏雨、秋雨）的过程，刻画雨前、雨中、雨后的景色变化，加入自己的观察和感受，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_3_id, '迷人的夜晚', '写夜晚的一处场景（星空、城市夜景、乡村夜晚），运用比喻、拟人修辞，营造氛围，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_3_id, '公园漫步', '按游览顺序描写公园的景物，重点突出1-2处印象最深的景点，写出游览时的心情，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_3_id, '雪后的世界', '描写雪后的景象（校园、街道、庭院），捕捉雪景的静态美和人们活动的动态美，语言生动，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW());

-- 题库一 专题4：状物篇（6题）
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at, updated_at) VALUES
  (@book1_4_id, '我的"亲密伙伴"', '写一件心爱的物品（文具、玩具、书籍），介绍它的外形、来历和对自己的意义，表达喜爱之情，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_4_id, '家乡的特产', '介绍家乡的一种特色物产（水果、美食、手工艺品），描写其外形、味道或特点，融入对家乡的情感，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_4_id, '可爱的小动物', '写家里或熟悉的小动物，描写它的外形、习性（吃饭、玩耍、休息），用具体事例体现它的可爱，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_4_id, '平凡的植物，不凡的美', '选取一种常见植物（花草、树木、蔬菜），观察其生长过程或形态特点，写出它的精神品质（如坚韧、顽强），不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_4_id, '多功能的"生活帮手"', '介绍一件家用电器或工具（洗衣机、电饭煲、书包），说明它的外形、用途和给生活带来的便利，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_4_id, '一件有故事的老物件', '写家里的一件老物品（旧照片、奶奶的针线盒、爸爸的旧书），讲述它背后的故事，体现亲情或回忆，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW());

-- 题库一 专题5：想象篇（6题）
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at, updated_at) VALUES
  (@book1_5_id, '假如我有一双翅膀', '发挥想象，写出有了翅膀后想去的地方、做的事情，内容积极向上，情节合理，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_5_id, '动物王国的运动会', '想象动物们举办运动会的场景，刻画1-2个精彩比赛瞬间，加入动物的对话和动作，生动有趣，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_5_id, '我的魔法书包', '赋予书包一种特殊魔法（如时空穿梭、变大变小、答疑解惑），讲述一次奇妙的经历，逻辑清晰，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_5_id, '外星朋友来做客', '想象外星朋友来到地球的情景，描写相遇的过程、相处的趣事，体现友好互助的主题，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_5_id, '假如我是大自然的一员', '选择一种自然事物（风、雨、云、树），以第一人称视角写自己的经历和感受，运用拟人修辞，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_5_id, '未来的家乡', '畅想20年后家乡的变化（科技、环境、生活），内容具体，体现对家乡的美好期待，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW());

-- 题库一 专题6：感悟篇（6题）
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at, updated_at) VALUES
  (@book1_6_id, '那一刻，我长大了', '写一件让自己心智成熟的事（如体谅父母、面对挫折、帮助他人），突出"长大"的瞬间感悟，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_6_id, '坚持的力量', '通过自己或他人的经历，体现坚持带来的收获，写出从中明白的道理，真情实感，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_6_id, '珍惜当下的幸福', '结合生活中的小事（家人陪伴、朋友关心、健康成长），表达对幸福的理解和珍惜，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_6_id, '一次失败的启示', '记录一次失败的经历，分析失败的原因，写出从中得到的教训和成长，积极向上，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_6_id, '平凡中的美好', '发现生活中平凡场景里的美好（如邻里互助、陌生人的善意、自然的小景），写出自己的感悟，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_6_id, '学会感恩', '结合具体事例，表达对他人（父母、老师、朋友）的感恩之情，说明感恩的意义，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW());

-- 题库一 专题7：应用篇（6题）
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at, updated_at) VALUES
  (@book1_7_id, '给远方朋友的一封信', '格式正确（称呼、问候、正文、祝福、署名、日期），介绍自己的生活、学习或家乡，表达交友的愿望，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_7_id, '倡议书：爱护校园环境', '针对校园环境问题（如乱扔垃圾、破坏绿植），写一份倡议书，明确倡议目的、内容和呼吁，语言得体，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_7_id, '我的读书推荐卡', '推荐一本喜爱的书，介绍书名、作者、主要内容，说明推荐理由（如情节精彩、道理深刻），不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_7_id, '给老师的建议书', '针对班级或学校的某一问题（如作业布置、活动安排），提出合理建议，说明建议的原因和具体内容，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_7_id, '日记一则', '格式正确（日期、星期、天气、正文），记录一天中印象最深的事，写出自己的所见、所闻、所感，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_7_id, '邀请信：来我的家乡做客', '格式规范，向朋友介绍家乡的特色（美景、美食、习俗），发出真诚的邀请，语言亲切，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW());

-- 题库一 专题8：看图写作篇（6题）
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at, updated_at) VALUES
  (@book1_8_id, '校园植树活动', '根据图片内容（一群同学在校园里植树，分工合作），展开合理想象，写出植树的过程和同学们的心情，突出"爱护环境"的主题，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_8_id, '雨中的温暖', '观察图片中的人物（下雨天，一位同学为老人撑伞），编写一个温暖的故事，体现尊老爱幼的美德，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_8_id, '班级生日会', '根据图片场景（教室里，同学们围坐在一起，为过生日的同学唱生日歌），描写班级生日会的热闹氛围，写出同学间的友谊，细节生动，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_8_id, '拯救小鸟', '结合图片（一只小鸟受伤落在窗边，小朋友小心翼翼地为它包扎），想象小鸟受伤的原因和小朋友的后续行动，表达爱护小动物的主题，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_8_id, '社区志愿服务', '根据图片（放学后，几位同学在社区里清理垃圾，路过的居民纷纷点赞），写一件社区志愿服务的事，突出同学们的责任感，写出周围人的反应，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book1_8_id, '书籍的魅力', '描写图片场景（图书馆里，同学们安静地看书，有人在做笔记，有人在查阅资料），描写图书馆的安静氛围和同学们认真学习的样子，表达对知识的热爱，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW());

-- ============================================================
-- 题库二：五感描写专项训练（省略部分，仅创建框架）
-- ============================================================
INSERT INTO practice_library (title, description, author, total_count, created_at, updated_at) 
VALUES (
  '五感描写专项训练',
  '通过视觉、听觉、嗅觉、味觉、触觉的综合运用，提升学生的细节描写能力和文字表现力',
  '李老师',
  48,
  NOW(),
  NOW()
);
SET @lib2_id = LAST_INSERT_ID();

-- 创建8个专题练习册（逐个插入）
INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib2_id, '视觉描写：捕捉色彩与形态', NOW());
SET @book2_1_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib2_id, '听觉描写：聆听世界的声音', NOW());
SET @book2_2_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib2_id, '嗅觉描写：品味气味的故事', NOW());
SET @book2_3_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib2_id, '味觉描写：舌尖上的体验', NOW());
SET @book2_4_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib2_id, '触觉描写：感受触摸的温度', NOW());
SET @book2_5_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib2_id, '五感综合：描写一处场景', NOW());
SET @book2_6_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib2_id, '五感+想象：创意写作', NOW());
SET @book2_7_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib2_id, '五感+感悟：借景抒情', NOW());
SET @book2_8_id = LAST_INSERT_ID();

-- 题库二 各专题快速插入（示例：视觉描写）
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at, updated_at) VALUES
  (@book2_1_id, '窗外的风景线', '聚焦窗外的一处景物（树木、街道、天空），细致描写其颜色、形状、动态变化，运用比喻、拟人修辞，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book2_1_id, '菜市场里的"色彩派对"', '描写菜市场里的各种食材（蔬菜、水果、肉类），通过视觉捕捉丰富的色彩和多样的形态，展现生活气息，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book2_1_id, '我眼中的四季色彩', '分别选取四季中最具代表性的颜色，结合具体景物描写，展现四季的不同韵味，结构清晰，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book2_1_id, '可爱的"小模样"', '描写一种小动物或一件物品的外形，重点刻画细节（如眼睛、毛发、纹路），让读者如见其物，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book2_1_id, '热闹的校园活动', '描写一次校园活动（运动会、文艺汇演、游园会），通过视觉捕捉热闹的场景、人物的动作和表情，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book2_1_id, '夜晚的灯光', '描写夜晚不同场景的灯光（路灯、车灯、家里的灯光），刻画灯光的颜色、亮度和氛围，融入自己的感受，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW());

-- ============================================================
-- 题库三：成长励志主题训练
-- ============================================================
INSERT INTO practice_library (title, description, author, total_count, created_at, updated_at) 
VALUES (
  '成长励志主题训练',
  '包含挫折与成长、自信与勇气、责任与担当、坚持与自律、感恩与珍惜、团结与合作等六大主题',
  '王老师',
  36,
  NOW(),
  NOW()
);
SET @lib3_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib3_id, '挫折与成长', NOW());
SET @book3_1_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib3_id, '自信与勇气', NOW());
INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib3_id, '责任与担当', NOW());
INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib3_id, '坚持与自律', NOW());
INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib3_id, '感恩与珍惜', NOW());
INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib3_id, '团结与合作', NOW());

INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at, updated_at) VALUES
  (@book3_1_id, '一次难忘的挫折', '写一件自己遇到的挫折（考试失利、比赛失败、任务受阻），写出面对挫折的心情和应对过程，体现成长，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book3_1_id, '我战胜了自己', '聚焦自己的一个弱点（胆小、懒惰、粗心），描写战胜弱点的过程，突出内心的变化，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book3_1_id, '失败是成功之母', '通过一件具体事例，说明"失败是成功之母"的道理，写出从失败中吸取的教训，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book3_1_id, '风雨后的彩虹', '用比喻的手法，将挫折比作"风雨"，成功或成长比作"彩虹"，讲述相关经历，表达积极向上的态度，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book3_1_id, '面对困难，我选择坚持', '写一件遇到困难的事，描写自己从犹豫到坚持的过程，体现坚持的力量，不少于450字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book3_1_id, '那次，我没有放弃', '记录一次想要放弃但最终坚持下来的经历，写出坚持的原因和收获，真情实感，不少于400字。', 'SUBJECTIVE', 100, NOW(), NOW());

-- ============================================================
-- 题库四-八：简化处理，仅创建框架（完整数据可手动或脚本扩展）
-- ============================================================

-- 题库四：社会观察与实践
INSERT INTO practice_library (title, description, author, total_count, created_at, updated_at) 
VALUES (
  '社会观察与实践',
  '涵盖社会新风尚、城市变化、社会调查、传统文化、科技与生活、志愿服务、家乡发展、自然与生命八大主题',
  '陈老师',
  48,
  NOW(),
  NOW()
);

-- 题库五：传统与创新的碰撞
INSERT INTO practice_library (title, description, author, total_count, created_at, updated_at) 
VALUES (
  '传统与创新的碰撞',
  '探索传统文化与现代生活的融合，体现文化自信与创新发展',
  '罗老师',
  48,
  NOW(),
  NOW()
);

-- 题库六：想象创意主题训练
INSERT INTO practice_library (title, description, author, total_count, created_at, updated_at) 
VALUES (
  '想象创意主题训练',
  '涵盖童话寓言、科幻故事、奇幻冒险、物品拟人、穿越时空、创意改写、奇思妙想、故事续写',
  '周老师',
  48,
  NOW(),
  NOW()
);

-- 题库七：应用文专项训练
INSERT INTO practice_library (title, description, author, total_count, created_at, updated_at) 
VALUES (
  '应用文专项训练',
  '包含书信、日记、倡议书、建议书、通知等常用应用文写作',
  '吴老师',
  30,
  NOW(),
  NOW()
);

-- 题库八：初中记叙文专项（8个专题×6题）
INSERT INTO practice_library (title, description, author, total_count, created_at, updated_at) 
VALUES (
  '初中记叙文专项训练',
  '面向初中学生，要求800字以上，涵盖成长足迹、亲情暖怀、师恩难忘、友谊如歌、校园记事、自然感悟、社会观察、青春感悟',
  '郑老师',
  48,
  NOW(),
  NOW()
);
SET @lib8_id = LAST_INSERT_ID();

-- 创建题库八的8个专题练习册（逐个插入）
INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib8_id, '成长足迹', NOW());
SET @book8_1_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib8_id, '亲情暖怀', NOW());
SET @book8_2_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib8_id, '师恩难忘', NOW());
SET @book8_3_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib8_id, '友谊如歌', NOW());
SET @book8_4_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib8_id, '校园记事', NOW());
SET @book8_5_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib8_id, '自然感悟', NOW());
SET @book8_6_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib8_id, '社会观察', NOW());
SET @book8_7_id = LAST_INSERT_ID();

INSERT INTO practice_book (library_id, name, created_at) VALUES (@lib8_id, '青春感悟', NOW());
SET @book8_8_id = LAST_INSERT_ID();

-- 题库八 专题1：成长足迹（6题，要求800字以上）
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at, updated_at) VALUES
  (@book8_1_id, '那一次，我突破了自己', '写一件挑战自我、突破舒适区的经历（如公开演讲、参加竞赛、直面恐惧），详细描写事件的起因、经过、结果，重点刻画准备过程中的挣扎、执行时的紧张与成功后的感悟，体现"突破"带来的成长，不少于800字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book8_1_id, '成长路上的灯塔', '以"灯塔"为喻，写一位在成长中给予你指引的人（老师、家人、陌生人），通过1-2件具体事例，展现对方的言行如何照亮你的迷茫，突出人物的精神品质与对你的深远影响，不少于800字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book8_1_id, '原来，我也可以', '聚焦自己曾经的短板（如体育、写作、社交），描写从"自卑退缩"到"尝试坚持"再到"收获进步"的完整过程，融入具体的动作、心理、环境描写，突出"自我接纳与努力"的主题，不少于800字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book8_1_id, '一场深刻的教训', '写一件因自己的疏忽、任性或判断失误导致的错误事件（如失信于人、逃避责任、盲目跟风），详细描写事件的发展与后果，重点刻画内心的愧疚、反思与改正过程，体现教训带来的心智成熟，不少于800字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book8_1_id, '成长中的"逆旅"', '"逆旅"指不顺的旅程，写一段充满挫折的成长经历（如学业低谷、家庭变故、友谊破裂），描写面对困境时的挣扎、他人的帮助与自己的坚守，突出"挫折是成长必修课"的感悟，不少于800字。', 'SUBJECTIVE', 100, NOW(), NOW()),
  (@book8_1_id, '我与____的约定', '补充题目（如梦想、朋友、未来的自己），写一个跨越时间的约定，详细描写约定的起因、坚守约定的过程与最终的兑现或感悟，体现约定对成长的激励作用，不少于800字。', 'SUBJECTIVE', 100, NOW(), NOW());

-- ============================================================
-- 统计信息
-- ============================================================
SELECT CONCAT('
=== 数据插入完成 ===
题库总数: ', COUNT(*), '个
') AS result
FROM practice_library;

SELECT CONCAT('
练习册总数: ', COUNT(*), '个
') AS result
FROM practice_book;

SELECT CONCAT('
题目总数: ', COUNT(*), '个
') AS result
FROM practice_question;
