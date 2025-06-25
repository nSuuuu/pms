package com.niit.service;

import com.niit.entity.User;
import com.niit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Integer id);
    void deleteUser(Integer id);
    boolean isUsernameExists(String username);
    boolean isPhoneExists(String phone);
    User saveUser(User user);
}