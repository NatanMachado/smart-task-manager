package com.codeuai.smarttaskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeuai.smarttaskmanager.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
