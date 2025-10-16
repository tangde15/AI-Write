package com.write.write.service;

import com.write.write.entity.UserAccount;
import com.write.write.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        user.setPassword(password);
        user.setRole(role != null ? role.toUpperCase() : "STUDENT");
        userRepository.save(user);
    }

    /** 用户验证 **/
    public UserAccount verify(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }
}
