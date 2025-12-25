-- 完整题目数据插入脚本（基于需要插入的数据.txt）
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET @teacher_id = 1;  -- 默认教师ID


-- ====== 题库1：基础写作能力训练 (library_id=4) ======

-- 专题练习册1：写人篇
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (4, '写人篇', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的专属“超人”', '写一位熟悉的人（家人、老师、同学），突出他/她的一个核心特点（如勇敢、细心、幽默），用1-2件具体事例支撑，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '班级里的“小能手”', '聚焦同学的一项特长（学习、运动、艺术等），描写他/她发挥特长的场景，加入动作、语言细节，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我最想感谢的人', '明确感谢对象，说明感谢的原因，通过具体事件体现对方的付出，表达真情实感，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '陌生的温暖', '写一位陌生人给予的帮助或善意，突出相遇的场景和内心的感受，细节描写真实自然，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的“反差萌”老师', '写出老师平时的样子与特殊场景下的反差（如严厉与温柔），用具体事例展现，语言生动有趣，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭“喜剧人”', '写家人中爱搞笑的一员，记录他/她的经典言行或搞笑事件，让读者感受到家庭的欢乐，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册2：记事篇
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (4, '记事篇', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '一次难忘的尝试', '写一件第一次做的事（如做饭、演讲、手工），按“尝试前-尝试中-尝试后”的顺序，写出遇到的困难和收获，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '一场激烈的较量', '记录一次比赛或竞争（体育、学习、游戏等），突出过程的紧张激烈，描写自己的动作、心理变化，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '意外的惊喜', '写一件突然发生的美好事情，交代清楚事情的起因、经过、结果，重点刻画收到惊喜时的心情，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我学会了坚持', '通过一件需要毅力才能完成的事（如长跑、练字、完成复杂任务），体现自己从放弃到坚持的过程，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '班级里的小风波', '记录班级中发生的一次矛盾或意外事件，写出事件的解决过程，体现同学间的包容或成长，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '难忘的节日瞬间', '选取一个节日里印象最深的场景（如团圆饭、庙会、公益活动），描写环境、人物和自己的感受，不少于450字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册3：写景篇
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (4, '写景篇', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园的四季', '选取校园中不同季节的典型景物，用感官描写（看、听、闻、触）展现四季之美，结构清晰，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的特色风光', '写家乡的一处自然景观（如山林、河流、田野），突出其独特之处，融入对家乡的热爱，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '雨中即景', '描写一场雨（春雨、夏雨、秋雨）的过程，刻画雨前、雨中、雨后的景色变化，加入自己的观察和感受，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '迷人的夜晚', '写夜晚的一处场景（星空、城市夜景、乡村夜晚），运用比喻、拟人修辞，营造氛围，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '公园漫步', '按游览顺序描写公园的景物，重点突出1-2处印象最深的景点，写出游览时的心情，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '雪后的世界', '描写雪后的景象（校园、街道、庭院），捕捉雪景的静态美和人们活动的动态美，语言生动，不少于450字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册4：状物篇
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (4, '状物篇', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的“亲密伙伴”', '写一件心爱的物品（文具、玩具、书籍），介绍它的外形、来历和对自己的意义，表达喜爱之情，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的特产', '介绍家乡的一种特色物产（水果、美食、手工艺品），描写其外形、味道或特点，融入对家乡的情感，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '可爱的小动物', '写家里或熟悉的小动物，描写它的外形、习性（吃饭、玩耍、休息），用具体事例体现它的可爱，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '平凡的植物，不凡的美', '选取一种常见植物（花草、树木、蔬菜），观察其生长过程或形态特点，写出它的精神品质（如坚韧、顽强），不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '多功能的“生活帮手”', '介绍一件家用电器或工具（洗衣机、电饭煲、书包），说明它的外形、用途和给生活带来的便利，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '一件有故事的老物件', '写家里的一件老物品（旧照片、奶奶的针线盒、爸爸的旧书），讲述它背后的故事，体现亲情或回忆，不少于450字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册5：想象篇
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (4, '想象篇', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '假如我有一双翅膀', '发挥想象，写出有了翅膀后想去的地方、做的事情，内容积极向上，情节合理，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '动物王国的运动会', '想象动物们举办运动会的场景，刻画1-2个精彩比赛瞬间，加入动物的对话和动作，生动有趣，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的魔法书包', '赋予书包一种特殊魔法（如时空穿梭、变大变小、答疑解惑），讲述一次奇妙的经历，逻辑清晰，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '外星朋友来做客', '想象外星朋友来到地球的情景，描写相遇的过程、相处的趣事，体现友好互助的主题，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '假如我是大自然的一员', '选择一种自然事物（风、雨、云、树），以第一人称视角写自己的经历和感受，运用拟人修辞，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '未来的家乡', '畅想20年后家乡的变化（科技、环境、生活），内容具体，体现对家乡的美好期待，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册6：感悟篇
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (4, '感悟篇', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '那一刻，我长大了', '写一件让自己心智成熟的事（如体谅父母、面对挫折、帮助他人），突出“长大”的瞬间感悟，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '坚持的力量', '通过自己或他人的经历，体现坚持带来的收获，写出从中明白的道理，真情实感，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '珍惜当下的幸福', '结合生活中的小事（家人陪伴、朋友关心、健康成长），表达对幸福的理解和珍惜，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '一次失败的启示', '记录一次失败的经历，分析失败的原因，写出从中得到的教训和成长，积极向上，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '平凡中的美好', '发现生活中平凡场景里的美好（如邻里互助、陌生人的善意、自然的小景），写出自己的感悟，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '学会感恩', '结合具体事例，表达对他人（父母、老师、朋友）的感恩之情，说明感恩的意义，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册7：应用篇
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (4, '应用篇', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给远方朋友的一封信', '格式正确（称呼、问候、正文、祝福、署名、日期），介绍自己的生活、学习或家乡，表达交友的愿望，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '倡议书：爱护校园环境', '针对校园环境问题（如乱扔垃圾、破坏绿植），写一份倡议书，明确倡议目的、内容和呼吁，语言得体，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的读书推荐卡', '推荐一本喜爱的书，介绍书名、作者、主要内容，说明推荐理由（如情节精彩、道理深刻），不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给老师的建议书', '针对班级或学校的某一问题（如作业布置、活动安排），提出合理建议，说明建议的原因和具体内容，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '日记一则', '格式正确（日期、星期、天气、正文），记录一天中印象最深的事，写出自己的所见、所闻、所感，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '邀请信：来我的家乡做客', '格式规范，向朋友介绍家乡的特色（美景、美食、习俗），发出真诚的邀请，语言亲切，不少于450字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册8：看图写作篇
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (4, '看图写作篇', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '图：一群同学在校园里植树，分工合作，满头大汗却笑容灿烂', '根据图片内容，展开合理想象，写出植树的过程和同学们的心情，突出“爱护环境”的主题，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '图：下雨天，一位同学为老人撑伞，两人并肩走在人行道上', '观察图片中的人物、动作和环境，编写一个温暖的故事，体现尊老爱幼的美德，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '图：教室里，同学们围坐在一起，为过生日的同学唱生日歌，桌上有蛋糕', '根据图片场景，描写班级生日会的热闹氛围，写出同学间的友谊，细节生动，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '图：一只小鸟受伤落在窗边，小朋友小心翼翼地为它包扎', '结合图片，想象小鸟受伤的原因和小朋友的后续行动，表达爱护小动物的主题，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '图：放学后，几位同学在社区里清理垃圾，路过的居民纷纷点赞', '根据图片，写一件社区志愿服务的事，突出同学们的责任感，写出周围人的反应，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '图：图书馆里，同学们安静地看书，有人在做笔记，有人在查阅资料', '描写图书馆的安静氛围和同学们认真学习的样子，表达对知识的热爱，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- ====== 题库2：五感描写专项训练 (library_id=5) ======

-- 专题练习册1：视觉描写——捕捉色彩与形态
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (5, '视觉描写——捕捉色彩与形态', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '窗外的风景线', '聚焦窗外的一处景物（树木、街道、天空），细致描写其颜色、形状、动态变化，运用比喻、拟人修辞，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '菜市场里的“色彩派对”', '描写菜市场里的各种食材（蔬菜、水果、肉类），通过视觉捕捉丰富的色彩和多样的形态，展现生活气息，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我眼中的四季色彩', '分别选取四季中最具代表性的颜色，结合具体景物描写，展现四季的不同韵味，结构清晰，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '可爱的“小模样”', '描写一种小动物或一件物品的外形，重点刻画细节（如眼睛、毛发、纹路），让读者如见其物，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '热闹的校园活动', '描写一次校园活动（运动会、文艺汇演、游园会），通过视觉捕捉热闹的场景、人物的动作和表情，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '夜晚的灯光', '描写夜晚不同场景的灯光（路灯、车灯、家里的灯光），刻画灯光的颜色、亮度和氛围，融入自己的感受，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册2：听觉描写——聆听世界的声音
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (5, '听觉描写——聆听世界的声音', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '大自然的交响曲', '捕捉大自然中的声音（风声、雨声、鸟鸣、溪流声），运用比喻修辞描写声音的特点，表达对自然的喜爱，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园里的声音', '描写校园中不同时段的声音（早读声、下课铃、体育课的呐喊声），展现校园生活的活力，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '厨房里的“乐章”', '记录家人做饭时的各种声音（切菜声、炒菜声、碗筷碰撞声），体现家庭的温暖，细节生动，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '令人难忘的声音', '写一种让自己印象深刻的声音（鼓励的话语、久违的呼唤、特殊的旋律），讲述声音背后的故事和感受，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '热闹的集市', '描写集市上的各种声音（叫卖声、讨价还价声、笑声），通过声音展现集市的热闹，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '雨夜的声音', '细致描写雨夜里的不同声音（雨滴声、雷声、风声），刻画雨夜的氛围和自己的心情，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册3：嗅觉描写——品味气味的故事
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (5, '嗅觉描写——品味气味的故事', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的味道', '描写家乡特有的一种气味（饭菜香、花香、泥土香），结合具体场景，表达对家乡的思念，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '厨房里的香气', '描写一道美食的制作过程，重点刻画气味的变化（从无到有、从淡到浓），体现家人的爱，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '四季的芬芳', '选取四季中具有代表性的气味（春花香、夏荷香、秋桂香、冬梅香），描写气味带来的感受，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '难忘的气味记忆', '写一种与回忆相关的气味（旧书的墨香、奶奶的肥皂香、童年的零食香），讲述背后的故事，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园的香气', '描写校园里的气味（花坛的花香、食堂的饭菜香、图书馆的书香），展现校园生活的美好，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '大自然的气息', '描写走进自然时闻到的气味（草地的清香、森林的腐叶香、海边的咸腥气），写出自己的感受，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册4：味觉描写——舌尖上的体验
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (5, '味觉描写——舌尖上的体验', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我最爱的一道菜', '描写一道自己喜爱的菜，详细刻画入口的味道（酸、甜、苦、辣、咸），以及吃菜时的心情，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的特色美食', '介绍家乡的一种特色美食，描写其口感和味道，结合制作过程或食用场景，表达对家乡的热爱，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '第一次尝试的味道', '写一种第一次吃的食物，描写味道的独特之处，以及当时的心情（好奇、惊喜、抗拒），不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '四季的味道', '选取四季中代表性的食物（春芽、夏瓜、秋果、冬汤），描写其味道和食用感受，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '妈妈的味道', '描写妈妈做的饭菜的味道，体现饭菜中包含的母爱，写出自己的感受，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '舌尖上的旅行', '写一次品尝异地美食的经历，描写美食的味道和特色，展现不同的饮食文化，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册5：触觉描写——感受触摸的温度
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (5, '触觉描写——感受触摸的温度', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '触摸大自然', '描写触摸自然事物的感受（草地的柔软、树皮的粗糙、溪水的清凉），运用细节描写，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '温暖的触摸', '写一次感受到温暖的触摸（妈妈的拥抱、老师的抚摸、朋友的搀扶），讲述背后的故事，表达真情实感，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '不同材质的触感', '描写几种不同材质物品的触摸感受（丝绸的顺滑、石头的坚硬、棉花的柔软），运用对比，描写生动，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '季节的触感', '描写不同季节的触摸感受（春风的轻柔、夏雨的冰凉、秋霜的冷冽、冬雪的松软），不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我心爱的物品', '描写一件心爱物品的触感（玩具的光滑、书籍的纸张质感、文具的细腻），表达喜爱之情，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '难忘的触感记忆', '写一次印象深刻的触摸体验（受伤时的疼痛、成功时的激动触感、离别时的不舍），讲述背后的故事，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册6：五感综合——描写一处场景
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (5, '五感综合——描写一处场景', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '热闹的公园', '运用五感描写公园的场景（看景色、听声音、闻花香、触微风、尝零食），展现公园的热闹，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的庙会', '结合五感，描写庙会的场景（看表演、听吆喝、闻美食香、触摊位物品、尝小吃），体现民俗特色，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园的清晨', '用五感捕捉校园清晨的美好（看朝阳、听早读声、闻草木香、触晨露、尝早餐），不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '走进森林', '运用五感描写森林中的体验（看绿树、听鸟鸣、闻腐叶香、触树皮、尝野果），展现自然之美，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭聚餐', '结合五感描写家庭聚餐的场景（看饭菜、听笑声、闻香气、触碗筷、尝美食），体现家庭的温馨，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '书店里的时光', '用五感描写在书店的体验（看书籍、听翻书声、闻墨香、触书页、尝饮品），表达对阅读的热爱，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册7：五感+想象——创意写作
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (5, '五感+想象——创意写作', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '假如我是一朵花', '以花的视角，运用五感描写生长过程（看阳光、听风雨、闻蜂鸣、触露珠、尝花蜜），加入想象，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '海底探险记', '发挥想象，运用五感描写海底世界（看珊瑚、听鱼群、闻海水、触海藻、尝海盐），情节有趣，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '魔法面包店', '想象一家魔法面包店，运用五感描写面包的神奇（看外形、听制作声、闻香气、触口感、尝味道），故事生动，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '云朵上的旅行', '发挥想象，用五感描写在云朵上的经历（看大地、听风声、闻云气、触云朵、尝“云零食”），充满童趣，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '玩具王国的派对', '想象玩具们举办派对的场景，运用五感描写（看装饰、听音乐、闻零食香、触道具、尝甜点），热闹有趣，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '月光下的奇遇', '结合想象，用五感描写月光下的奇遇（看月光、听夜虫、闻花香、触露水、尝“月光果”），意境优美，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册8：五感+感悟——借景抒情
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (5, '五感+感悟——借景抒情', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '雨中的感悟', '运用五感描写雨天的场景，结合自身经历，写出从中得到的感悟（如坚持、平静、希望），不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '桂花飘香时', '通过五感描写桂花，结合回忆或经历，表达对亲情、友情或时光的感悟，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '冬日暖食', '用五感描写冬日里的一道暖食，写出食物带来的温暖，以及从中体会到的生活美好，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '清晨的鸟鸣', '通过五感捕捉清晨的鸟鸣和周边场景，表达对生活的热爱或对自由的向往，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '老街的味道', '运用五感描写老街的场景（看建筑、听吆喝、闻气味、触墙壁、尝小吃），表达对时光或家乡的感悟，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '掌心的温度', '描写一次触摸的经历（如牵手、接物品），结合五感，表达对亲情、友情或善意的感悟，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- ====== 题库3：成长励志主题训练 (library_id=6) ======

-- 专题练习册1：挫折与成长
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (6, '挫折与成长', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '一次难忘的挫折', '写一件自己遇到的挫折（考试失利、比赛失败、任务受阻），写出面对挫折的心情和应对过程，体现成长，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我战胜了自己', '聚焦自己的一个弱点（胆小、懒惰、粗心），描写战胜弱点的过程，突出内心的变化，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '失败是成功之母', '通过一件具体事例，说明“失败是成功之母”的道理，写出从失败中吸取的教训，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '风雨后的彩虹', '用比喻的手法，将挫折比作“风雨”，成功或成长比作“彩虹”，讲述相关经历，表达积极向上的态度，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '面对困难，我选择坚持', '写一件遇到困难的事，描写自己从犹豫到坚持的过程，体现坚持的力量，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '那次，我没有放弃', '记录一次想要放弃但最终坚持下来的经历，写出坚持的原因和收获，真情实感，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册2：自信与勇气
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (6, '自信与勇气', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我终于敢尝试了', '写一件自己曾经不敢做，后来鼓起勇气尝试的事，描写勇气产生的过程和尝试后的感受，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '自信让我更出色', '通过具体事例，说明自信对自己的帮助（如演讲、比赛、学习），写出培养自信的过程，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '勇敢迈出第一步', '聚焦“第一次”做某件勇敢的事（如发言、独自出行、帮助他人），描写内心的紧张和勇敢迈出后的成长，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '做勇敢的自己', '写一件体现自己勇敢的事，对比过去的胆小，突出现在的成长，表达对“勇敢”的理解，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '挑战自我', '描写一次挑战自我的经历（如参加演讲比赛、长跑、攻克难题），写出挑战过程中的努力和最终的收获，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '勇气来自鼓励', '写他人的鼓励如何让自己变得勇敢，通过具体事例，体现鼓励的力量和勇气的成长，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册3：责任与担当
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (6, '责任与担当', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的责任', '写一件自己承担责任的事（如照顾家人、班级工作、小组任务），描写承担责任的过程和感受，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '做一个有担当的人', '通过具体事例，说明什么是“担当”，写出自己如何承担责任，体现成长，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '班级小主人', '描写自己为班级做的一件事（如打扫卫生、帮助同学、组织活动），体现对班级的责任，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '责任的重量', '写一件因自己的疏忽而需要承担后果的事，描写认识错误、承担责任的过程，写出从中明白的道理，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我当“小管家”', '记录一次当家庭或小组“小管家”的经历（如管理财务、安排任务），描写承担责任的辛苦和收获，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '担当让我成长', '通过一件具体事例，展现自己的担当（如主动承认错误、帮助他人解决困难），写出担当带来的成长，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册4：坚持与自律
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (6, '坚持与自律', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '坚持的收获', '写一件需要长期坚持才能完成的事（如练字、运动、背单词），描写坚持过程中的困难和最终的收获，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '自律让我进步', '通过具体事例，说明自律对自己的帮助（如合理安排时间、克服拖延），写出培养自律的过程，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的“打卡”计划', '记录一次长期打卡活动（如读书打卡、运动打卡），描写打卡过程中的坚持和成长，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '战胜拖延症', '写自己如何克服拖延的毛病，通过具体事例，写出自律带来的改变，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '为梦想坚持', '围绕自己的一个小梦想（如画画、弹琴、写作），描写坚持追求梦想的过程，表达对梦想的热爱，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '自律是一种力量', '通过一件事，体现自律的力量（如坚持晨读、拒绝诱惑），写出自己对自律的理解，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册5：感恩与珍惜
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (6, '感恩与珍惜', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '感恩父母的陪伴', '描写父母陪伴自己成长的一件事，体现父母的爱，表达感恩之情，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '珍惜同学情', '写一件与同学之间的难忘往事，体现同学间的友谊，表达珍惜之情，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '老师的恩情', '通过具体事例，描写老师对自己的关心和帮助，表达对老师的感恩，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '珍惜当下的生活', '结合生活中的小事，表达对幸福生活的珍惜（如健康、和平、亲情），写出自己的感悟，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '感恩陌生人的善意', '写一件陌生人帮助自己的事，描写当时的场景和心情，表达对善意的感恩和传递意愿，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '珍惜时间', '通过一件因浪费时间而后悔或因珍惜时间而受益的事，表达对“时间”的理解和珍惜，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册6：团结与合作
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (6, '团结与合作', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '一次成功的合作', '写一件与他人合作完成的事（如小组项目、比赛、活动），描写合作的过程和遇到的困难，体现团结的力量，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '团结就是力量', '通过具体事例（如拔河比赛、集体活动），说明“团结就是力量”的道理，写出合作中的收获，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我们的小组', '描写自己所在的小组，通过一件事展现小组的团结协作，表达对小组的热爱，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '合作让事情更简单', '写一件自己独自做不好，通过合作完成的事，对比独自尝试和合作的不同，体现合作的重要性，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '团队的温暖', '描写在团队中感受到的温暖（如互相帮助、互相鼓励），通过具体事例，体现团队的凝聚力，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '学会合作', '写自己从不会合作到学会合作的过程，描写合作中遇到的问题和解决方法，体现成长，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册7：诚信与善良
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (6, '诚信与善良', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '一次诚信的考验', '写一件考验自己诚信的事（如捡到东西、考试作弊、答应别人的事），描写内心的挣扎和最终的选择，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '善良的力量', '通过具体事例（如帮助他人、关爱小动物），体现善良带来的温暖和美好，表达对“善良”的理解，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我做到了诚信', '写一件自己坚守诚信的事（如按时归还物品、承认错误），描写过程和感受，体现诚信的品质，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '善良让世界更美好', '写一件自己或他人传递善良的事，描写场景和影响，表达对善良的赞美，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '诚信是金', '通过一件事，说明“诚信是金”的道理，写出诚信带来的信任和收获，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '小小的善举', '描写一件看似微小的善举（如让座、指路、分享），写出善举带来的温暖，表达“勿以善小而不为”的道理，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册8：梦想与追求
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (6, '梦想与追求', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的梦想', '明确自己的一个梦想（如科学家、医生、教师），说明梦想的由来，写出为实现梦想付出的努力，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '追求梦想的路上', '写一件在追求梦想过程中发生的事（如遇到困难、得到鼓励），描写内心的坚持和成长，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '为梦想点赞', '写一位为梦想努力的人（自己、同学、名人），描写他/她追求梦想的过程，表达敬佩之情，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的小目标', '设定一个近期的小目标（如提高成绩、学会技能），描写为实现目标的努力和进展，体现对梦想的逐步追求，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '梦想的力量', '通过具体事例，说明梦想如何激励自己克服困难、不断进步，表达对梦想的执着，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '坚持梦想，永不放弃', '写一件在追求梦想过程中遇到挫折但不放弃的事，描写坚持的原因和收获，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- ====== 题库4：校园生活主题训练 (library_id=14) ======

-- 专题练习册1：课堂内外
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (14, '课堂内外', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '一堂难忘的课', '写一堂印象深刻的课（有趣、感动、有启发），描写课堂上的场景、老师的讲解和自己的收获，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '课间十分钟', '描写课间十分钟的热闹场景（游戏、聊天、准备下节课），突出欢乐氛围，细节生动，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的课堂收获', '写在某一门课上的学习收获（知识、技能、道理），通过具体事例，体现学习的乐趣，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '课堂上的小插曲', '记录课堂上发生的一次意外或有趣的小事件（如回答错误、突发状况），描写过程和大家的反应，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我最喜欢的一堂课', '说明喜欢这堂课的原因（老师、内容、形式），描写课堂中的精彩瞬间，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '课后的小探索', '写课堂学习后，自己围绕知识点进行的探索（实验、查阅资料、实践），写出探索的过程和发现，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册2：师生情谊
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (14, '师生情谊', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '老师，谢谢您', '写一件老师关心、帮助自己的事，描写老师的言行和自己的感受，表达感恩之情，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的“良师益友”', '描写一位既是老师又是朋友的老师，通过具体事例展现师生间的亲密关系，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '老师的鼓励', '写老师在自己遇到困难或挫折时的鼓励，描写鼓励的话语和带来的力量，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '难忘的师生情', '回忆与老师之间的一段难忘往事（如毕业赠言、共同活动），表达对老师的思念和感激，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '老师的小秘密', '发现老师的一个“小秘密”（如关爱学生、默默付出），通过具体事例，体现老师的可爱或伟大，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我想对老师说', '以书信或直接倾诉的方式，写出想对老师说的话（感谢、建议、心里话），真情实感，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册3：同学情谊
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (14, '同学情谊', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的好朋友', '写一位好朋友，突出他/她的特点，通过1-2件事展现友谊，表达珍惜之情，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '同学间的温暖', '写一件同学帮助自己或互相帮助的事，描写过程和感受，体现同学情谊的珍贵，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我们的“小组故事”', '描写小组同学之间的相处（合作、互助、趣事），展现小组的凝聚力，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '一次难忘的同学聚会', '写一次同学聚会（生日会、节日聚会、毕业聚会），描写热闹的场景和大家的心情，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '误会化解之后', '写一次与同学发生误会的经历，描写误会产生、化解的过程，体现友谊的包容，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '同学教会我', '写一位同学教会自己的道理或技能，通过具体事例，表达对同学的感谢，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册4：校园活动
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (14, '校园活动', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '精彩的运动会', '描写运动会中的一个或多个精彩瞬间（比赛、加油、互助），突出热闹氛围和拼搏精神，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园文艺汇演', '写校园文艺汇演的场景，描写一个印象最深的节目，体现同学们的才艺和付出，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园读书节', '描写读书节的活动（读书分享、朗诵比赛、图书义卖），表达对阅读的热爱，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '难忘的校园义卖', '写校园义卖活动的过程（准备、售卖、捐款），描写热闹的场景和自己的感受，体现爱心，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园大扫除', '描写校园大扫除的场景，同学们分工合作的过程，体现团结和责任感，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园科技节', '写科技节中的有趣活动（实验展示、小发明评比），描写自己的参与和收获，体现对科学的热爱，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册5：校园景物
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (14, '校园景物', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '美丽的校园', '按一定顺序（空间、四季）描写校园的景物，突出校园的美丽，表达对校园的热爱，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园的小树林', '描写校园里的小树林，刻画其四季变化或不同时段的景色，融入自己的感受，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我们的教室', '描写自己的教室，包括布置、氛围、同学们的活动，体现教室的温暖，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园的花坛', '描写校园花坛里的花草，刻画其形态、颜色、香气，写出花坛给校园带来的生机，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园的小路', '写校园里的一条小路，描写小路的景色和自己在小路上的经历（散步、思考、聊天），不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园的清晨与黄昏', '对比描写校园清晨和黄昏的景色，展现不同的美，表达对校园的喜爱，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册6：校园成长
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (14, '校园成长', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我在校园里长大', '写在校园里的成长变化（从胆小到勇敢、从粗心到细心），通过具体事例，体现校园生活的意义，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的校园收获', '总结在校园里的收获（知识、友谊、技能、道理），分点或通过事例展现，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园里的第一次', '写在校园里第一次做的事（发言、获奖、当班干部），描写过程和感受，体现成长，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我为校园添光彩', '写一件自己为校园做的有意义的事（如参加比赛、志愿服务、保护环境），表达自豪感，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园里的挫折与成长', '写在校园里遇到的一次挫折（考试失利、竞选失败），描写面对挫折的过程和成长，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的校园梦想', '写在校园里的一个小梦想（如成为优秀学生、参加比赛获奖），描写为实现梦想的努力，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册7：校园规则与文明
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (14, '校园规则与文明', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园规则伴我行', '写校园规则对自己的影响（如遵守纪律、爱护环境），通过具体事例，说明规则的重要性，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '做文明校园小主人', '描写自己或同学的文明行为（如礼让、垃圾分类、爱护公物），表达对文明校园的向往，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园里的文明小事', '写校园里发生的一件文明小事（如帮助他人、主动认错），体现文明的力量，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '遵守规则，快乐成长', '通过一件事，说明遵守校园规则带来的快乐和收获，表达对规则的理解，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我是文明宣传员', '写自己向同学宣传文明行为的经历（如制作海报、演讲、提醒他人），描写过程和效果，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '文明在校园', '描写校园里的文明现象，对比不文明行为，表达对文明校园的热爱，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册8：毕业与回忆
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (14, '毕业与回忆', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '难忘的校园时光', '回忆校园生活中的难忘瞬间（师生情、同学情、精彩活动），表达留恋之情，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给母校的一封信', '格式正确，向母校表达感谢，回忆在校园的成长，送上祝福，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '毕业赠言', '给同学或老师写一段毕业赠言，结合具体事例，表达真情实感，不少于450字（可写多人或一人）。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '再见了，我的校园', '描写毕业时的场景和心情，回忆在校园的点点滴滴，表达不舍，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园里的遗憾与成长', '写在校园里的一件遗憾事（如错过机会、误解他人），描写从中得到的成长，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的校园回忆册', '以“回忆册”的形式，选取3-4个校园生活片段（如第一次入学、运动会、生日会），串联成篇，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- ====== 题库5：家庭生活主题训练 (library_id=15) ======

-- 专题练习册1：亲情暖心
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (15, '亲情暖心', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '妈妈的爱藏在____里', '先补充题目（如饭菜、唠叨、拥抱），通过具体事例，描写妈妈的爱，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '爸爸的“硬核”关爱', '写爸爸表达爱的独特方式（如严厉的教导、默默的付出、一起运动），体现深沉的父爱，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭欢乐多', '描写家庭中的一件搞笑或欢乐的事（如家庭游戏、旅行、聚餐），展现家庭的温馨，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我与家人的约定', '写自己与家人的一个约定（如一起学习、旅行、完成目标），描写约定的过程和实现后的感受，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家人的陪伴', '写家人在自己遇到困难、生病或失落时的陪伴，表达对家人的感恩，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭“小矛盾”', '写一次家庭中的小矛盾（如意见不合、误会），描写矛盾化解的过程，体现亲情的包容，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册2：家庭趣事
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (15, '家庭趣事', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭厨艺大赛', '描写一次家庭厨艺比赛的过程（准备、烹饪、评选），突出热闹欢乐的氛围，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭游戏之夜', '写一次家庭游戏活动（如桌游、益智游戏、户外游戏），描写游戏中的趣事和大家的反应，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我家的“动物成员”', '写家里的宠物，描写它的习性和与家人的趣事，体现宠物给家庭带来的欢乐，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭旅行记', '写一次家庭旅行的经历，描写途中的趣事、美景和感受，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我当家庭“小老师”', '写自己教家人做某件事（如用手机、学英语、做手工），描写过程中的趣事和成就感，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭大扫除中的趣事', '描写家庭大扫除时发生的有趣事情（如分工合作、意外发现、搞笑瞬间），不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册3：家庭责任
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (15, '家庭责任', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我是家庭“小主人”', '写自己为家庭做的家务（如做饭、打扫、照顾家人），描写过程和感受，体现责任感，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '照顾家人的一天', '记录一次照顾家人的经历（如照顾生病的父母、年幼的弟妹），描写付出的努力和收获，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭理财小管家', '写自己管理家庭零花钱或参与家庭购物的经历，描写如何合理规划，体现责任和成长，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我为家庭出份力', '写一件自己为家庭解决的小问题（如修理物品、出主意、节省开支），表达自豪感，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭责任我分担', '描写自己主动分担家庭责任的过程（如主动做家务、关心家人），体现成长，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的家庭“岗位”', '给自己在家庭中设定一个“岗位”（如环保员、采购员、调解员），描写履行职责的经历，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册4：家庭变迁
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (15, '家庭变迁', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我家的新房子', '描写家里搬新家的经历，对比新旧房子的不同，描写布置新家的过程和喜悦，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭里的新变化', '写家里的一个新变化（如添了新成员、换了新工作、培养了新爱好），描写变化带来的影响，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的变化，家庭的幸福', '结合家乡的变化（如交通、环境、生活），描写家庭生活的改善，表达幸福感，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我家的“旧时光”', '通过一件老物件或照片，回忆家庭的往事，描写时代的变迁和亲情的不变，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭的成长与变迁', '写自己长大过程中家庭的变化（如父母的衰老、自己的成熟），表达对家庭的珍惜，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '科技改变我的家', '描写科技产品（如智能家居、网络）给家庭生活带来的便利和变化，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册5：家庭学习
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (15, '家庭学习', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭学习小组', '写自己和家人一起学习的经历（如一起读书、讨论问题、共同进步），体现家庭的学习氛围，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家人教我____', '补充题目（如做人、技能、知识），写家人教会自己的一件事，描写过程和收获，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的家庭图书馆', '描写家里的藏书或阅读角落，写自己和家人的阅读经历，表达对阅读的热爱，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭中的“学习榜样”', '写家人中热爱学习的一员，描写他/她的学习习惯和对自己的影响，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭实践活动', '写一次家庭实践活动（如种植、手工、实验），描写过程和学到的知识，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我教家人学新知', '写自己教家人学习新知识或技能（如电脑操作、英语单词、环保知识），描写过程和成就感，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册6：节日与传统
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (15, '节日与传统', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我家的春节', '描写家里过春节的习俗（贴春联、包饺子、拜年），展现节日的热闹和家庭的温馨，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '中秋团圆夜', '写中秋节家人团聚的场景（赏月、吃月饼、聊天），表达对团圆的珍惜，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的节日习俗', '介绍家乡的一个传统节日习俗，描写家人庆祝节日的过程，体现传统文化的魅力，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我家的节日大餐', '描写节日里家人一起做大餐的过程，突出饭菜中的亲情，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '节日里的感动', '写节日里发生的一件感动的事（如帮助他人、家人的惊喜），表达节日的温暖，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '传统节日的意义', '通过一次节日经历，写出自己对传统节日意义的理解（如团圆、感恩、传承），不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册7：家庭梦想
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (15, '家庭梦想', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的家庭梦想', '写一个家庭共同的梦想（如旅行、买房、家人健康），描写为实现梦想的努力，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我为家庭圆梦', '写自己为实现家庭梦想做的努力（如努力学习、分担家务、节省开支），表达对家庭的爱，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家人的梦想与支持', '写家人的一个梦想，描写家人为实现梦想的努力和自己的支持，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '小小的家庭愿望', '写一个简单的家庭愿望（如一起看日出、拍全家福），描写实现愿望的过程和感受，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '梦想中的家庭生活', '畅想自己理想中的家庭生活（如和睦、快乐、充满书香），表达对美好生活的向往，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '为家庭梦想点赞', '写家人为实现梦想而努力的故事，表达敬佩和支持，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册8：家庭感悟
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (15, '家庭感悟', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家是温暖的港湾', '通过一件事，说明“家是温暖的港湾”，描写家人在自己遇到困难时的支持，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我理解了家人的爱', '写自己从不懂到理解家人的爱的过程，通过具体事例，体现成长，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭教会我的道理', '写家庭生活教会自己的一个重要道理（如诚信、善良、坚持），通过具体事例，表达感悟，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '平凡的家庭，不平凡的爱', '描写家庭中平凡的小事（如日常陪伴、饭菜香、唠叨），体现不平凡的亲情，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '珍惜我的家', '结合生活经历，表达对家庭的珍惜（如珍惜陪伴、珍惜健康、珍惜和睦），不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家庭的力量', '写家庭的力量如何帮助自己克服困难、勇敢前行，通过具体事例，表达对家庭的感恩，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- ====== 题库6：社会与自然主题训练 (library_id=16) ======

-- 专题练习册1：自然之美
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (16, '自然之美', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '春天的脚步', '描写春天到来的景象（植物发芽、动物苏醒、天气变化），运用感官描写，体现春天的生机，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '夏日荷塘', '描写夏日荷塘的景色（荷花、荷叶、蜻蜓、水波），运用比喻、拟人修辞，意境优美，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '秋天的田野', '描写秋天田野的丰收景象（农作物、农民劳作、色彩），表达对秋天的喜爱，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '冬日雪景', '描写冬天的雪景（雪花、积雪、雪景中的人和事），突出冬日的静谧或热闹，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '美丽的星空', '描写夜晚的星空，运用想象和修辞，表达对自然的敬畏或对梦想的向往，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的小河', '描写家乡的小河（四季变化、河边景色、童年回忆），表达对家乡和自然的热爱，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册2：环境保护
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (16, '环境保护', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '保护我们的家园', '写一件与环境保护相关的事（如垃圾分类、植树、制止破坏行为），说明保护环境的重要性，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我是环保小卫士', '记录自己参与环保的经历（如捡垃圾、节约资源、宣传环保），描写过程和感受，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给地球妈妈的一封信', '格式正确，向地球妈妈表达歉意（如污染、破坏），提出保护建议，表达决心，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '身边的环保变化', '写身边的环保成果（如空气变清新、河水变清澈、绿化增多），表达喜悦之情，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '节约资源，从我做起', '通过具体事例（如节约用水、用电、用纸），说明节约资源的意义，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '保护小动物', '写一件保护小动物的事（如救助受伤动物、宣传保护知识），表达对生命的尊重，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册3：社会观察
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (16, '社会观察', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '身边的劳动者', '描写一位身边的劳动者（清洁工、交警、快递员），通过具体场景，体现他们的辛苦和奉献，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '社会新风尚', '写一种社会新风尚（如志愿服务、文明出行、邻里互助），通过具体事例，展现社会的进步，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '城市里的变化', '描写城市中的一个变化（如交通、环境、设施），体现城市的发展，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '一次社会调查', '记录一次简单的社会调查（如垃圾分类情况、阅读习惯、交通秩序），描写调查过程和发现，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '身边的感动', '写一件社会中让人感动的事（如陌生人的帮助、爱心捐赠、见义勇为），表达对善良的赞美，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我眼中的社会', '结合自己的观察，写对社会的认识（如和谐、进步、需要改进的地方），表达自己的看法，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册4：传统文化
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (16, '传统文化', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的传统工艺', '介绍家乡的一种传统工艺（如剪纸、刺绣、陶艺），描写其制作过程和特点，表达对传统文化的热爱，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '传统节日的魅力', '写一个传统节日（如端午、中秋、重阳），描写节日习俗和文化内涵，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我学传统技艺', '写自己学习一项传统技艺（如书法、包饺子、扎灯笼）的过程，描写遇到的困难和收获，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '传统文化在身边', '发现身边的传统文化元素（如古建筑、传统美食、民俗活动），描写其魅力，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '传承传统文化', '通过具体事例，说明传承传统文化的重要性，写出自己能为传承做的事，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '传统与现代', '对比传统与现代的生活方式（如通讯、交通、娱乐），表达对传统文化的珍惜和对现代发展的赞叹，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册5：科技与生活
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (16, '科技与生活', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '科技改变生活', '写科技产品（如手机、互联网、智能家居）给生活带来的便利，通过具体事例，体现科技的力量，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的科技小发明', '发挥想象，设计一项科技小发明，说明其用途、特点和制作思路，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '科技节见闻', '描写参加科技节的经历（如参观展览、体验科技产品、观看表演），表达对科学的热爱，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我眼中的人工智能', '写自己对人工智能的认识（如AI助手、智能机器人），结合生活体验，表达看法，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '未来科技畅想', '发挥想象，描写未来的科技生活（如交通、医疗、学习），内容合理，充满创意，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '科技与环保', '写科技在环境保护中的应用（如新能源、环保设备），说明科技对环保的帮助，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册6：志愿服务
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (16, '志愿服务', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '一次难忘的志愿活动', '写一次参与志愿服务的经历（如照顾老人、帮助儿童、社区服务），描写过程和感受，体现奉献精神，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我是志愿者', '记录自己作为志愿者的工作（如引导、讲解、帮扶），描写遇到的人和事，表达自豪感，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '志愿服务的意义', '通过具体事例，说明志愿服务的价值（帮助他人、提升自己、促进和谐），不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '身边的志愿者', '描写一位身边的志愿者，通过他/她的故事，表达对志愿服务的敬佩，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园志愿行', '写校园里的志愿服务活动（如帮助同学、维护秩序、美化校园），体现团结互助，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '志愿精神伴我行', '写志愿服务对自己的影响（如学会关爱、懂得奉献），表达传承志愿精神的决心，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册7：家乡发展
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (16, '家乡发展', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的变化', '描写家乡近年来的变化（如交通、环境、生活水平），通过具体事例，体现家乡的发展，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我爱我的家乡', '结合家乡的美景、美食、习俗，表达对家乡的热爱，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的未来', '畅想家乡的未来发展（如更美丽、更便利、更繁荣），表达对家乡的美好期待，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的名片', '介绍家乡的一个特色（如美景、美食、名人、特产），成为家乡的“名片”，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我为家乡代言', '以“家乡代言人”的身份，向他人介绍家乡的优点，邀请大家来家乡做客，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家乡的故事', '通过一个家乡的故事（如老建筑的故事、长辈的经历），体现家乡的历史和文化，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册8：自然与生命
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (16, '自然与生命', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '顽强的生命', '写一种顽强的生命（如小草、野花、受伤的动物），描写其生长或求生的过程，表达对生命的敬畏，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '生命的意义', '通过具体事例（如植物生长、动物互助、人的奉献），表达对生命意义的理解，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '四季与生命', '结合四季的变化，描写生命的成长与轮回（如植物发芽、开花、结果），表达感悟，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我与生命的相遇', '写一次与生命相关的经历（如救助小动物、见证植物生长、照顾他人），描写过程和感受，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '珍惜生命', '通过具体事例，说明生命的宝贵，表达珍惜生命、热爱生活的态度，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '生命的力量', '写生命的力量如何战胜困难（如疾病、灾害、挫折），通过具体事例，表达敬佩之情，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- ====== 题库7：想象创意主题训练 (library_id=9) ======

-- 专题练习册1：童话寓言
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (9, '童话寓言', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '小动物的环保行动', '编写一则童话，讲述小动物们合作保护环境的故事，情节生动，蕴含道理，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '骄傲的孔雀', '以孔雀为主角，编写一则寓言，说明“骄傲使人落后”的道理，语言生动，符合动物特点，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '森林里的音乐会', '想象森林里的动物们举办音乐会的场景，描写精彩的表演和欢乐的氛围，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '魔法种子', '编写一个关于“魔法种子”的童话，描写种子的神奇作用，体现善良、勇敢的主题，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '团结的小蚂蚁', '以小蚂蚁为主角，编写一则寓言，说明“团结就是力量”的道理，情节具体，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '月亮的礼物', '想象月亮给大地送礼物的故事，描写礼物的神奇和带来的美好变化，意境优美，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册2：科幻故事
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (9, '科幻故事', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '未来的学校', '想象未来学校的样子（教学方式、设施、课程），描写一次有趣的校园经历，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '太空探险记', '发挥想象，描写一次太空探险的经历（遇到外星人、发现新星球、解决危机），情节惊险有趣，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '智能机器人朋友', '写自己与智能机器人成为朋友的故事，描写机器人的特点和相处的趣事，体现友谊，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '时间旅行机', '想象乘坐时间旅行机回到过去或去往未来的经历，描写所见所闻和感受，逻辑合理，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '环保机器人', '设计一款环保机器人，描写它如何帮助保护环境，解决污染问题，情节具体，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '海底城市', '想象海底有一座城市，描写城市的样子、居民的生活，编写一个发生在海底城市的故事，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册3：奇幻冒险
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (9, '奇幻冒险', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '森林探险记', '想象自己进入森林探险的经历（遇到神秘生物、解开谜题、克服困难），情节紧张刺激，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '山洞里的秘密', '描写发现一个神秘山洞后，进入山洞探索秘密的过程，充满悬念，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '魔法王国之旅', '想象进入魔法王国的经历（学习魔法、认识朋友、打败邪恶），情节丰富，充满童趣，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '沙漠寻宝记', '描写在沙漠中寻找宝藏的经历，描写遇到的困难（缺水、沙尘暴）和如何克服，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '梦境冒险', '想象自己在梦境中的冒险（如与怪兽战斗、拯救世界、探索未知），情节奇幻，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '海底探险', '描写潜入海底探险的经历（遇到神奇生物、发现沉船、探索珊瑚礁），内容有趣，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册4：物品拟人
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (9, '物品拟人', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '书包里的争吵', '将书包里的文具（铅笔、橡皮、笔记本）拟人化，描写它们之间的争吵和和解，蕴含道理，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '大树的自述', '以大树的第一人称，描写自己的生长过程（发芽、开花、结果）、遇到的困难（风雨、砍伐），表达愿望，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '玩具们的夜晚', '想象夜晚玩具们醒来后的活动（聚会、冒险、解决问题），情节有趣，充满童趣，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '路灯的故事', '将路灯拟人化，描写它夜晚的所见所闻（帮助他人、见证温暖），表达奉献精神，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '杯子的旅行', '想象一个杯子的旅行经历（被制造、使用、流浪、被珍藏），描写遇到的人和事，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '云朵的冒险', '将云朵拟人化，描写它在天空中的冒险（变成不同形状、帮助他人、旅行），内容生动，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册5：穿越时空
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (9, '穿越时空', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '回到恐龙时代', '想象自己穿越到恐龙时代的经历（见到恐龙、躲避危险、了解习性），情节惊险，符合常识，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '与古人对话', '想象穿越到古代，与一位古人（诗人、科学家、英雄）对话的经历，描写对话内容和感受，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '未来的我', '想象20年后的自己，描写自己的职业、生活和家乡的变化，表达对未来的期待，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '穿越到童话世界', '想象穿越到熟悉的童话世界（如《白雪公主》《灰姑娘》），描写与童话人物的互动，改编或续写故事，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '回到童年', '想象自己回到童年时光，描写重新经历的一件难忘的事，表达对童年的怀念，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '时空救援队', '想象自己是时空救援队的一员，描写一次穿越时空解决危机的经历（如阻止破坏历史、拯救生命），情节紧张，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册6：创意改写
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (9, '创意改写', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '龟兔赛跑新编', '改编“龟兔赛跑”的故事，加入新的情节或角色（如其他动物帮忙、遇到新困难），改变结局或蕴含新道理，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '三只小猪的新房子', '改写“三只小猪”的故事，想象小猪们建造新房子的材料和过程，如何打败大灰狼，情节新颖，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '灰姑娘的现代版', '将“灰姑娘”的故事改编为现代背景，描写灰姑娘的困境和逆袭，体现现代元素，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '狼和小羊新编', '改编“狼和小羊”的故事，让小羊用智慧战胜狼，情节合理，蕴含“智慧比力量更重要”的道理，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '小红帽的冒险', '改写“小红帽”的故事，增加新的冒险情节（如遇到其他动物、智斗大灰狼的同伙），突出小红帽的勇敢和智慧，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '井底之蛙新说', '改编“井底之蛙”的故事，让青蛙跳出井口后有新的经历和感悟，蕴含新的道理，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册7：奇思妙想
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (9, '奇思妙想', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '假如我会变', '想象自己拥有“变身”的能力，写出想变成什么、做什么事，内容积极向上，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的神奇口袋', '想象自己有一个神奇口袋，能拿出各种东西（如勇气、知识、帮助他人的工具），描写使用口袋的经历，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '会说话的植物', '想象植物们能说话，描写它们之间的对话或与人类的互动，体现环保或友谊的主题，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '不用睡觉的世界', '想象一个不用睡觉的世界，描写人们的生活变化（工作、学习、娱乐），有优点也有烦恼，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '我的理想世界', '畅想自己心中的理想世界（没有战争、环境优美、人人友善），描写世界的样子和人们的生活，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '会飞的房子', '想象自己有一座会飞的房子，描写房子带着自己旅行的经历（去各地看风景、帮助他人），情节有趣，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册8：故事续写
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (9, '故事续写', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '《小猫钓鱼》续写', '续写“小猫钓鱼”的故事，描写小猫后来如何坚持钓鱼，或遇到新的挑战，体现成长，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '《皇帝的新装》续写', '续写“皇帝的新装”，描写皇帝发现真相后的反应和做法，蕴含道理，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '《卖火柴的小女孩》续写', '为“卖火柴的小女孩”编写一个温暖的结局（如被救助、拥有幸福生活），情节感人，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '《丑小鸭》续写', '续写“丑小鸭”变成白天鹅后的经历（如回到家乡、帮助他人、遇到新的挑战），体现善良和勇敢，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '《小红帽》续写', '续写“小红帽”的故事，描写她再次遇到危险时如何应对，体现智慧和成长，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '《狼来了》续写', '续写“狼来了”的故事，描写牧童改正错误后，如何获得大家的信任，蕴含“诚实可贵”的道理，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- ====== 题库8：应用文专项训练 (library_id=10) ======

-- 专题练习册1：书信
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (10, '书信', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给远方亲友的一封信', '格式正确，介绍自己的学习、生活或家乡的变化，表达思念之情，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给老师的一封信', '格式规范，向老师汇报自己的成长，感谢老师的教导，提出自己的困惑或建议，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给同学的道歉信', '格式正确，针对自己的错误（如误会、争吵、伤害）向同学道歉，表达诚意，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给未来自己的一封信', '格式规范，畅想未来的自己，写下现在的愿望和努力方向，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给环保部门的一封信', '格式正确，反映身边的环境问题（如污染、破坏），提出合理建议，表达环保决心，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给小动物的一封信', '格式规范，以友好的语气与小动物对话，表达关爱之情，呼吁保护它们，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册2：日记
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (10, '日记', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '第一次____》日记  
要求：补充题目（如做饭、独自回家、参加比赛），格式正确（日期、星期、天气），描写过程和感受，不少于450字。
2. 《难忘的一天》日记  
要求：格式规范，记录一天中印象最深的事（如旅行、活动、感动的瞬间），写出所见、所闻、所感，不少于400字。
3. 《观察日记：____的生长', '补充题目（如绿豆、多肉、向日葵），连续记录3-5天的生长变化，描写细致，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '节日日记', '格式正确，记录一个节日的经历（如春节、中秋、生日），描写节日氛围和自己的心情，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '学习日记：攻克难题', '格式规范，记录自己攻克一道学习难题的过程，描写遇到的困难和解决方法，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '心情日记', '格式正确，记录自己一天的心情变化（如从开心到失落、从紧张到放松），写出原因和感受，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册3：倡议书
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (10, '倡议书', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '节约用水倡议书', '格式正确（标题、称呼、正文、倡议人、日期），说明节约用水的重要性，提出具体倡议内容，语言有号召力，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '爱护校园环境倡议书', '格式规范，针对校园环境问题，提出具体的爱护措施（如不乱扔垃圾、爱护绿植），呼吁同学们参与，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '多读书倡议书', '格式正确，说明读书的好处，提出读书倡议（如每日读书、分享读书心得），语言有感染力，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '文明出行倡议书', '格式规范，针对出行中的不文明行为（如闯红灯、乱扔垃圾），提出文明出行建议，呼吁大家遵守，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '关爱老人倡议书', '格式正确，说明关爱老人的意义，提出具体关爱方式（如看望、帮助、陪伴），呼吁社会关注，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '节约粮食倡议书', '格式规范，结合“光盘行动”，说明节约粮食的重要性，提出具体措施，语言得体，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册4：建议书
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (10, '建议书', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给学校的建议书', '格式正确，针对学校的某一问题（如食堂、操场、课程），提出合理建议，说明建议的原因和具体内容，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给家长的建议书', '格式规范，向家长提出合理建议（如多陪伴、少唠叨、支持兴趣），表达自己的想法，语气诚恳，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给社区的建议书', '格式正确，针对社区的问题（如设施、环境、活动），提出改进建议，说明好处，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给班级的建议书', '格式规范，为班级建设提出建议（如班级活动、学习氛围、团结互助），具体可行，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给图书馆的建议书', '格式正确，针对图书馆的问题（如书籍种类、借阅规则、环境），提出改进建议，不少于450字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '给网络平台的建议书', '格式规范，针对网络中的问题（如不良信息、沉迷游戏），提出合理建议，呼吁健康上网，不少于400字。', 'SUBJECTIVE', 100, NOW());

-- 专题练习册5：通知
-- 先创建练习册
INSERT INTO practice_book (library_id, name, created_at) VALUES (10, '通知', NOW());
SET @book_id = LAST_INSERT_ID();

-- 插入6道题目
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '班级活动通知', '格式正确（标题、称呼、正文、落款、日期），说明活动的目的、时间、地点、内容和要求，语言简洁明了，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '志愿服务通知', '格式规范，通知同学们参加志愿服务活动，说明活动的主题、时间、地点、任务和注意事项，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '读书分享会通知', '格式正确，通知班级同学参加读书分享会，说明活动的时间、地点、分享内容和准备要求，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '家长会通知', '格式规范，以班主任的名义通知家长参加家长会，说明会议的时间、地点、主题和注意事项，语言得体，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '校园大扫除通知', '格式正确，通知各班级参加校园大扫除，说明时间、区域、任务和标准，简洁明确，不少于400字。', 'SUBJECTIVE', 100, NOW());
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) VALUES (@book_id, '节日活动通知', '格式规范，通知同学们参加节日活动（如元旦、国庆），说明活动的内容、时间、地点和报名方式，不少于400字。', 'SUBJECTIVE', 100, NOW());