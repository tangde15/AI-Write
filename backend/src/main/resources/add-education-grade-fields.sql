-- 为user_account表添加学历和年级字段
-- 如果字段已存在则忽略错误

ALTER TABLE user_account 
ADD COLUMN IF NOT EXISTS education_level VARCHAR(20) COMMENT '学历：PRIMARY(小学) / MIDDLE(初中)';

ALTER TABLE user_account 
ADD COLUMN IF NOT EXISTS grade VARCHAR(20) COMMENT '年级：GRADE_1 ~ GRADE_6 (小学) / GRADE_1 ~ GRADE_3 (初中)';


