package com.write.write.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 聊天消息DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {

    /** 消息ID */
    private Long id;

    /** 发送者ID */
    private Long senderId;

    /** 发送者用户名 */
    private String senderUsername;

    /** 发送者角色（STUDENT/TEACHER/PARENT） */
    private String senderRole;

    /** 接收者ID */
    private Long receiverId;

    /** 接收者用户名 */
    private String receiverUsername;

    /** 接收者角色（STUDENT/TEACHER/PARENT） */
    private String receiverRole;

    /** 消息内容 */
    private String content;

    /** 是否已读 */
    private Boolean isRead;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 消息类型：chat（普通消息）、system（系统消息）、notification（通知） */
    private String messageType = "chat";

    /** 来源会话ID（用于UI标识） */
    private String conversationId;
}
