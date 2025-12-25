package com.write.write.service;

import com.write.write.entity.Conversation;
import com.write.write.entity.PrivateMessage;
import com.write.write.entity.UserAccount;
import com.write.write.repository.ConversationRepository;
import com.write.write.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationService {
    
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    
    /**
     * 获取用户的会话列表
     */
    public List<Conversation> getUserConversations(Long userId) {
        return conversationRepository.findByUserIdOrderByLastMessageTimeDesc(userId);
    }
    
    /**
     * 更新会话信息（发送消息后）
     */
    @Transactional
    public void updateConversation(PrivateMessage message) {
        Long senderId = message.getSender().getId();
        Long receiverId = message.getReceiver().getId();
        
        // 更新发送者的会话
        updateOrCreateConversation(senderId, receiverId, message, false);
        
        // 更新接收者的会话（增加未读数）
        updateOrCreateConversation(receiverId, senderId, message, true);
    }
    
    private void updateOrCreateConversation(Long userId, Long friendId, 
                                           PrivateMessage message, boolean incrementUnread) {
        Optional<Conversation> optional = conversationRepository.findByUserIdAndFriendId(userId, friendId);
        Conversation conversation;
        
        if (optional.isPresent()) {
            conversation = optional.get();
        } else {
            conversation = new Conversation();
            UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            UserAccount friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("好友不存在"));
            conversation.setUser(user);
            conversation.setFriend(friend);
            conversation.setUnreadCount(0);
        }
        
        conversation.setLastMessageId(message.getId());
        conversation.setLastMessageContent(message.getContent());
        conversation.setLastMessageTime(message.getCreatedAt());
        
        if (incrementUnread) {
            conversation.setUnreadCount(conversation.getUnreadCount() + 1);
        }
        
        conversationRepository.save(conversation);
    }
    
    /**
     * 标记会话为已读
     */
    @Transactional
    public void markAsRead(Long userId, Long friendId) {
        Optional<Conversation> optional = conversationRepository.findByUserIdAndFriendId(userId, friendId);
        optional.ifPresent(conversation -> {
            conversation.setUnreadCount(0);
            conversationRepository.save(conversation);
        });
    }
    
    /**
     * 获取总未读数
     */
    public Long getTotalUnreadCount(Long userId) {
        Long count = conversationRepository.getTotalUnreadCount(userId);
        return count != null ? count : 0L;
    }
}
