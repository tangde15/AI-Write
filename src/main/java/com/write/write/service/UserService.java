package com.write.write.service;

import com.write.write.entity.UserAccount;
import com.write.write.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 注册：用户名不存在则加密密码并保存
    public void register(String username, String password) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        // BCrypt加密密码
        String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        // 保存用户
        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setPasswordHash(encryptedPassword);
        userRepository.save(user);
    }

    // 验证：检查用户名和密码是否匹配
    public UserAccount verify(String username, String password) {
        // 根据用户名查询用户
        return userRepository.findByUsername(username)
                // 验证密码是否匹配
                .filter(user -> BCrypt.checkpw(password, user.getPasswordHash()))
                // 匹配返回用户，否则返回null
                .orElse(null);
    }
}
