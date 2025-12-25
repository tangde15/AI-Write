package com.write.write.service;

import com.write.write.entity.FriendRelation;
import com.write.write.entity.FriendRelation.FriendStatus;
import com.write.write.entity.UserAccount;
import com.write.write.repository.FriendRelationRepository;
import com.write.write.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {
    
    private final FriendRelationRepository friendRelationRepository;
    private final UserRepository userRepository;
    
    /**
     * 搜索用户（通过账号）
     */
    public List<UserAccount> searchUserByAccount(String account) {
        List<UserAccount> users = new ArrayList<>();
        Optional<UserAccount> user = userRepository.findByAccount(account);
        user.ifPresent(users::add);
        return users;
    }
    
    /**
     * 发送好友请求
     */
    @Transactional
    public FriendRelation sendFriendRequest(Long userId, Long friendId) {
        // 检查是否已存在关系
        Optional<FriendRelation> existing = friendRelationRepository.findRelationBetween(userId, friendId);
        if (existing.isPresent()) {
            throw new RuntimeException("好友关系已存在");
        }
        
        // 不能添加自己为好友
        if (userId.equals(friendId)) {
            throw new RuntimeException("不能添加自己为好友");
        }
        
        UserAccount user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        UserAccount friend = userRepository.findById(friendId)
            .orElseThrow(() -> new RuntimeException("好友不存在"));
        
        FriendRelation relation = new FriendRelation();
        relation.setUser(user);
        relation.setFriend(friend);
        relation.setStatus(FriendStatus.PENDING);
        
        return friendRelationRepository.save(relation);
    }
    
    /**
     * 接受好友请求
     */
    @Transactional
    public void acceptFriendRequest(Long relationId, Long currentUserId) {
        FriendRelation relation = friendRelationRepository.findById(relationId)
            .orElseThrow(() -> new RuntimeException("好友请求不存在"));
        
        // 只有接收方可以接受请求
        if (!relation.getFriend().getId().equals(currentUserId)) {
            throw new RuntimeException("无权限接受此请求");
        }
        
        relation.setStatus(FriendStatus.ACCEPTED);
        friendRelationRepository.save(relation);
    }
    
    /**
     * 拒绝好友请求
     */
    @Transactional
    public void rejectFriendRequest(Long relationId, Long currentUserId) {
        FriendRelation relation = friendRelationRepository.findById(relationId)
            .orElseThrow(() -> new RuntimeException("好友请求不存在"));
        
        if (!relation.getFriend().getId().equals(currentUserId)) {
            throw new RuntimeException("无权限拒绝此请求");
        }
        
        relation.setStatus(FriendStatus.REJECTED);
        friendRelationRepository.save(relation);
    }
    
    /**
     * 获取好友列表
     */
    public List<UserAccount> getFriendList(Long userId) {
        List<FriendRelation> relations = friendRelationRepository.findByUserIdAndStatus(userId, FriendStatus.ACCEPTED);
        List<UserAccount> friends = new ArrayList<>();
        
        for (FriendRelation relation : relations) {
            if (relation.getUser().getId().equals(userId)) {
                friends.add(relation.getFriend());
            } else {
                friends.add(relation.getUser());
            }
        }
        
        return friends;
    }
    
    /**
     * 获取收到的好友请求
     */
    public List<FriendRelation> getPendingRequests(Long userId) {
        return friendRelationRepository.findByFriendIdAndStatus(userId, FriendStatus.PENDING);
    }
    
    /**
     * 检查是否是好友关系
     */
    public boolean areFriends(Long userId, Long friendId) {
        return friendRelationRepository.areFriends(userId, friendId);
    }
    
    /**
     * 检查是否已发送好友请求（待处理状态）
     */
    public boolean hasRequestSent(Long userId, Long friendId) {
        Optional<FriendRelation> relation = friendRelationRepository.findByUserIdAndFriendIdAndStatus(
            userId, friendId, FriendStatus.PENDING
        );
        return relation.isPresent();
    }
    
    /**
     * 删除好友
     */
    @Transactional
    public void deleteFriend(Long userId, Long friendId) {
        Optional<FriendRelation> relation = friendRelationRepository.findRelationBetween(userId, friendId);
        relation.ifPresent(friendRelationRepository::delete);
    }
}
