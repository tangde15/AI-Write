package com.write.write.repository;

import com.write.write.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
    Optional<UserAccount> findByAccount(String account);
    Optional<UserAccount> findByBindingCode(String bindingCode);
    Optional<UserAccount> findByBindingCodeIgnoreCase(String bindingCode);
}
