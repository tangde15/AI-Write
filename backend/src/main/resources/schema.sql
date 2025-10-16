CREATE TABLE IF NOT EXISTS user_account (
                                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS writing_record (
                                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                              user_id BIGINT,
                                              topic VARCHAR(255),
    essay TEXT,
    ai_response TEXT,
    teacher_feedback TEXT,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_record_user FOREIGN KEY (user_id) REFERENCES user_account(id)
    ) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS writing_progress (
                                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                student_id BIGINT,
                                                avg_score FLOAT,
                                                improvement_rate FLOAT,
                                                date DATETIME
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
