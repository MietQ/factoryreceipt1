package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MonclerGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Generate a unique order number (10-digit number, for example)
        String orderNumber = generateMonclerOrderNumber();

        // Convert price: usuwamy separator tysięcy (kropki) i zamieniamy przecinek na kropkę
        String priceInput = request.getOrDefault("price", "0");
        priceInput = priceInput.replace(".", "").replace(',', '.');
        double price = 0.0;
        try {
            price = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            price = 0.0;
        }

        // Calculate tax (example: 21% VAT) and final price (for example, price + 10)
        double tax = price * 0.21;
        double finalPrice = price + 10.0;

        // Prepare a DecimalFormat to format numbers in the desired format:
        // - Separator tysięcy: kropka
        // - Separator dziesiętny: przecinek
        // - Zawsze dwa miejsca po przecinku
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pl", "PL"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

        String formattedPrice = df.format(price);
        String formattedTax = df.format(tax);
        String formattedFinalPrice = df.format(finalPrice);

        // Retrieve additional fields from the request
        String email = request.getOrDefault("email", "");
        String size = request.getOrDefault("size", "");
        String photo = request.getOrDefault("photo", "");
        String userName = request.getOrDefault("user_name", "");
        String address = request.getOrDefault("address", "");
        String city = request.getOrDefault("city", "");
        String wojew = request.getOrDefault("wojew", "");
        String productName = request.getOrDefault("product_name", "");
        String productColor = request.getOrDefault("product_color", "");
        String condition = request.getOrDefault("condition", "");
        String country = request.getOrDefault("country", "");

        // Populate the variables map – keys must correspond to placeholders in your email template
        variables.put("order_number", orderNumber);
        variables.put("price", formattedPrice);
        variables.put("tax", formattedTax);
        variables.put("final_price", formattedFinalPrice);
        variables.put("email", email);
        variables.put("user_name", userName);
        variables.put("address", address);
        variables.put("city", city);
        variables.put("wojew", wojew);
        variables.put("size", size);
        variables.put("photo", photo);
        variables.put("product_name", productName);
        variables.put("product_color", productColor);
        variables.put("condition", condition);
        variables.put("country", country);

        return variables;
    }

    private static String generateMonclerOrderNumber() {
        // Example: generate a 10-digit order number.
        long randomNum = 1000000000L + (long)(Math.random() * 9000000000L);
        return String.valueOf(randomNum);
    }
}
