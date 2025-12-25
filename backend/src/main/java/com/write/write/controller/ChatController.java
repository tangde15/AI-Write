package com.write.write.controller;

import com.write.write.dto.ChatMessageDTO;
import com.write.write.entity.Conversation;
import com.write.write.entity.PrivateMessage;
import com.write.write.entity.UserAccount;
import com.write.write.service.ConversationService;
import com.write.write.service.FriendService;
import com.write.write.service.MessageService;
import com.write.write.service.UserService;
import com.write.write.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * WebSocket聊天控制器
 * 处理实时聊天消息
 */
@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final UserService userService;
    private final FriendService friendService;
    private final ConversationService conversationService;

    /**
     * 处理聊天消息
     * 客户端发送到 /app/chat/send
     */
    @MessageMapping("/chat/send")
    public void handleChatMessage(@Payload ChatMessageDTO message, StompHeaderAccessor accessor) {
        try {
            log.info("[WebSocket] 收到聊天消息: 从 {} 到 {}", message.getSenderUsername(), message.getReceiverUsername());

            // 获取发送者和接收者信息
            Optional<UserAccount> sender = userService.findByUsername(message.getSenderUsername());
            Optional<UserAccount> receiver = userService.findByUsername(message.getReceiverUsername());

            if (sender.isEmpty() || receiver.isEmpty()) {
                log.error("[WebSocket] 用户不存在");
                return;
            }

            // 检查是否是好友关系
            if (!friendService.areFriends(sender.get().getId(), receiver.get().getId())) {
                log.error("[WebSocket] 非好友关系，无法发送消息");
                return;
            }

            // 保存消息到数据库
            PrivateMessage privateMessage = new PrivateMessage();
            privateMessage.setSender(sender.get());
            privateMessage.setReceiver(receiver.get());
            privateMessage.setContent(message.getContent());
            privateMessage.setIsRead(false);
            privateMessage.setCreatedAt(LocalDateTime.now());
            privateMessage.setUpdatedAt(LocalDateTime.now());

            PrivateMessage savedMessage = messageService.savePrivateMessage(privateMessage);

            // 更新会话信息
            conversationService.updateConversation(savedMessage);

            // 构建返回消息
            ChatMessageDTO response = new ChatMessageDTO();
            response.setId(savedMessage.getId());
            response.setSenderId(sender.get().getId());
            response.setSenderUsername(sender.get().getUsername());
            response.setSenderRole(sender.get().getRole());
            response.setReceiverId(receiver.get().getId());
            response.setReceiverUsername(receiver.get().getUsername());
            response.setReceiverRole(receiver.get().getRole());
            response.setContent(message.getContent());
            response.setIsRead(false);
            response.setCreatedAt(savedMessage.getCreatedAt());
            response.setMessageType("chat");

            // 发送给接收者的个人队列
            String receiverQueue = "/queue/messages/" + receiver.get().getId();
            messagingTemplate.convertAndSend(receiverQueue, response);

            // 发送给发送者的个人队列（确认已发送）
            String senderQueue = "/queue/messages/" + sender.get().getId();
            messagingTemplate.convertAndSend(senderQueue, response);

            log.info("[WebSocket] 消息已发送并保存: {}", savedMessage.getId());
        } catch (Exception e) {
            log.error("[WebSocket] 处理消息失败", e);
        }
    }

    /**
     * 标记消息为已读
     */
    @MessageMapping("/chat/read")
    public void handleReadMessage(@Payload Long messageId) {
        try {
            log.info("[WebSocket] 标记消息为已读: {}", messageId);
            messageService.markMessageAsRead(messageId);
        } catch (Exception e) {
            log.error("[WebSocket] 标记消息已读失败", e);
        }
    }

    /**
     * 用户上线通知
     */
    @MessageMapping("/chat/online")
    public void handleUserOnline(@Payload ChatMessageDTO message, StompHeaderAccessor accessor) {
        try {
            log.info("[WebSocket] 用户上线: {}", message.getSenderUsername());

            ChatMessageDTO notification = new ChatMessageDTO();
            notification.setSenderUsername(message.getSenderUsername());
            notification.setMessageType("system");
            notification.setContent(message.getSenderUsername() + " 已上线");
            notification.setCreatedAt(LocalDateTime.now());

            // 广播给所有订阅者
            messagingTemplate.convertAndSend("/topic/users/online", notification);
        } catch (Exception e) {
            log.error("[WebSocket] 用户上线通知失败", e);
        }
    }

    /**
     * 用户下线通知
     */
    @MessageMapping("/chat/offline")
    public void handleUserOffline(@Payload ChatMessageDTO message) {
        try {
            log.info("[WebSocket] 用户下线: {}", message.getSenderUsername());

            ChatMessageDTO notification = new ChatMessageDTO();
            notification.setSenderUsername(message.getSenderUsername());
            notification.setMessageType("system");
            notification.setContent(message.getSenderUsername() + " 已离线");
            notification.setCreatedAt(LocalDateTime.now());

            // 广播给所有订阅者
            messagingTemplate.convertAndSend("/topic/users/online", notification);
        } catch (Exception e) {
            log.error("[WebSocket] 用户下线通知失败", e);
        }
    }

    /**
     * REST API：获取与指定用户的聊天历史
     */
    @GetMapping("/history/{otherUserId}")
    public List<ChatMessageDTO> getChatHistory(@PathVariable Long otherUserId) {
        log.info("[Chat] 获取聊天历史: 与用户ID {} 的对话", otherUserId);

        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        // 检查是否是好友关系
        if (!friendService.areFriends(currentUserId, otherUserId)) {
            log.error("[Chat] 非好友关系，无法查看聊天历史");
            return new ArrayList<>();
        }

        List<PrivateMessage> messages = messageService.getChatHistory(currentUserId, otherUserId);
        List<ChatMessageDTO> result = new ArrayList<>();

        for (PrivateMessage msg : messages) {
            ChatMessageDTO dto = new ChatMessageDTO();
            dto.setId(msg.getId());
            dto.setSenderId(msg.getSender().getId());
            dto.setSenderUsername(msg.getSender().getUsername());
            dto.setSenderRole(msg.getSender().getRole());
            dto.setReceiverId(msg.getReceiver().getId());
            dto.setReceiverUsername(msg.getReceiver().getUsername());
            dto.setReceiverRole(msg.getReceiver().getRole());
            dto.setContent(msg.getContent());
            dto.setIsRead(msg.getIsRead());
            dto.setCreatedAt(msg.getCreatedAt());
            result.add(dto);
        }

        return result;
    }

    /**
     * REST API：获取联系人列表（最近聊天）- 基于会话表
     */
    @GetMapping("/contacts")
    public List<Map<String, Object>> getContacts() {
        log.info("[Chat] 获取联系人列表");
        
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<Conversation> conversations = conversationService.getUserConversations(currentUserId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Conversation conv : conversations) {
            Map<String, Object> contactInfo = new HashMap<>();
            UserAccount friend = conv.getFriend();
            
            contactInfo.put("id", friend.getId());
            contactInfo.put("username", friend.getUsername());
            contactInfo.put("role", friend.getRole());
            contactInfo.put("lastMessage", conv.getLastMessageContent());
            contactInfo.put("lastMessageTime", conv.getLastMessageTime());
            contactInfo.put("unreadCount", conv.getUnreadCount());
            
            result.add(contactInfo);
        }

        return result;
    }

    /**
     * REST API：标记会话为已读（清除未读数）
     */
    @PostMapping("/read/{friendId}")
    public ResponseEntity<?> markConversationAsRead(@PathVariable Long friendId) {
        try {
            log.info("[Chat] 标记会话为已读: friendId={}", friendId);
            Long currentUserId = SecurityUtils.getCurrentUserId();
            conversationService.markAsRead(currentUserId, friendId);
            return ResponseEntity.ok(Map.of("message", "已标记为已读"));
        } catch (Exception e) {
            log.error("[Chat] 标记会话已读失败", e);
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
