package com.niit.controller;


import com.niit.entity.HupRegion;
import com.niit.entity.User;
import com.niit.service.HupRegionService;
import com.niit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private HupRegionService hupRegionService;

    // 显示用户列表
    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }

    // 获取用户数据用于编辑（返回JSON）
    @GetMapping("/edit/{id}")
    @ResponseBody
    public User getUserForEdit(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    // 处理添加用户提交
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addUser(@ModelAttribute User user, BindingResult result) {
        Map<String, String> errors = validateUser(user, result, true);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    // 处理编辑用户提交
    @PostMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> editUser(@PathVariable Integer id, @ModelAttribute User user, BindingResult result) {
        user.setId(id);
        Map<String, String> errors = validateUser(user, result, false);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    // 删除用户
    @GetMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/regions")
    @ResponseBody
    public List<HupRegion> searchRegions(@RequestParam String keyword) {
        return hupRegionService.searchRegions(keyword);
    }

    // 验证用户数据
    private Map<String, String> validateUser(User user, BindingResult result, boolean isNew) {
        Map<String, String> errors = new HashMap<>();

        if (isNew) {
            // 验证用户名是否已存在
            if (userService.isUsernameExists(user.getUsername())) {
                errors.put("username", "用户名已存在");
            }

            // 验证手机号是否已存在
            if (userService.isPhoneExists(user.getPhone())) {
                errors.put("phone", "手机号已存在");
            }
        }

        // 验证真实姓名
        if (!isValidChineseName(user.getRealName())) {
            errors.put("realName", "请输入有效的中文姓名");
        }

        // 验证身份证号
        if (user.getIdCard() != null && !user.getIdCard().isEmpty() &&
                !isValidIdCard(user.getIdCard())) {
            errors.put("idCard", "请输入有效的身份证号码");
        }

        // 验证手机号格式
        if (!isValidPhone(user.getPhone())) {
            errors.put("phone", "请输入有效的手机号码");
        }

        return errors;
    }

    private boolean isValidChineseName(String name) {
        return name.matches("^[\\u4e00-\\u9fa5]{2,10}$");
    }

    private boolean isValidIdCard(String idCard) {
        // 简单的身份证号验证，可根据需求完善
        return idCard.matches("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^1[3-9]\\d{9}$");
    }
}