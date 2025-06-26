package com.niit.repository;

import com.niit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByPhone(String phone);
    long countByPhone(String phone);
    long countByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);

    boolean existsByIdCardAndIdNot(String idCard, Integer id);
}