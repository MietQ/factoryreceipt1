package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class SnkrsGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Generate a 10-digit order number as an example
        String orderNumber = generateSnkrsOrderNumber();

        // Parse price: zamieniamy przecinek na kropkę przed parsowaniem
        String priceStr = request.getOrDefault("price", "0").replace(",", ".");
        double price = 0.0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            price = 0.0;
        }

        // Format price tak, aby zawsze miało dwa miejsca po kropce, np. "199.00" lub "199.99"
        // Ustawienia: używamy Locale.US, czyli separator dziesiętny to kropka, a grupa tysięcy oddzielana przecinkiem (opcjonalnie)
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        String formattedPrice = df.format(price);

        // Retrieve values from the request (using fallback keys if necessary)
        String email           = request.getOrDefault("email", "");
        String productName     = request.getOrDefault("product_name", "");
        String size            = request.getOrDefault("size", "");
        String userName        = request.getOrDefault("user_name", "");
        String address         = request.getOrDefault("address", "");
        String city            = request.getOrDefault("city", "");
        String country         = request.getOrDefault("country", "");
        String photo           = request.getOrDefault("photo", "");
        String deliveryTime    = request.getOrDefault("delivery_time", "");
        String deliveryArrived = request.getOrDefault("delivery_arrived", "");
        String productCat      = request.getOrDefault("product_cat", "");

        // Compute first name (for personalization)
        String firstName = "";
        if (!userName.isEmpty()) {
            firstName = userName.split(" ")[0];
        }

        // Optionally compute tax and final price
        double tax = price * 0.21;
        double finalPrice = price - tax;

        // Map the variables – keys must correspond to those in your email template
        variables.put("order_number", orderNumber);
        variables.put("price", formattedPrice);
        variables.put("tax", String.valueOf(tax));
        variables.put("final_price", String.valueOf(finalPrice));
        variables.put("email", email);
        variables.put("product_name", productName);
        variables.put("size", size);
        variables.put("user_name", userName);
        variables.put("address", address);
        variables.put("city", city);
        variables.put("country", country);
        variables.put("photo", photo);
        variables.put("delivery_time", deliveryTime);
        variables.put("delivery_arrived", deliveryArrived);
        variables.put("product_cat", productCat);
        variables.put("first_name", firstName);

        return variables;
    }

    private static String generateSnkrsOrderNumber() {
        // Generate a random 10-digit number
        long randomNum = 1000000000L + (long)(Math.random() * 9000000000L);
        return String.valueOf(randomNum);
    }
}
