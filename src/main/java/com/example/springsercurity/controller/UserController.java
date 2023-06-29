package com.example.springsercurity.controller;

import com.example.springsercurity.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUserManagement() {
        // Trả về view user_management.html
        return "user_management";
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser() {
        // Logic tạo user
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        // Logic lấy thông tin user theo id và truyền vào model
        // Trả về view edit_user.html
        return "edit_user";
    }

    @PostMapping("/users/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User userDto) {
        // Logic cập nhật thông tin user theo id và userDto
        return "redirect:/users";
    }
}