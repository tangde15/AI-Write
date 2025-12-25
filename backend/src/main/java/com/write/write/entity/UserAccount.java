package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String account;

    @Column(nullable = false)
    private String password;

    /** 用户角色：STUDENT / TEACHER / PARENT **/
    @Column(nullable = false)
    private String role;

    /** 学历：PRIMARY(小学) / MIDDLE(初中) **/
    @Column
    private String educationLevel;

    /** 年级：GRADE_1 ~ GRADE_6 (小学) / GRADE_1 ~ GRADE_3 (初中) **/
    @Column
    private String grade;

    /** 教师绑定码（仅教师角色使用） */
    @Column(unique = true)
    private String bindingCode;

    /** 绑定码更新时间 */
    @Column
    private java.time.LocalDateTime bindingCodeUpdatedAt;
}
