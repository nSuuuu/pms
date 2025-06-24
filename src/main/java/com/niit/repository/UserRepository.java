package com.niit.repository;

import com.niit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户对象
     */
    User findByPhone(String phone);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在返回大于0的值，不存在返回0
     */
    int countByUsername(String username);

    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @return 存在返回大于0的值，不存在返回0
     */
    int countByPhone(String phone);
}