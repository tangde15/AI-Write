package com.write.write.service;

import com.write.write.dto.ConversationDTO;
import com.write.write.dto.MessageDTO;
import com.write.write.dto.SendMessageRequest;
import com.write.write.entity.PrivateMessage;
import com.write.write.entity.UserAccount;
import com.write.write.repository.PrivateMessageRepository;
import com.write.write.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final PrivateMessageRepository messageRepository;
    private final UserRepository userRepository;

    /**
     * 发送消息
     */
    public MessageDTO sendMessage(Long senderId, SendMessageRequest request) {
        UserAccount sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("发送者不存在"));
        
        UserAccount receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("接收者不存在"));

        PrivateMessage message = new PrivateMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message.setIsRead(false);

        PrivateMessage saved = messageRepository.save(message);
        return convertToDTO(saved);
    }

    /**
     * 获取对话记录
     */
    public List<MessageDTO> getConversation(Long userId, Long partnerId) {
        List<PrivateMessage> messages = messageRepository.getConversation(userId, partnerId);
        return messages.stream().map(this::convertToDTO).toList();
    }

    /**
     * 获取消息列表（对话摘要）
     */
    public List<ConversationDTO> getConversations(Long userId) {
        List<Long> partnerIds = messageRepository.findConversationPartners(userId);
        List<ConversationDTO> conversations = new ArrayList<>();

        for (Long partnerId : partnerIds) {
            ConversationDTO dto = new ConversationDTO();
            
            // 获取对方信息
            UserAccount partner = userRepository.findById(partnerId).orElse(null);
            if (partner == null) continue;
            
            dto.setPartnerId(partner.getId());
            dto.setPartnerName(partner.getUsername());
            dto.setPartnerRole(partner.getRole());

            // 获取最后一条消息
            List<PrivateMessage> lastMessages = messageRepository.getLastMessage(userId, partnerId);
            if (!lastMessages.isEmpty()) {
                PrivateMessage lastMsg = lastMessages.get(0);
                dto.setLastMessage(lastMsg.getContent());
                dto.setLastMessageTime(formatTime(lastMsg.getCreatedAt()));
                dto.setLastSenderId(lastMsg.getSender().getId());
            }

            // 统计未读数量
            long unreadCount = messageRepository.countByReceiverIdAndIsRead(userId, false);
            dto.setUnreadCount(unreadCount);

            conversations.add(dto);
        }

        return conversations;
    }

    /**
     * 标记消息已读
     */
    public void markAsRead(Long messageId, Long userId) {
        PrivateMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("消息不存在"));

        // 只能标记自己接收的消息为已读
        if (message.getReceiver().getId().equals(userId)) {
            message.setIsRead(true);
            messageRepository.save(message);
        }
    }

    /**
     * 保存私信（用于WebSocket）
     */
    public PrivateMessage savePrivateMessage(PrivateMessage message) {
        return messageRepository.save(message);
    }

    /**
     * 标记消息为已读（WebSocket用）
     */
    public void markMessageAsRead(Long messageId) {
        PrivateMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("消息不存在"));
        message.setIsRead(true);
        message.setUpdatedAt(LocalDateTime.now());
        messageRepository.save(message);
    }

    /**
     * 获取与指定用户的对话（WebSocket/REST用）
     */
    public List<PrivateMessage> getChatHistory(Long currentUserId, Long otherUserId) {
        return messageRepository.getChatHistory(currentUserId, otherUserId);
    }

    /**
     * 获取最近的联系人列表
     */
    public List<PrivateMessage> getRecentContacts() {
        return messageRepository.getRecentContacts();
    }

    /**
     * 获取未读消息数
     */
    public long getUnreadCount(Long userId) {
        return messageRepository.countByReceiverIdAndIsRead(userId, false);
    }

    /**
     * 转换为DTO
     */
    private MessageDTO convertToDTO(PrivateMessage message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getUsername());
        dto.setReceiverId(message.getReceiver().getId());
        dto.setReceiverName(message.getReceiver().getUsername());
        dto.setContent(message.getContent());
        dto.setIsRead(message.getIsRead());
        dto.setCreatedAt(message.getCreatedAt());
        return dto;
    }

    /**
     * 格式化时间
     */
    private String formatTime(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        long hours = java.time.Duration.between(time, now).toHours();

        if (hours < 1) {
            long minutes = java.time.Duration.between(time, now).toMinutes();
            return minutes + "分钟前";
        } else if (hours < 24) {
            return hours + "小时前";
        } else if (hours < 48) {
            return "昨天";
        } else {
            return time.format(DateTimeFormatter.ofPattern("MM-dd"));
        }
    }
}




