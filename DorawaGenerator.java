package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;

public class DorawaGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Generujemy unikalny numer zamówienia (np. 11-cyfrowy)
        String orderNumber = generateDorawaOrderNumber();

        // Pobieramy wartości z request
        String email = request.getOrDefault("email", "");
        String productName = request.getOrDefault("productName", "");
        String priceStr = request.getOrDefault("price", "0");
        double price = 0.0;
        // Usuwamy ewentualne spacje i zamieniamy przecinek na kropkę
        try {
            price = Double.parseDouble(priceStr.replace(" ", "").replace(",", "."));
        } catch (NumberFormatException e) {
            price = 0.0;
        }
        String size = request.getOrDefault("size", "");

        // Druga część formularza
        String userName = request.getOrDefault("userName", "");
        String address = request.getOrDefault("address", "");
        String city = request.getOrDefault("city", "");
        String productCode = request.getOrDefault("productCode", "");

        // Wkładamy zmienne – klucze muszą odpowiadać placeholderom w szablonie e-mail Dorawa
        variables.put("order_number", orderNumber);
        variables.put("email", email);
        variables.put("product_name", productName);
        variables.put("price", String.valueOf(price));
        variables.put("size", size);
        variables.put("user_name", userName);
        variables.put("address", address);
        variables.put("city", city);
        variables.put("product_code", productCode);

        return variables;
    }

    private static String generateDorawaOrderNumber() {
        // Przykładowa implementacja – generujemy 11-cyfrowy numer zamówienia
        long randomNum = 10000000000L + (long)(Math.random() * 90000000000L);
        return String.valueOf(Math.abs(randomNum));
    }
}
