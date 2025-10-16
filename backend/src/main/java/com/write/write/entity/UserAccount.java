package com.write.write.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    /** 用户角色：STUDENT / TEACHER / PARENT **/
    @Column(nullable = false)
    private String role;
}
