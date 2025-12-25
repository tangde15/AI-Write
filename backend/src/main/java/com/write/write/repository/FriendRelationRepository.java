package com.write.write.repository;

import com.write.write.entity.FriendRelation;
import com.write.write.entity.FriendRelation.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRelationRepository extends JpaRepository<FriendRelation, Long> {
    
    /**
     * 查询两个用户之间的好友关系
     */
    @Query("SELECT f FROM FriendRelation f WHERE " +
           "(f.user.id = :userId AND f.friend.id = :friendId) OR " +
           "(f.user.id = :friendId AND f.friend.id = :userId)")
    Optional<FriendRelation> findRelationBetween(@Param("userId") Long userId, 
                                                   @Param("friendId") Long friendId);
    
    /**
     * 查询用户的所有好友关系
     */
    @Query("SELECT f FROM FriendRelation f WHERE " +
           "(f.user.id = :userId OR f.friend.id = :userId) AND f.status = :status")
    List<FriendRelation> findByUserIdAndStatus(@Param("userId") Long userId, 
                                                 @Param("status") FriendStatus status);
    
    /**
     * 查询收到的好友请求
     */
    List<FriendRelation> findByFriendIdAndStatus(Long friendId, FriendStatus status);
    
    /**
     * 查询指定用户向指定好友发送的指定状态的请求
     */
    Optional<FriendRelation> findByUserIdAndFriendIdAndStatus(Long userId, Long friendId, FriendStatus status);
    
    /**
     * 检查是否已经是好友
     */
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FriendRelation f WHERE " +
           "((f.user.id = :userId AND f.friend.id = :friendId) OR " +
           "(f.user.id = :friendId AND f.friend.id = :userId)) AND f.status = 'ACCEPTED'")
    boolean areFriends(@Param("userId") Long userId, @Param("friendId") Long friendId);
}
