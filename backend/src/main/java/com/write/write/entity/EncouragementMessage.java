package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class EncouragementMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 被激励的学生 **/
    @ManyToOne
    private UserAccount student;

    /** 发送者（教师、家长、AI 可为空） **/
    @ManyToOne
    private UserAccount sender;

    /** 来源角色：TEACHER / PARENT / AI **/
    private String fromRole;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
}
