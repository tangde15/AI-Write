package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 好友关系实体
 */
@Data
@Entity
@Table(name = "friend_relation", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"}))
public class FriendRelation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private UserAccount friend;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FriendStatus status = FriendStatus.PENDING;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum FriendStatus {
        PENDING,   // 待确认
        ACCEPTED,  // 已接受
        REJECTED   // 已拒绝
    }
}
