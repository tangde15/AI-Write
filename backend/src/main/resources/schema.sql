CREATE TABLE IF NOT EXISTS user_account (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          username VARCHAR(100) NOT NULL UNIQUE,
    account VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    education_level VARCHAR(20),
    grade VARCHAR(20)
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS writing_record (
                                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                              user_id BIGINT,
                                              topic VARCHAR(255),
    essay TEXT,
    ai_response TEXT,
    teacher_feedback TEXT,
    score INT,
    previous_record_id BIGINT,
    comparison_analysis TEXT,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_record_user FOREIGN KEY (user_id) REFERENCES user_account(id),
    CONSTRAINT fk_record_previous FOREIGN KEY (previous_record_id) REFERENCES writing_record(id)
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS writing_progress (
                                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                student_id BIGINT,
                                                avg_score FLOAT,
                                                improvement_rate FLOAT,
                                                date DATETIME
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- 练习模块表结构
CREATE TABLE IF NOT EXISTS practice_library (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    author VARCHAR(100),
    total_count INT DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS practice_set (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    creator_id BIGINT,
    created_at DATETIME,
    updated_at DATETIME
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS practice_book (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    library_id BIGINT,
    set_id BIGINT,
    creator_id BIGINT,
    grade_tag VARCHAR(16),
    topic_tags VARCHAR(255),
    status VARCHAR(20) DEFAULT 'DRAFT',
    is_draft TINYINT(1) DEFAULT 1,
    created_at DATETIME,
    updated_at DATETIME,
    closed_at DATETIME,
    INDEX idx_library (library_id),
    INDEX idx_set (set_id),
    CONSTRAINT fk_practice_book_library FOREIGN KEY (library_id) REFERENCES practice_library(id) ON DELETE SET NULL,
    CONSTRAINT fk_practice_book_set FOREIGN KEY (set_id) REFERENCES practice_set(id) ON DELETE SET NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS book_assignment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  book_id BIGINT NOT NULL,
  teacher_id BIGINT NOT NULL,
  student_id BIGINT NOT NULL,
  pushed_at DATETIME NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  read_at DATETIME NULL,
  completed_at DATETIME NULL,
  UNIQUE KEY uk_book_student (book_id, student_id),
  INDEX idx_teacher (teacher_id),
  CONSTRAINT fk_ba_book FOREIGN KEY (book_id) REFERENCES practice_book(id) ON DELETE CASCADE,
  CONSTRAINT fk_ba_teacher FOREIGN KEY (teacher_id) REFERENCES user_account(id) ON DELETE CASCADE,
  CONSTRAINT fk_ba_student FOREIGN KEY (student_id) REFERENCES user_account(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS practice_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT,
    title VARCHAR(255) NOT NULL,
    requirement TEXT,
    type VARCHAR(20) NOT NULL,
    max_score INT DEFAULT 100,
    correct_answer TEXT,
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_book (book_id),
    CONSTRAINT fk_practice_question_book FOREIGN KEY (book_id) REFERENCES practice_book(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS practice_answer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT,
    status VARCHAR(20),
    score INT,
    ai_feedback TEXT,
    teacher_feedback TEXT,
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_q_user (question_id, user_id),
    CONSTRAINT fk_practice_answer_q FOREIGN KEY (question_id) REFERENCES practice_question(id) ON DELETE CASCADE,
    CONSTRAINT fk_practice_answer_user FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS student_teacher (
                                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                               teacher_id BIGINT,
                                               student_id BIGINT
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS student_parent (
                                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                              parent_id BIGINT,
                                              student_id BIGINT
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS encouragement_message (
                                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                     student_id BIGINT,
                                                     sender_id BIGINT,
                                                     from_role VARCHAR(20),
    content TEXT,
    created_at DATETIME,
    CONSTRAINT fk_msg_student FOREIGN KEY (student_id) REFERENCES user_account(id),
    CONSTRAINT fk_msg_sender FOREIGN KEY (sender_id) REFERENCES user_account(id)
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS private_message (
                                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                               sender_id BIGINT NOT NULL,
                                               receiver_id BIGINT NOT NULL,
                                               content TEXT,
                                               is_read TINYINT(1) DEFAULT 0,
                                               created_at DATETIME,
                                               updated_at DATETIME,
                                               INDEX idx_sender (sender_id),
                                               INDEX idx_receiver (receiver_id),
                                               INDEX idx_created (created_at),
                                               CONSTRAINT fk_private_msg_sender FOREIGN KEY (sender_id) REFERENCES user_account(id),
                                               CONSTRAINT fk_private_msg_receiver FOREIGN KEY (receiver_id) REFERENCES user_account(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS sample_essay (
                                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            title VARCHAR(255) NOT NULL,
                                            author_name VARCHAR(100) NOT NULL,
                                            author VARCHAR(100) NOT NULL,
                                            content TEXT NOT NULL,
                                            tag VARCHAR(50),
                                            favorite_count INT DEFAULT 0,
                                            created_at DATETIME,
                                            updated_at DATETIME,
                                            INDEX idx_tag (tag),
                                            INDEX idx_favorite (favorite_count),
                                            INDEX idx_created (created_at)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- 好友关系表
CREATE TABLE IF NOT EXISTS friend_relation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at DATETIME,
    updated_at DATETIME,
    UNIQUE KEY unique_relation (user_id, friend_id),
    INDEX idx_user_status (user_id, status),
    INDEX idx_friend (friend_id),
    CONSTRAINT fk_friend_user FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE,
    CONSTRAINT fk_friend_friend FOREIGN KEY (friend_id) REFERENCES user_account(id) ON DELETE CASCADE,
    CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED'))
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- 会话表
CREATE TABLE IF NOT EXISTS conversation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    last_message_id BIGINT,
    last_message_content TEXT,
    last_message_time DATETIME,
    unread_count INT DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    UNIQUE KEY unique_conversation (user_id, friend_id),
    INDEX idx_user_time (user_id, last_message_time),
    INDEX idx_last_message (last_message_id),
    CONSTRAINT fk_conv_user FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE,
    CONSTRAINT fk_conv_friend FOREIGN KEY (friend_id) REFERENCES user_account(id) ON DELETE CASCADE,
    CONSTRAINT fk_conv_message FOREIGN KEY (last_message_id) REFERENCES private_message(id) ON DELETE SET NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
