package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MediaExpertGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();
        // Generowanie numeru zamówienia – przykładowo 11-cyfrowy numer
        long randomNum = 10000000000L + (long)(new Random().nextDouble() * 90000000000L);
        variables.put("orderNumber", String.valueOf(Math.abs(randomNum)));

        // Przekazanie pozostałych pól z request
        variables.put("email", request.get("email"));
        variables.put("productName", request.get("productName"));

        // Formatowanie ceny:
        // np. "1000" -> "1000.00", "199,99" -> "199.00"
        String priceInput = request.get("price");
        double priceValue = 0;
        if (priceInput != null && !priceInput.isEmpty()) {
            try {
                // Zamieniamy przecinek na kropkę, aby móc sparsować jako double
                priceValue = Double.parseDouble(priceInput.replace(',', '.'));
            } catch (NumberFormatException e) {
                priceValue = 0;
            }
        }
        int intPrice = (int) priceValue; // odrzucamy część dziesiętną
        String formattedPrice = intPrice + ".00";
        variables.put("price", formattedPrice);

        variables.put("productCode", request.get("productCode"));
        variables.put("photo", (request.get("photo") != null && !request.get("photo").isEmpty()) ? request.get("photo") : "Brak");
        variables.put("deliveryTime", request.get("deliveryTime"));
        variables.put("address", request.get("address"));
        variables.put("city", request.get("city"));
        variables.put("phoneNumber", request.get("phoneNumber"));
        return variables;
    }
}
