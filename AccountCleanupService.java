package com.factoryreceipt.factoryreceipt.service;

import com.factoryreceipt.factoryreceipt.model.User;
import com.factoryreceipt.factoryreceipt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountCleanupService {

    @Autowired
    private UserRepository userRepository;

    // Zadanie uruchamiane co minutę (60000 ms)
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanupExpiredAccounts() {
        System.out.println("Zadanie czyszczące uruchomione: " + LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now();

        // Usuń konta czasowe, które wygasły
        List<User> expiredTimeAccounts = userRepository.findAll().stream()
                .filter(u -> "time".equalsIgnoreCase(u.getAccountType())
                        && u.getExpirationDate() != null
                        && u.getExpirationDate().isBefore(now))
                .collect(Collectors.toList());
        System.out.println("Znaleziono " + expiredTimeAccounts.size() + " kont czasowych do usunięcia.");
        if (!expiredTimeAccounts.isEmpty()) {
            userRepository.deleteAll(expiredTimeAccounts);
            System.out.println("Usunięto " + expiredTimeAccounts.size() + " kont czasowych wygasłych.");
        }

        // Usuń konta limitowane, które osiągnęły 0 użyć
        List<User> exhaustedLimitAccounts = userRepository.findAll().stream()
                .filter(u -> "limit".equalsIgnoreCase(u.getAccountType())
                        && u.getUsageLimit() != null
                        && u.getUsageLimit() <= 0)
                .collect(Collectors.toList());
        System.out.println("Znaleziono " + exhaustedLimitAccounts.size() + " kont limitowanych do usunięcia.");
        if (!exhaustedLimitAccounts.isEmpty()) {
            userRepository.deleteAll(exhaustedLimitAccounts);
            System.out.println("Usunięto " + exhaustedLimitAccounts.size() + " kont limitowanych wyczerpanych.");
        }
    }
}
