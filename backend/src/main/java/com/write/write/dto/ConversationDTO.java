package com.write.write.dto;

import lombok.Data;

@Data
public class ConversationDTO {
    private Long partnerId;
    private String partnerName;
    private String partnerRole;
    private String lastMessage;
    private String lastMessageTime;
    private Long unreadCount;
    private Long lastSenderId;
}




