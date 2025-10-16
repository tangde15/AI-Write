-- 初始化测试账户
INSERT INTO user_account (username, password, role)
SELECT 'student1', '123456', 'STUDENT'
    WHERE NOT EXISTS (SELECT 1 FROM user_account WHERE username = 'student1');

INSERT INTO user_account (username, password, role)
SELECT 'teacher1', '123456', 'TEACHER'
    WHERE NOT EXISTS (SELECT 1 FROM user_account WHERE username = 'teacher1');

INSERT INTO user_account (username, password, role)
SELECT 'parent1', '123456', 'PARENT'
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
