package com.write.write.repository;

import com.write.write.entity.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    
    /**
     * 获取我与某个人的所有对话消息
     */
    @Query("SELECT m FROM PrivateMessage m WHERE " +
           "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
           "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
           "ORDER BY m.createdAt ASC")
    List<PrivateMessage> getConversation(@Param("userId1") Long userId1, 
                                        @Param("userId2") Long userId2);
    
    /**
     * 获取我与某个人的最后一条消息
     */
    @Query("SELECT m FROM PrivateMessage m WHERE " +
           "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
           "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
           "ORDER BY m.createdAt DESC")
    List<PrivateMessage> getLastMessage(@Param("userId1") Long userId1, 
                                       @Param("userId2") Long userId2);
    
    /**
     * 统计未读消息数
     */
    long countByReceiverIdAndIsRead(Long receiverId, Boolean isRead);
    
    /**
     * 获取所有与我有对话的用户ID
     */
    @Query("SELECT DISTINCT " +
           "CASE WHEN m.sender.id = :userId THEN m.receiver.id " +
           "ELSE m.sender.id END " +
           "FROM PrivateMessage m " +
           "WHERE m.sender.id = :userId OR m.receiver.id = :userId")
    List<Long> findConversationPartners(@Param("userId") Long userId);

    /**
     * 获取与指定用户的对话（需要当前用户ID和对方用户ID）
     */
    @Query("SELECT m FROM PrivateMessage m WHERE " +
           "(m.sender.id = :currentUserId AND m.receiver.id = :otherUserId) OR " +
           "(m.sender.id = :otherUserId AND m.receiver.id = :currentUserId) " +
           "ORDER BY m.createdAt ASC")
    List<PrivateMessage> getChatHistory(@Param("currentUserId") Long currentUserId, 
                                        @Param("otherUserId") Long otherUserId);

    /**
     * 获取最近的联系人和消息
     */
    @Query(value = "SELECT m.* FROM private_message m " +
           "WHERE m.id IN (" +
           "  SELECT MAX(id) FROM private_message " +
           "  GROUP BY CASE WHEN sender_id < receiver_id THEN CONCAT(sender_id, '-', receiver_id) " +
           "           ELSE CONCAT(receiver_id, '-', sender_id) END" +
           ") ORDER BY m.created_at DESC", 
           nativeQuery = true)
    List<PrivateMessage> getRecentContacts();
}




