package com.silvioromano.todo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findByUsername(String username);
}
