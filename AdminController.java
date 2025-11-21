package com.factoryreceipt.factoryreceipt.controller;

import com.factoryreceipt.factoryreceipt.model.Shop;
import com.factoryreceipt.factoryreceipt.model.User;
import com.factoryreceipt.factoryreceipt.repository.ShopRepository;
import com.factoryreceipt.factoryreceipt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

    // =========================================
    // DTO do aktualizacji użytkownika
    // =========================================
    public static class UserUpdateRequest {
        public String email;
        public Integer usageLimit;
        public LocalDateTime expirationDate;
    }

    // =========================================
    // Endpoint: lista użytkowników
    // =========================================
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // =========================================
    // Endpoint: aktualizacja użytkownika
    // =========================================
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable String userId,
            @RequestBody UserUpdateRequest request
    ) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        // Zmiana e-maila (sprawdzenie 24h)
        if (request.email != null) {
            if (user.getLastEmailChange() != null) {
                LocalDateTime nextAllowed = user.getLastEmailChange().plusHours(24);
                if (LocalDateTime.now().isBefore(nextAllowed)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("E-mail można zmienić raz na 24 godziny. "
                                    + "Następna zmiana możliwa od: " + nextAllowed);
                }
            }
            user.setEmail(request.email);
            user.setLastEmailChange(LocalDateTime.now());
        }

        // Limit
        if (request.usageLimit != null) {
            user.setUsageLimit(request.usageLimit);
        }

        // Data wygaśnięcia
        if (request.expirationDate != null) {
            user.setExpirationDate(request.expirationDate);
        }

        userRepository.save(user);
        return ResponseEntity.ok("Dane użytkownika zostały zaktualizowane");
    }

    // =========================================
    // DTO: dodawanie nowego sklepu
    // =========================================
    public static class ShopRequest {
        // Upewnij się, że w admin.html wysyłasz
        // JSON z polami: storeName, senderEmail, senderName, ...
        public String storeName;       // nazwa sklepu (np. "MediaExpert")
        public String senderEmail;     // e-mail nadawcy (np. "no-reply@mediaexpert.pl")
        public String senderName;      // np. "MediaExpert"
        public String templateName;    // nazwa pliku szablonu (bez .html)
        public String defaultSubject;  // domyślny tytuł maila
        public String formConfig;      // JSON z konfiguracją pól
    }

    // =========================================
    // Endpoint: lista sklepów
    // =========================================
    @GetMapping("/shops")
    public ResponseEntity<List<Shop>> getAllShops() {
        return ResponseEntity.ok(shopRepository.findAll());
    }

    // =========================================
    // Endpoint: tworzenie sklepu + generowanie plików
    // =========================================
    @PostMapping("/shops")
    public ResponseEntity<?> createShop(@RequestBody ShopRequest request) {
        // Walidacja
        if (request.storeName == null || request.storeName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nazwa sklepu jest wymagana.");
        }
        // Sprawdzamy, czy sklep o takiej nazwie już istnieje
        if (shopRepository.findByShopName(request.storeName) != null) {
            return ResponseEntity.badRequest().body("Sklep o tej nazwie już istnieje.");
        }

        // Tworzymy encję Shop
        Shop shop = new Shop();
        shop.setShopName(request.storeName);
        shop.setEmailFrom(request.senderEmail);
        shop.setSenderName(request.senderName);
        shop.setTemplateName(request.templateName);
        shop.setDefaultSubject(request.defaultSubject);
        shop.setFormConfig(request.formConfig);

        shopRepository.save(shop);

        // Teraz tworzymy pliki:
        // 1) [shopName]Generator.java
        // 2) [templateName].html
        try {
            generateJavaFile(shop);
            generateTemplateFile(shop);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sklep dodany, ale wystąpił błąd przy tworzeniu plików: " + e.getMessage());
        }

        return ResponseEntity.ok("Dodano nowy sklep: " + request.storeName);
    }

    // =========================================
    // Metoda pomocnicza: tworzenie pliku .java
    // =========================================
    private void generateJavaFile(Shop shop) throws IOException {
        // Zakładamy, że mamy katalog:
        // src/main/java/com/factoryreceipt/factoryreceipt/customshops
        // Możesz go zmienić wg potrzeb
        String dirPath = "src/main/java/com/factoryreceipt/factoryreceipt/customshops";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Nazwa pliku: np. MediaExpertGenerator.java
        String fileName = shop.getShopName() + "Generator.java";
        File javaFile = new File(dir, fileName);

        // Prosty kod generatora:
        try (FileWriter fw = new FileWriter(javaFile)) {
            fw.write("package com.factoryreceipt.factoryreceipt.customshops;\n\n");
            fw.write("import java.util.HashMap;\n");
            fw.write("import java.util.Map;\n\n");
            fw.write("public class " + shop.getShopName() + "Generator {\n\n");
            fw.write("    public static Map<String, Object> generate(Map<String, String> request) {\n");
            fw.write("        // Przykładowa logika generowania potwierdzenia\n");
            fw.write("        Map<String, Object> variables = new HashMap<>();\n");
            fw.write("        // Numer zamówienia: ZAM- + timestamp\n");
            fw.write("        variables.put(\"orderNumber\", \"ZAM-\" + System.currentTimeMillis());\n");
            fw.write("        // Przekazujemy też wszystkie pola z request\n");
            fw.write("        variables.putAll(request);\n");
            fw.write("        return variables;\n");
            fw.write("    }\n");
            fw.write("}\n");
        }
    }

    // =========================================
    // Metoda pomocnicza: tworzenie pliku .html
    // =========================================
    private void generateTemplateFile(Shop shop) throws IOException {
        // Katalog na szablony: "src/main/resources/templates"
        String dirPath = "src/main/resources/templates";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Nazwa pliku: np. mediaexpertTemplate.html
        // (pobieramy z shop.getTemplateName())
        String fileName = shop.getTemplateName() + ".html";
        File htmlFile = new File(dir, fileName);

        try (FileWriter fw = new FileWriter(htmlFile)) {
            fw.write("<html>\n");
            fw.write("<head><meta charset=\"UTF-8\"></head>\n");
            fw.write("<body>\n");
            fw.write("  <h1>Potwierdzenie zamówienia " + shop.getShopName() + "</h1>\n");
            fw.write("  <p>Numer zamówienia: <span>{{ orderNumber }}</span></p>\n");
            fw.write("  <!-- Tu możesz wstawić kolejne pola, np. {{ city }}, {{ phoneNumber }} itp. -->\n");
            fw.write("</body>\n");
            fw.write("</html>\n");
        }
    }
}
