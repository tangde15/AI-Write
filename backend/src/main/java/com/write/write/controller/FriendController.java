package com.write.write.controller;

import com.write.write.dto.FriendRequestDTO;
import com.write.write.entity.FriendRelation;
import com.write.write.entity.UserAccount;
import com.write.write.service.FriendService;
import com.write.write.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 好友管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {
    
    private final FriendService friendService;
    
    /**
     * 搜索用户（通过账号）
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam String account) {
        try {
            log.info("[Friend] 搜索用户请求: account={}", account);
            
            // 先获取当前用户ID，如果未登录会直接抛异常
            Long currentUserId = SecurityUtils.getCurrentUserId();
            log.info("[Friend] 当前用户ID: {}", currentUserId);
            
            List<UserAccount> users = friendService.searchUserByAccount(account);
            
            if (users.isEmpty()) {
                log.warn("[Friend] 未找到用户: {}", account);
                return ResponseEntity.ok(Map.of(
                    "found", false,
                    "message", "未找到该用户"
                ));
            }
            
            UserAccount user = users.get(0);
            
            // 检查是否已经是好友
            boolean isFriend = friendService.areFriends(currentUserId, user.getId());
            
            // 检查是否已发送好友请求
            boolean requestSent = friendService.hasRequestSent(currentUserId, user.getId());
            
            Map<String, Object> result = new HashMap<>();
            result.put("found", true);
            result.put("id", user.getId());
            result.put("username", user.getUsername());
            result.put("account", user.getAccount());
            result.put("role", user.getRole());
            result.put("isFriend", isFriend);
            result.put("requestSent", requestSent);
            
            log.info("[Friend] 搜索成功: {}", result);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            log.error("[Friend] 搜索用户失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", true,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("[Friend] 搜索用户发生未知错误", e);
            return ResponseEntity.badRequest().body(Map.of(
                "error", true,
                "message", "服务器内部错误: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 发送好友请求
     */
    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequestDTO dto) {
        try {
            Long currentUserId = SecurityUtils.getCurrentUserId();
            FriendRelation relation = friendService.sendFriendRequest(currentUserId, dto.getFriendId());
            return ResponseEntity.ok(Map.of("message", "好友请求已发送", "id", relation.getId()));
        } catch (Exception e) {
            log.error("[Friend] 发送好友请求失败", e);
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    /**
     * 获取收到的好友请求
     */
    @GetMapping("/requests")
    public ResponseEntity<?> getPendingRequests() {
        try {
            Long currentUserId = SecurityUtils.getCurrentUserId();
            List<FriendRelation> requests = friendService.getPendingRequests(currentUserId);
            List<Map<String, Object>> result = requests.stream().map(relation -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", relation.getId());
                map.put("userId", relation.getUser().getId());
                map.put("username", relation.getUser().getUsername());
                map.put("account", relation.getUser().getAccount());
                map.put("role", relation.getUser().getRole());
                map.put("createdAt", relation.getCreatedAt());
                return map;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("[Friend] 获取好友请求失败", e);
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    /**
     * 接受好友请求
     */
    @PostMapping("/accept/{relationId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long relationId) {
        try {
            Long currentUserId = SecurityUtils.getCurrentUserId();
            friendService.acceptFriendRequest(relationId, currentUserId);
            return ResponseEntity.ok(Map.of("message", "已接受好友请求"));
        } catch (Exception e) {
            log.error("[Friend] 接受好友请求失败", e);
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    /**
     * 拒绝好友请求
     */
    @PostMapping("/reject/{relationId}")
    public ResponseEntity<?> rejectFriendRequest(@PathVariable Long relationId) {
        try {
            Long currentUserId = SecurityUtils.getCurrentUserId();
            friendService.rejectFriendRequest(relationId, currentUserId);
            return ResponseEntity.ok(Map.of("message", "已拒绝好友请求"));
        } catch (Exception e) {
            log.error("[Friend] 拒绝好友请求失败", e);
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    /**
     * 获取好友列表
     */
    @GetMapping("/list")
    public ResponseEntity<?> getFriendList() {
        try {
            Long currentUserId = SecurityUtils.getCurrentUserId();
            List<UserAccount> friends = friendService.getFriendList(currentUserId);
            List<Map<String, Object>> result = friends.stream().map(friend -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", friend.getId());
                map.put("username", friend.getUsername());
                map.put("account", friend.getAccount());
                map.put("role", friend.getRole());
                return map;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("[Friend] 获取好友列表失败", e);
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    /**
     * 删除好友
     */
    @DeleteMapping("/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable Long friendId) {
        try {
            Long currentUserId = SecurityUtils.getCurrentUserId();
            friendService.deleteFriend(currentUserId, friendId);
            return ResponseEntity.ok(Map.of("message", "已删除好友"));
        } catch (Exception e) {
            log.error("[Friend] 删除好友失败", e);
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
