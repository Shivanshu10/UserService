package com.shivanshu.secexample.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shivanshu.secexample.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // This interface will automatically provide CRUD operations for User entity
    // No need to implement any methods, Spring Data JPA will handle it

    boolean existsByUsername(String username);
}