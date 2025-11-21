package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;

public class VitkacGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Generujemy unikalny numer zamówienia (przykładowo 10-cyfrowy)
        String orderNumber = generateOrderNumber();
        // Generujemy suffix karty (4 cyfry)
        String visaNumber = generateVisaCardSuffix();

        // Bezpiecznie pobieramy cenę
        String priceStr = request.getOrDefault("price", "0.0");
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            price = 0.0;
        }
        double finalPrice = price + 28.06;
        double tax = price * 0.20;

        // Pobieramy dane użytkownika i dzielimy imię
        String userName = request.getOrDefault("userName", "");
        String firstName = !userName.isEmpty() ? userName.split(" ")[0] : "Klient";

        // Mapujemy dane – klucze muszą odpowiadać placeholderom w szablonie e-mail
        variables.put("orderNumber", orderNumber);
        variables.put("visa_number", visaNumber);
        variables.put("price", request.getOrDefault("price", ""));
        variables.put("finalPrice", String.valueOf(finalPrice));
        variables.put("tax", String.valueOf(tax));
        variables.put("product_name", request.getOrDefault("productName", ""));
        variables.put("size", request.getOrDefault("size", ""));
        variables.put("email", request.getOrDefault("email", ""));
        // Ujednolicony format kluczy zgodny z szablonem
        variables.put("email_dane", request.getOrDefault("email_dane", ""));
        variables.put("user_name", request.getOrDefault("userName", ""));
        variables.put("first_name", firstName);
        variables.put("address", request.getOrDefault("address", ""));
        variables.put("city", request.getOrDefault("city", ""));
        variables.put("country", request.getOrDefault("country", ""));
        variables.put("phone_number", request.getOrDefault("phone_number", ""));

        return variables;
    }

    private static String generateOrderNumber() {
        long randomNum = 1000000000L + (long)(Math.random() * 9000000000L);
        return String.valueOf(randomNum);
    }

    private static String generateVisaCardSuffix() {
        int randomNum = 1000 + (int)(Math.random() * 9000);
        return String.valueOf(randomNum);
    }
}
