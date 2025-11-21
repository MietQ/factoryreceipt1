package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class GrailPointGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Generate a 6-digit order number
        String orderNumber = generateSixDigitNumber();

        // Parse price (replace comma with dot if needed)
        String priceStr = request.getOrDefault("price", "0").replace(",", ".");
        double price = 0.0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            price = 0.0;
        }

        // Format price to always have two decimal places with a dot as decimal separator
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        String formattedPrice = df.format(price);

        // Retrieve values from the request
        String email        = request.getOrDefault("email", "");
        String productName  = request.getOrDefault("product_name", "");
        String userName     = request.getOrDefault("user_name", "");
        String size         = request.getOrDefault("size", "");
        String street       = request.getOrDefault("street", "");
        String city         = request.getOrDefault("city", "");
        String phoneNumber  = request.getOrDefault("phone_number", "");
        String deliveryTime = request.getOrDefault("delivery_time", "");
        String photo        = request.getOrDefault("photo", "");

        // Populate the variables map with keys matching your email template placeholders
        variables.put("order_number", orderNumber);
        variables.put("price", formattedPrice);
        variables.put("email", email);
        variables.put("product_name", productName);
        variables.put("user_name", userName);
        variables.put("size", size);
        variables.put("street", street);
        variables.put("city", city);
        variables.put("phone_number", phoneNumber);
        variables.put("delivery_time", deliveryTime);
        variables.put("photo", photo);

        return variables;
    }

    // Helper method to generate a 6-digit order number
    private static String generateSixDigitNumber() {
        int num = 100000 + (int)(Math.random() * 900000);
        return String.valueOf(num);
    }
}
