package com.factoryreceipt.factoryreceipt.controller;

import com.factoryreceipt.factoryreceipt.model.User;
import com.factoryreceipt.factoryreceipt.repository.UserRepository;
import com.factoryreceipt.factoryreceipt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static class LoginRequest {
        public String userId;
        public String password;
    }

    public static class LoginResponse {
        public String token;
        public String userId;
        public String accountType;
        public Integer usageLimit;
        public LocalDateTime expirationDate;

        public LoginResponse(String token, User user) {
            this.token = token;
            this.userId = user.getUserId();
            this.accountType = user.getAccountType();
            this.usageLimit = user.getUsageLimit();
            this.expirationDate = user.getExpirationDate();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUserId(request.userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        if ("time".equalsIgnoreCase(user.getAccountType())) {
            if (user.getExpirationDate() != null && user.getExpirationDate().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account expired");
            }
        }
        if ("limit".equalsIgnoreCase(user.getAccountType())) {
            if (user.getUsageLimit() != null && user.getUsageLimit() <= 0) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usage limit reached");
            }
        }
        // Generujemy token z rolą – zakładamy, że accountType "admin" oznacza rolę admina
        String token = jwtUtil.generateToken(user.getUserId(), user.getAccountType());
        LoginResponse response = new LoginResponse(token, user);
        return ResponseEntity.ok(response);
    }
}
