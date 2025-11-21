package com.factoryreceipt.factoryreceipt.customshops;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RalphlaurenGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Pobieramy dane z request
        String deliveryTime = request.getOrDefault("delivery_time", "");
        String photo = request.getOrDefault("photo", "");
        String productName = request.getOrDefault("product_name", "");
        String productColor = request.getOrDefault("product_color", "");
        String size = request.getOrDefault("size", "");
        String styleid = request.getOrDefault("styleid", "");
        String priceStr = request.getOrDefault("price", "0");
        String userName = request.getOrDefault("user_name", "");
        String address = request.getOrDefault("address", "");
        String city = request.getOrDefault("city", "");
        String country = request.getOrDefault("country", "");

        // Przetwarzamy cenę: usuwamy spacje i zamieniamy przecinek na kropkę
        double priceValue = parsePrice(priceStr);
        // Formatowanie ceny do formatu "0.00" z przecinkiem jako separatorem dziesiętnym
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedPrice = df.format(priceValue).replace(".", ",");

        // Generujemy numer zamówienia jako ciąg numeryczny o długości 8 znaków
        String orderNumber = generateOrderNumber(8);
        // Generujemy 4 ostatnie cyfry karty – losowy ciąg 4-cyfrowy
        String cardSuffix = generateCardSuffix();

        // Wkładamy zmienne do mapy
        variables.put("delivery_time", deliveryTime);
        variables.put("photo", photo);
        variables.put("product_name", productName);
        variables.put("product_color", productColor);
        variables.put("size", size);
        variables.put("styleid", styleid);
        variables.put("price", formattedPrice);
        variables.put("user_name", userName);
        variables.put("address", address);
        variables.put("city", city);
        variables.put("country", country);
        variables.put("order_number", orderNumber);
        variables.put("card_suffix", cardSuffix);

        return variables;
    }

    private static double parsePrice(String priceStr) {
        // Usuń spacje i zamień przecinek na kropkę
        priceStr = priceStr.replace(" ", "").replace(",", ".");
        try {
            return Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Generuje losowy ciąg numeryczny o podanej długości (pierwsza cyfra nie może być zerem)
    private static String generateOrderNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        // Pierwszy znak od 1 do 9
        sb.append(random.nextInt(9) + 1);
        for (int i = 1; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // Generuje losowy ciąg numeryczny o długości 4 (dla ostatnich cyfr karty)
    private static String generateCardSuffix() {
        Random random = new Random();
        int num = random.nextInt(9000) + 1000; // liczba od 1000 do 9999
        return String.valueOf(num);
    }
}
