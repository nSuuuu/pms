package com.niit.service;

import com.niit.entity.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Integer id);
    void deleteUser(Integer id);
    boolean isUsernameExists(String username);
    boolean isPhoneExists(String phone);
    User saveUser(User user);
}