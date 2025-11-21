package com.factoryreceipt.factoryreceipt.controller;

import com.factoryreceipt.factoryreceipt.customshops.*;
import com.factoryreceipt.factoryreceipt.dto.StockxRequest;
import com.factoryreceipt.factoryreceipt.model.Receipt;
import com.factoryreceipt.factoryreceipt.model.User;
import com.factoryreceipt.factoryreceipt.repository.ReceiptRepository;
import com.factoryreceipt.factoryreceipt.repository.UserRepository;
import com.factoryreceipt.factoryreceipt.service.EmailService;
import com.factoryreceipt.factoryreceipt.stockx.StockxGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @PostMapping("/{shopName}")
    public ResponseEntity<String> generateConfirmation(@PathVariable String shopName,
                                                       @RequestBody Map<String, String> request) {
        // 1) Walidacja userId
        String userId = request.get("userId");
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Brak identyfikatora użytkownika.");
        }
        // 2) Pobieramy użytkownika
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Użytkownik nie istnieje.");
        }
        // 3) Sprawdzamy limit dla kont typu "limit"
        if ("limit".equalsIgnoreCase(user.getAccountType())) {
            Integer currentLimit = user.getUsageLimit();
            if (currentLimit == null || currentLimit <= 0) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Limit potwierdzeń został wyczerpany.");
            }
        }
        // 4) Generujemy zmienne
        Map<String, Object> variables;
        try {
            // 4) Generujemy zmienne w zależności od sklepu
            if ("StockX".equalsIgnoreCase(shopName)) {
                StockxRequest stockxRequest = new StockxRequest();
                stockxRequest.setUserId(request.get("userId"));
                stockxRequest.setProductName(request.get("productName"));
                stockxRequest.setPrice(request.get("price"));
                stockxRequest.setPhoto(request.get("photo"));
                stockxRequest.setDeliveryTime(request.get("deliveryTime"));
                stockxRequest.setStyleid(request.get("styleid"));
                stockxRequest.setSize(request.get("size"));
                variables = StockxGenerator.generate(stockxRequest);

            } else if ("MediaExpert".equalsIgnoreCase(shopName)) {
                variables = MediaExpertGenerator.generate(request);

            } else if ("Zalando".equalsIgnoreCase(shopName)) {
                variables = ZalandoGenerator.generate(request);

            } else if ("Dropsy".equalsIgnoreCase(shopName)) {
                variables = DropsyGenerator.generate(request);

            } else if ("Vitkac".equalsIgnoreCase(shopName)) {
                variables = VitkacGenerator.generate(request);

            } else if ("Grailed".equalsIgnoreCase(shopName)) {
                variables = GrailedGenerator.generate(request);
            } else if ("Farfetch".equalsIgnoreCase(shopName)) {
                variables = FarfetchGenerator.generate(request);
            } else if ("Dorawa".equalsIgnoreCase(shopName)) {  // Nowa obsługa Dorawa
                variables = DorawaGenerator.generate(request);
            } else if ("Corteiz".equalsIgnoreCase(shopName)) {  // Nowa obsługa Dorawa
                variables = CorteizGenerator.generate(request);
            } else if ("Nike".equalsIgnoreCase(shopName)) {  // Nowa obsługa Dorawa
                variables = NikeGenerator.generate(request);
            } else if ("Confirmed".equalsIgnoreCase(shopName)) {  // Nowa obsługa Dorawa
                variables = ConfirmedGenerator.generate(request);
            } else if ("Snkrs".equalsIgnoreCase(shopName)) {  // Nowa obsługa Dorawa
                variables = SnkrsGenerator.generate(request);
            } else if ("grail point".equalsIgnoreCase(shopName)) {  // Nowa obsługa Dorawa
                variables = GrailPointGenerator.generate(request);
            } else if ("Moncler".equalsIgnoreCase(shopName)) {  // Nowa obsługa Dorawa
                variables = MonclerGenerator.generate(request);
            } else if ("Trapstar".equalsIgnoreCase(shopName)) {
                variables = TrapstarGenerator.generate(request);
            } else if ("Notino".equalsIgnoreCase(shopName)) {
                variables = NotinoGenerator.generate(request);
            } else if ("Supreme".equalsIgnoreCase(shopName)) {
                variables = SupremeGenerator.generate(request);
            } else if ("Goat".equalsIgnoreCase(shopName)) {
                variables = GoatGenerator.generate(request);
            } else if ("Ralph Lauren".equalsIgnoreCase(shopName)) {
                variables = RalphlaurenGenerator.generate(request);
            } else if ("Balenciaga".equalsIgnoreCase(shopName)) {
                variables = BalenciagaGenerator.generate(request);
            } else if ("Louis Vuitton".equalsIgnoreCase(shopName)) {
                variables = LouisvuittonGenerator.generate(request);
            }

        else {
                // Jeżeli żaden z powyższych warunków nie pasuje,
                // to zwracamy błąd, że sklep nie jest obsługiwany
                return ResponseEntity.badRequest().body("Sklep nieobsługiwany: " + shopName);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Błąd podczas generowania danych: " + e.getMessage());
        }

        // 5) Wysyłamy e-mail
        String actualEmail = user.getEmail();
        if (actualEmail != null && !actualEmail.isEmpty()) {
            emailService.sendStockxEmail(actualEmail, null, variables, shopName);
        }
        // 6) Aktualizacja limitu
        if ("limit".equalsIgnoreCase(user.getAccountType())) {
            Integer currentLimit = user.getUsageLimit();
            if (currentLimit != null && currentLimit > 0) {
                user.setUsageLimit(currentLimit - 1);
                userRepository.save(user);
            }
        }
        // 7) Zapis historii potwierdzenia
        Receipt receipt = new Receipt(
                userId,
                request.get("productName"),
                (String) variables.get("price"),
                (String) variables.get("fee"),
                (String) variables.get("shipping"),
                (String) variables.get("finalPrice"),
                (String) variables.get("orderNumber"),
                request.get("styleid"),
                request.get("size"),
                request.get("deliveryTime"),
                request.get("photo") != null ? request.get("photo") : "Brak",
                LocalDateTime.now()
        );
        receiptRepository.save(receipt);
        return ResponseEntity.ok("Potwierdzenie wysłane na Twój adres e-mail.");
    }
}
