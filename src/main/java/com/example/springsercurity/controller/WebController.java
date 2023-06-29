package com.example.springsercurity.controller;

import com.example.springsercurity.entity.TokenConfirm;
import com.example.springsercurity.entity.User;
import com.example.springsercurity.repository.TokenConfirmRepository;
import com.example.springsercurity.repository.UserRepository;
import com.example.springsercurity.security.IsAdmin;
import com.example.springsercurity.service.MailService;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;


@Controller
public class WebController {
    @Autowired
    public MailService mailService;
    @Autowired
    private TokenConfirmRepository tokenConfirmRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // Ai cũng có thể vào được
    @GetMapping("/")
    public String getHome() {
        return "index";
    }


    @GetMapping("/login")
    public String getLoginPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }

    // Có role admin hoặc author mới có thể vào
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AUTHOR')")
    @GetMapping("/profile")
    public String getProfile() {
        return "profile";
    }

    // Phải có role admin mới vào được
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @IsAdmin
    @GetMapping("/admin")
    public String getAdmin() {
        return "admin";
    }

    // Phải có role author mới vào được
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @GetMapping("/author")
    public String getAuthor() {
        return "author";
    }

    //quen mk
    //gui email xac nhan quen mk:trong email cos link xac thuc
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "/forgot-password";
    }

    @GetMapping("/api/send-email")
    public ResponseEntity<?> getSendMail() {
        mailService.sendEmail("minhnguyettkh@gmail.com", "nhan tieu de nguoi gui", "xin chao");
        return ResponseEntity.ok("Send mail success");
    }

    @GetMapping("/doi-mat-khau/{token}")
    public String getUpdatePasswordPage(@PathVariable String token, Model model) {
        // Kiểm tra token có hợp lệ hay không
        Optional<TokenConfirm> optionalTokenConfirm = tokenConfirmRepository.findByToken(token);
        if(optionalTokenConfirm.isEmpty()) {
            model.addAttribute("isValid", false);
            model.addAttribute("message", "Token không hợp lệ");
            return "update-password";
        }
        // Kiểm tra token đã được kích hoạt hay chưa
        TokenConfirm tokenConfirm = optionalTokenConfirm.get();
        if(tokenConfirm.getConfirmAt() != null) {
            model.addAttribute("isValid", false);
            model.addAttribute("message", "Token đã được kích hoạt");
            return "update-password";
        }

        // Kiểm tra token đã hết hạn hay chưa
        if(tokenConfirm.getExpiredAt().isBefore(LocalDateTime.now())) {
            model.addAttribute("isValid", false);
            model.addAttribute("message", "Token đã hết hạn");
            return "update-password";
        }

        tokenConfirm.setConfirmAt(LocalDateTime.now());
        tokenConfirmRepository.save(tokenConfirm);
        model.addAttribute("isValid", true);
        model.addAttribute("token", token);
        return "update-password";

    }
    @PostMapping("/doi-mat-khau/{token}")
    public String updatePassword(@RequestBody Map<String, Object> request,
                                 @RequestParam String token,
                                 Model model){

        String password = (String) request.get("password");
        String confirmPassword = (String) request.get("confirmPassword");

        if(!password.equals(confirmPassword)){
            model.addAttribute("error","mk khong khop");
            return"update-password";
        }

        TokenConfirm tokenConfirm =tokenConfirmRepository.findByToken(token).orElseThrow(()->{
            throw  new RuntimeException("k tim thay user");
        });

        User user =  tokenConfirm.getUser();
        if(user==null){
            return "redirect:/login";
        }
        String encryptedPassword=passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        return "thanh cong";
    }


}