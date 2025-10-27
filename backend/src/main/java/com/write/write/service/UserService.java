package com.write.write.service;

import com.write.write.entity.UserAccount;
import com.write.write.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /** 用户注册 **/
    public void register(String username, String password, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        UserAccount user = new UserAccount();
        user.setUsername(username);
        // 使用BCrypt加密密码
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setRole(role != null ? role.toUpperCase() : "STUDENT");
        userRepository.save(user);
    }

    /** 用户验证 **/
    public UserAccount verify(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> checkPassword(password, u.getPassword()))
                .orElse(null);
    }
    
    /** 密码校验（兼容BCrypt多版本与明文，并做健壮性处理） **/
    private boolean checkPassword(String rawPassword, String storedPassword) {
        if (storedPassword == null) return false;

        String trimmedStored = storedPassword.trim();
        String trimmedRaw = rawPassword == null ? null : rawPassword.trim();

        // 兼容 $2a$ / $2b$ / $2y$ 前缀的 bcrypt 密文
        boolean looksLikeBcrypt = trimmedStored.startsWith("$2a$")
                || trimmedStored.startsWith("$2b$")
                || trimmedStored.startsWith("$2y$");

        try {
            if (looksLikeBcrypt) {
                boolean match = trimmedRaw != null && BCrypt.checkpw(trimmedRaw, trimmedStored);
                System.out.println("[Auth] BCrypt match=" + match + ", userHashed=\"" + trimmedStored.substring(0, 7) + "...\"");
                return match;
            }
        } catch (Exception ex) {
            System.err.println("[Auth] BCrypt check error: " + ex.getMessage());
        }

        // 兼容历史明文密码
        boolean plainMatch = Objects.equals(trimmedStored, trimmedRaw);
        System.out.println("[Auth] Plain match=" + plainMatch);
        return plainMatch;
    }
}
