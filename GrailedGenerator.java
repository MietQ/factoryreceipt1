package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;

public class GrailedGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Generujemy unikalny numer zamówienia (przykładowo 14-cyfrowy)
        String orderNumber = generateOrderNumber();

        // Pobieramy i parsujemy cenę
        String priceStr = request.get("price");
        double price = 0.0;
        if (priceStr != null && !priceStr.isEmpty()) {
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                price = 0.0;
            }
        }

        // Obliczamy podatek i cenę końcową (przyjmujemy stawkę podatku 21%)
        double tax = Math.round(price * 0.21 * 100.0) / 100.0;
        double finalPrice = price - tax;

        // Pobieramy dane użytkownika (pełne imię i nazwisko)
        String userName = request.get("userName") != null ? request.get("userName") : "";

        // Mapujemy dane – klucze odpowiadają placeholderom w szablonie Grailed
        variables.put("order_number", orderNumber);
        variables.put("price", priceStr != null ? priceStr : "");
        variables.put("size", request.getOrDefault("size", ""));
        variables.put("photo", request.getOrDefault("photo", ""));
        variables.put("email", request.getOrDefault("email", ""));
        variables.put("userName", request.getOrDefault("userName", ""));
        variables.put("address", request.getOrDefault("address", ""));
        variables.put("city", request.getOrDefault("city", ""));
        variables.put("wojew", request.getOrDefault("wojew", ""));
        variables.put("zip_code", request.getOrDefault("zip_code", ""));
        variables.put("country", request.getOrDefault("country", ""));
        variables.put("final_price", String.valueOf(finalPrice));
        variables.put("tax", String.valueOf(tax));
        variables.put("product_name", request.getOrDefault("product_name", ""));
        variables.put("product_cat", request.getOrDefault("productCat", ""));
        variables.put("product_color", request.getOrDefault("productColor", ""));
        variables.put("condition", request.getOrDefault("condition", ""));
        variables.put("delivery_time", request.getOrDefault("deliveryTime", ""));
        variables.put("delivery_arrived", request.getOrDefault("deliveryArrived", ""));
        variables.put("country_s", request.getOrDefault("country_s", ""));

        return variables;
    }

    private static String generateOrderNumber() {
        long randomNum = 10000000000000L + (long)(Math.random() * 90000000000000L);
        return String.valueOf(randomNum);
    }
}
