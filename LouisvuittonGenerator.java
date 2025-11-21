package com.factoryreceipt.factoryreceipt.customshops;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LouisvuittonGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Pobieramy dane z request
        String productName   = request.getOrDefault("product_name", "");
        String styleid       = request.getOrDefault("styleid", "");
        String material      = request.getOrDefault("material", "");
        String productColor  = request.getOrDefault("product_color", "");
        String priceStr      = request.getOrDefault("price", "0");
        String userName      = request.getOrDefault("user_name", "");
        String address       = request.getOrDefault("address", "");
        String city          = request.getOrDefault("city", "");
        String country       = request.getOrDefault("country", "");
        String photo         = request.getOrDefault("photo", "");

        // Przetwarzamy cenę:
        // Usuwamy znak euro i ewentualne przecinki (jako separator tysięcy)
        priceStr = priceStr.replace("€", "").replace(",", "").trim();
        double priceValue = 0.0;
        try {
            priceValue = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            priceValue = 0.0;
        }

        // Formatujemy cenę w formacie "€#,##0.00"
        DecimalFormat df = new DecimalFormat("€#,##0.00");
        String formattedPrice = df.format(priceValue);

        // Generujemy numer zamówienia: format "nv" + 10-cyfrowa liczba (pierwsza cyfra nie jest zerem)
        String orderNumber = generateOrderNumber();

        // Generujemy numer śledzenia (tracking number) – losowy ciąg alfanumeryczny o długości 18
        String trackingNumber = generateTrackingNumber(18);

        // Wkładamy wszystkie zmienne do mapy
        variables.put("product_name", productName);
        variables.put("styleid", styleid);
        variables.put("material", material);
        variables.put("product_color", productColor);
        variables.put("price", formattedPrice);
        variables.put("user_name", userName);
        variables.put("address", address);
        variables.put("city", city);
        variables.put("country", country);
        variables.put("photo", photo);
        variables.put("order_number", orderNumber);
        variables.put("tracking_n", trackingNumber);

        return variables;
    }

    // Generuje numer zamówienia w formacie "nv" + 10-cyfrowa liczba
    private static String generateOrderNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("nv");
        sb.append(random.nextInt(9) + 1); // pierwszy znak: 1-9
        for (int i = 1; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // Generuje losowy ciąg alfanumeryczny o zadanej długości – dla tracking number
    private static String generateTrackingNumber(int length) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
