package com.write.write.repository;

import com.write.write.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    
    /**
     * 查询用户的所有会话，按最后消息时间倒序
     */
    @Query("SELECT c FROM Conversation c WHERE c.user.id = :userId " +
           "ORDER BY c.lastMessageTime DESC")
    List<Conversation> findByUserIdOrderByLastMessageTimeDesc(@Param("userId") Long userId);
    
    /**
     * 查询两个用户之间的会话
     */
    Optional<Conversation> findByUserIdAndFriendId(Long userId, Long friendId);
    
    /**
     * 获取用户的未读消息总数
     */
    @Query("SELECT SUM(c.unreadCount) FROM Conversation c WHERE c.user.id = :userId")
    Long getTotalUnreadCount(@Param("userId") Long userId);
}
