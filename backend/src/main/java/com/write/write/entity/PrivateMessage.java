package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 私信消息实体
 * 支持学生、教师、家长三端互相发送消息
 */
@Entity
@Data
@Table(name = "private_message")
public class PrivateMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 发送者 */
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserAccount sender;

    /** 接收者 */
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserAccount receiver;

    /** 消息内容 */
    @Column(columnDefinition = "TEXT")
    private String content;

    /** 是否已读 */
    private Boolean isRead = false;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}




