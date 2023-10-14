package com.silvioromano.todo.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tb_user")
public class UserModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID uuid;

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(length = 80, nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
