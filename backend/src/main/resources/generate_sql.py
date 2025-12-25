#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
根据"需要插入的数据.txt"文件生成完整的SQL插入脚本
"""

import re

# 读取数据文件
with open(r'E:\Project Practice\Write\需要插入的数据.txt', 'r', encoding='utf-8') as f:
    content = f.read()

# 将文件分割成题库
libraries = re.split(r'\n## 题库[一二三四五六七八十]：', content)

sql_output = []
sql_output.append("-- 完整题目数据插入脚本（基于需要插入的数据.txt）")
sql_output.append("SET NAMES utf8mb4;")
sql_output.append("SET CHARACTER SET utf8mb4;")
sql_output.append("SET @teacher_id = 1;  -- 默认教师ID")
sql_output.append("")

# 处理每个题库
for idx, lib_content in enumerate(libraries[1:], start=1):  # 跳过第一个空元素
    lib_title = lib_content.split('\n')[0].strip()
    print(f"处理题库 {idx}: {lib_title}")
    
    # 根据标题匹配题库ID（更新后的映射）
    library_id_mapping = {
        "基础写作能力训练": 4,
        "五感描写专项训练": 5,
        "成长励志主题训练": 6,
        "校园生活主题训练": 14,  # 新创建
        "家庭生活主题训练": 15,   # 新创建
        "社会与自然主题训练": 16,  # 新创建
        "想象创意主题训练": 9,
        "应用文专项训练": 10
    }
    
    lib_id = None
    for key, value in library_id_mapping.items():
        if key in lib_title:
            lib_id = value
            break
    
    if lib_id is None:
        print(f"  跳过题库（未在数据库中找到）: {lib_title}")
        continue
    
    sql_output.append(f"\n-- ====== 题库{idx}：{lib_title} (library_id={lib_id}) ======")
    
    # 分割专题练习册
    books = re.split(r'\n### 专题练习册\d+：', lib_content)
    
    for book_idx, book_content in enumerate(books[1:], start=1):  # 跳过第一个空元素
        book_title = book_content.split('\n')[0].strip()
        print(f"  处理专题 {book_idx}: {book_title}")
        
        sql_output.append(f"\n-- 专题练习册{book_idx}：{book_title}")
        
        # 提取题目（处理复杂的题目格式）
        questions_matches = re.findall(
            r'(\d+)\.\s*[《（](.*?)[》）]\s*\n要求：(.*?)(?=\n\d+\.\s*[《（]|\n###|\n##|$)', 
            book_content, 
            re.DOTALL
        )
        
        if not questions_matches:
            print(f"    警告：未找到题目，尝试备用模式")
            questions_matches = re.findall(
                r'(\d+)\.\s*(.*?)\s*\n要求：(.*?)(?=\n\d+\.|\n###|\n##|$)',
                book_content,
                re.DOTALL
            )
        
        if questions_matches and len(questions_matches) > 0:
            sql_output.append(f"-- 先创建练习册")
            sql_output.append(f"INSERT INTO practice_book (library_id, name, created_at) VALUES ({lib_id}, '{book_title}', NOW());")
            sql_output.append(f"SET @book_id = LAST_INSERT_ID();")
            sql_output.append(f"")
            sql_output.append(f"-- 插入6道题目")
            
            for q_num, q_title, q_requirement in questions_matches[:6]:  # 最多取6道题
                # 清理标题和要求文本
                q_title = q_title.strip().replace("'", "\\'")
                q_requirement = q_requirement.strip().replace("'", "\\'").replace('\n', ' ')
                
                sql_output.append(
                    f"INSERT INTO practice_question (book_id, title, requirement, type, max_score, created_at) "
                    f"VALUES (@book_id, '{q_title}', '{q_requirement}', 'SUBJECTIVE', 100, NOW());"
                )
            
            print(f"    成功提取 {min(len(questions_matches), 6)} 道题目")
        else:
            print(f"    错误：未找到任何题目！")

# 写入SQL文件
output_file = r'E:\Project Practice\Write\backend\src\main\resources\insert-complete-practice-data.sql'
with open(output_file, 'w', encoding='utf-8') as f:
    f.write('\n'.join(sql_output))

print(f"\nSQL脚本已生成: {output_file}")
print(f"总行数: {len(sql_output)}")
print("还需要为题库7、8、9、10创建练习册和题目")
