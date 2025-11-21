package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NikeGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Generate a unique order number (simulate using a 10-digit number)
        String orderNumber = generateSnkrsOrderNumber();

        // Parse the price safely – zamieniamy przecinek na kropkę przed parsowaniem
        String priceStr = request.getOrDefault("price", "0");
        double price = 0.0;
        try {
            price = Double.parseDouble(priceStr.replace(',', '.'));
        } catch (NumberFormatException e) {
            price = 0.0;
        }

        // Calculate tax and final price (adjust tax rate as needed)
        double tax = price * 0.21; // Assuming 21% tax
        double finalPrice = price; // No extra fee in this example

        // Format the price in Polish style: exactly two decimal places and comma as decimal separator
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pl", "PL"));
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        String formattedPrice = df.format(price);

        // Retrieve values from the request (using fallback keys if necessary)
        String email = request.getOrDefault("email", "");
        String productName = request.getOrDefault("productName", request.getOrDefault("product_name", ""));
        String size = request.getOrDefault("size", "");
        String userName = request.getOrDefault("userName", "");
        String address = request.getOrDefault("address", "");
        String city = request.getOrDefault("city", "");
        String country = request.getOrDefault("country", "");
        String photo = request.getOrDefault("photo", "");
        // Zmiana: używamy "productCat" zamiast "product_cat"
        String productCat = request.getOrDefault("productCat", "");
        String deliveryTime = request.getOrDefault("delivery_time", "");
        String deliveryArrived = request.getOrDefault("delivery_arrived", "");

        // Extract first name from userName for personalization (if needed)
        String firstName = (userName != null && !userName.isEmpty()) ? userName.split(" ")[0] : "Klient";

        // Map the variables – keys must correspond to those in your email template
        variables.put("order_number", orderNumber);
        variables.put("price", formattedPrice);
        variables.put("tax", String.valueOf(tax));
        variables.put("final_price", String.valueOf(finalPrice));
        variables.put("email", email);
        variables.put("product_name", productName);
        variables.put("user_name", userName);
        variables.put("address", address);
        variables.put("city", city);
        variables.put("country", country);
        variables.put("photo", photo);
        variables.put("size", size);
        variables.put("productCat", productCat);
        variables.put("delivery_time", deliveryTime);
        variables.put("delivery_arrived", deliveryArrived);
        variables.put("first_name", firstName);

        return variables;
    }

    private static String generateSnkrsOrderNumber() {
        // Generate a random 10-digit number
        long randomNum = 1000000000L + (long) (Math.random() * 9000000000L);
        return String.valueOf(randomNum);
    }
}
