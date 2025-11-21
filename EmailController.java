package com.factoryreceipt.factoryreceipt.controller;

import com.factoryreceipt.factoryreceipt.model.User;
import com.factoryreceipt.factoryreceipt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/account")
public class EmailController {

    @Autowired
    private UserRepository userRepository;

    // Klasa do przyjmowania danych JSON: { "userId": "...", "newEmail": "..." }
    public static class UpdateEmailRequest {
        public String userId;
        public String newEmail;
        public String confirmEmail;
    }


    @PutMapping("/updateEmail")
    public ResponseEntity<?> updateEmail(@RequestBody UpdateEmailRequest request) {
        // Znajdź użytkownika
        User user = userRepository.findByUserId(request.userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Jeśli e-mail już został ustawiony – odmów zmiany
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email można ustawić tylko raz");
        }

        // Sprawdź, czy podane adresy e-mail są takie same
        if (!request.newEmail.equals(request.confirmEmail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Podane adresy e-mail nie są zgodne");
        }

        // Ustaw nowy e-mail i datę zmiany
        user.setEmail(request.newEmail);
        user.setLastEmailChange(LocalDateTime.now());

        // Zapisz zmiany w bazie
        userRepository.save(user);

        return ResponseEntity.ok("Email został ustawiony");
    }


}
