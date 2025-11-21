package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.text.DecimalFormat;

public class TrapstarGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Pobierz dane z request
        String productName   = request.getOrDefault("product_name", "");
        String size          = request.getOrDefault("size", "");
        String productColor  = request.getOrDefault("product_color", "");
        String priceInput    = request.getOrDefault("price", "0");
        String userName      = request.getOrDefault("user_name", "");
        String address       = request.getOrDefault("address", "");
        String city          = request.getOrDefault("city", "");
        String country       = request.getOrDefault("country", "");
        String photo         = request.getOrDefault("photo", "");
        String email         = request.getOrDefault("email", "");

        // Wyciągnij pierwsze imię z user_name (np. "damian nowak" -> "damian")
        String firstName = "";
        if (userName != null && !userName.trim().isEmpty()) {
            String[] parts = userName.trim().split("\\s+");
            if (parts.length > 0) {
                firstName = parts[0];
            }
        }

        // Zamień przecinki na kropki, aby umożliwić poprawne parsowanie liczby
        priceInput = priceInput.replace(",", ".");
        double priceValue = 0.0;
        try {
            priceValue = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            priceValue = 0.0;
        }

        // Używamy DecimalFormat do formatowania cen na 2 miejsca po przecinku
        DecimalFormat df = new DecimalFormat("0.00");
        // Format cen z kropką, a następnie zamieniamy kropkę na przecinek
        String formattedPrice = df.format(priceValue).replace(".", ",");

        // Oblicz podatek 19%
        double taxValue = priceValue * 0.19;
        String formattedTax = df.format(taxValue).replace(".", ",");

        // Oblicz finalną cenę (cena + podatek)
        double finalPriceValue = priceValue + taxValue;
        String formattedFinalPrice = df.format(finalPriceValue).replace(".", ",");

        // Generuj numer zamówienia: "TS" + 7 losowych cyfr (np. TS1316540)
        Random random = new Random();
        int randomDigits = 1000000 + random.nextInt(9000000); // losowa liczba z przedziału [1000000, 9999999]
        String orderNumber = "TS" + randomDigits;

        // Generuj 4 ostatnie cyfry karty Mastercard
        int cardSuffix = 1000 + random.nextInt(9000); // losowa liczba z przedziału [1000, 9999]
        String cardSuffixStr = String.valueOf(cardSuffix);

        // Wypełnij mapę zmiennych – klucze odpowiadają placeholderom w szablonie e-mail
        variables.put("order_number", orderNumber);
        variables.put("price", formattedPrice);
        variables.put("tax", formattedTax);
        variables.put("final_price", formattedFinalPrice);
        variables.put("email", email);
        variables.put("product_name", productName);
        variables.put("size", size);
        variables.put("product_color", productColor);
        variables.put("user_name", userName);
        variables.put("first_name", firstName);
        variables.put("address", address);
        variables.put("city", city);
        variables.put("country", country);
        variables.put("photo", photo);
        // Dodajemy 4 ostatnie cyfry karty
        variables.put("card_suffix", cardSuffixStr);

        return variables;
    }
}
