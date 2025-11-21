package com.factoryreceipt.factoryreceipt.controller;

import com.factoryreceipt.factoryreceipt.dto.AccountCreationRequest;
import com.factoryreceipt.factoryreceipt.dto.AccountCreationResponse;
import com.factoryreceipt.factoryreceipt.dto.AccountDto;
import com.factoryreceipt.factoryreceipt.model.User;
import com.factoryreceipt.factoryreceipt.model.Receipt;
import com.factoryreceipt.factoryreceipt.repository.ReceiptRepository;
import com.factoryreceipt.factoryreceipt.repository.UserRepository;
import com.factoryreceipt.factoryreceipt.service.DeviceRegistrationService;
import com.factoryreceipt.factoryreceipt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private DeviceRegistrationService deviceRegistrationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/account/{userId}")
    public ResponseEntity<?> getAccount(@PathVariable String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        AccountDto accountDto = new AccountDto(
                user.getUserId(),
                user.getEmail(),
                user.getAccountType(),
                user.getUsageLimit(),
                user.getExpirationDate()
        );
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping("/receipts/{userId}")
    public ResponseEntity<?> getReceipts(@PathVariable String userId) {
        List<Receipt> receipts = receiptRepository.findByUserId(userId);
        return ResponseEntity.ok(receipts);
    }

    @PostMapping("/createAccount")
    public ResponseEntity<AccountCreationResponse> createAccount(@RequestBody AccountCreationRequest request) {
        String generatedUserId = generateRandomDigits(5);
        String generatedPassword = generateRandomAlphaNumeric(5);

        String accountType = request.getAccountType();
        LocalDateTime expirationDate = null;
        Integer usageLimit = null;

        if ("time".equalsIgnoreCase(accountType)) {
            int days = (request.getDurationDays() != null && request.getDurationDays() > 0) ? request.getDurationDays() : 1;
            expirationDate = LocalDateTime.now().plusDays(days);
        } else if ("limit".equalsIgnoreCase(accountType)) {
            usageLimit = (request.getUsageLimit() != null && request.getUsageLimit() > 0) ? request.getUsageLimit() : 1;
        } else if ("lifetime".equalsIgnoreCase(accountType)) {
            // Konto lifetime – brak wygaśnięcia i limitu
        } else {
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        user.setUserId(generatedUserId);
        // Hashujemy hasło przed zapisem
        user.setPassword(passwordEncoder.encode(generatedPassword));
        user.setAccountType(accountType);
        user.setExpirationDate(expirationDate);
        user.setUsageLimit(usageLimit);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole("USER"); // domyślna rola dla nowego konta

        userRepository.save(user);

        AccountCreationResponse response = new AccountCreationResponse(
                generatedUserId,
                generatedPassword,
                accountType,
                expirationDate,
                usageLimit
        );

        return ResponseEntity.ok(response);
    }

    // Nowy endpoint logowania z rejestracją urządzeń – zmieniony mapping, aby uniknąć konfliktu
    @PostMapping("/account/login")
    public ResponseEntity<?> login(@RequestParam String userId, @RequestParam String password) {
        User user = userRepository.findByUserId(userId);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niepoprawne dane logowania");
        }
        // Spring Security zajmuje się sesją – nie trzeba generować JWT ani rejestrować urządzenia
        return ResponseEntity.ok("Zalogowano pomyślnie");
    }


    // Metody pomocnicze
    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String generateRandomAlphaNumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    // DTO do logowania
    public static class LoginRequest {
        private String userId;
        private String password;
        private String deviceId; // Opcjonalnie – może być również przekazany przez nagłówek

        public String getUserId() {
            return userId;
        }
        public void setUserId(String userId) {
            this.userId = userId;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public String getDeviceId() {
            return deviceId;
        }
        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }
}
