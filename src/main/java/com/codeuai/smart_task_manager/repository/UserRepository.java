package com.codeuai.smart_task_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeuai.smart_task_manager.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
