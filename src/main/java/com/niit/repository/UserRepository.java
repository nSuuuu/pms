package com.niit.repository;

import com.niit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Find user by phone number
    User findByPhone(String phone);

    // Count users with a given phone number
    long countByPhone(String phone);

    // Count users with a given username
    long countByUsername(String username);

    // Find user by ID (already provided by JpaRepository)
    // Optional<User> findById(Integer id);

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

}