-- 为满足非空的 account 字段，此处将 account 与 username 保持一致
INSERT INTO user_account (username, account, password, role)
SELECT 'student1', 'student1', '123456', 'STUDENT'
    WHERE NOT EXISTS (SELECT 1 FROM user_account WHERE username = 'student1');

INSERT INTO user_account (username, account, password, role)
SELECT 'teacher1', 'teacher1', '123456', 'TEACHER'
    WHERE NOT EXISTS (SELECT 1 FROM user_account WHERE username = 'teacher1');

INSERT INTO user_account (username, account, password, role)
SELECT 'parent1', 'parent1', '123456', 'PARENT'
    WHERE NOT EXISTS (SELECT 1 FROM user_account WHERE username = 'parent1');

-- 绑定教师与学生
INSERT INTO student_teacher (teacher_id, student_id)
SELECT
    (SELECT id FROM user_account WHERE username='teacher1'),
    (SELECT id FROM user_account WHERE username='student1')
    WHERE NOT EXISTS (
    SELECT 1 FROM student_teacher
    WHERE teacher_id=(SELECT id FROM user_account WHERE username='teacher1')
      AND student_id=(SELECT id FROM user_account WHERE username='student1')
);

-- 绑定家长与学生
INSERT INTO student_parent (parent_id, student_id)
SELECT
    (SELECT id FROM user_account WHERE username='parent1'),
    (SELECT id FROM user_account WHERE username='student1')
    WHERE NOT EXISTS (
    SELECT 1 FROM student_parent
    WHERE parent_id=(SELECT id FROM user_account WHERE username='parent1')
      AND student_id=(SELECT id FROM user_account WHERE username='student1')
);

-- 示例作文
INSERT INTO writing_record (user_id, topic, essay, ai_response, created_at)
SELECT
    (SELECT id FROM user_account WHERE username='student1'),
    '春天的校园',
    '春天到了，校园里的花开了，小草绿了，同学们在操场上欢笑。',
    '这篇作文描写生动，但可以进一步加入听觉和嗅觉细节来丰富画面。',
    NOW()
    WHERE NOT EXISTS (SELECT 1 FROM writing_record WHERE topic = '春天的校园');

-- 练习模块示例数据（仅在空数据库时插入）
INSERT INTO practice_library (title, description, author, total_count, created_at)
SELECT '小学三年级作文精选', '精选小学三年级优秀作文题目', '张老师', 15, NOW()
WHERE NOT EXISTS (SELECT 1 FROM practice_library);

INSERT INTO practice_set (name, creator_id, created_at)
SELECT '第一单元 写人作文专练', (SELECT id FROM user_account WHERE username='teacher1'), NOW()
WHERE NOT EXISTS (SELECT 1 FROM practice_set);

INSERT INTO practice_book (name, library_id, set_id, creator_id, created_at)
SELECT '写人作文专练',
       (SELECT id FROM practice_library LIMIT 1),
       (SELECT id FROM practice_set LIMIT 1),
       (SELECT id FROM user_account WHERE username='teacher1'), NOW()
WHERE NOT EXISTS (SELECT 1 FROM practice_book);

-- 题目（若不存在则插入若干主观题）
INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at)
SELECT (SELECT id FROM practice_book LIMIT 1), '我的妈妈', '不少于500字，注意人物描写', 'SUBJECTIVE', 100, NOW()
WHERE NOT EXISTS (SELECT 1 FROM practice_question);

INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at)
SELECT (SELECT id FROM practice_book LIMIT 1), '我最敬佩的人', '不少于500字，包含细节', 'SUBJECTIVE', 100, NOW()
WHERE (SELECT COUNT(1) FROM practice_question) < 2;

INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at)
SELECT (SELECT id FROM practice_book LIMIT 1), '我的好朋友', '不少于500字，结构完整', 'SUBJECTIVE', 100, NOW()
WHERE (SELECT COUNT(1) FROM practice_question) < 3;

