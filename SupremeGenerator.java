package com.factoryreceipt.factoryreceipt.customshops;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SupremeGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Pobieramy dane z request
        String userName     = request.getOrDefault("user_name", "");
        String deliveryTime = request.getOrDefault("delivery_time", "");
        String address      = request.getOrDefault("address", "");
        String addressN     = request.getOrDefault("address_n", "");
        String city         = request.getOrDefault("city", "");
        String productName  = request.getOrDefault("product_name", "");
        String productColor = request.getOrDefault("product_color", "");
        String size         = request.getOrDefault("size", "");
        String priceStr     = request.getOrDefault("price", "0");

        // Przygotowanie ceny: usunięcie spacji i zamiana przecinka na kropkę
        priceStr = priceStr.replace(" ", "").replace(",", ".");
        double priceValue = 0.0;
        try {
            priceValue = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            priceValue = 0.0;
        }

        // Obliczenie VAT: 10% ceny
        double vat = priceValue * 0.10;
        // Finalna cena: cena + 15 + VAT
        double finalPrice = priceValue + 15 + vat;

        // Formatowanie cen – jako liczby całkowite, bez miejsc po przecinku
        DecimalFormat df = new DecimalFormat("0");
        String formattedPrice = df.format(priceValue);
        String formattedVat = df.format(vat);
        String formattedFinalPrice = df.format(finalPrice);

        // Generowanie tracking number – losowy ciąg alfanumeryczny o długości 18 znaków
        String trackingN = generateTrackingNumber(18);
        // Generowanie order number – losowy ciąg numeryczny o długości 13 znaków
        String orderNumber = generateOrderNumber(13);

        // Wkładamy zmienne do mapy – klucze odpowiadają placeholderom w szablonie e-mail
        variables.put("user_name", userName);
        variables.put("delivery_time", deliveryTime);
        variables.put("address", address);
        variables.put("address_n", addressN);
        variables.put("city", city);
        variables.put("product_name", productName);
        variables.put("product_color", productColor);
        variables.put("size", size);
        variables.put("price", formattedPrice);
        variables.put("vat", formattedVat);
        variables.put("final_price", formattedFinalPrice);
        variables.put("tracking_n", trackingN);
        variables.put("order_number", orderNumber);

        return variables;
    }

    // Generuje losowy ciąg alfanumeryczny o podanej długości – dla tracking number
    private static String generateTrackingNumber(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    // Generuje losowy ciąg numeryczny o podanej długości – dla order number
    private static String generateOrderNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }
}
