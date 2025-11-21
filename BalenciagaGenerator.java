package com.factoryreceipt.factoryreceipt.customshops;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BalenciagaGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Pobieranie danych z request
        String productName = request.getOrDefault("product_name", "");
        String photo = request.getOrDefault("photo", "");
        String productColor = request.getOrDefault("product_color", "");
        String size = request.getOrDefault("size", "");
        String priceStr = request.getOrDefault("price", "0");
        String userName = request.getOrDefault("user_name", "");
        String address = request.getOrDefault("address", "");
        String city = request.getOrDefault("city", "");
        String country = request.getOrDefault("country", "");
        String zipCode = request.getOrDefault("zip_code", "");

        // Usuwamy spacje i zamieniamy przecinek na kropkę, aby uzyskać format numeryczny
        priceStr = priceStr.replace(" ", "").replace(",", ".");
        double priceValue = 0.0;
        try {
            priceValue = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            priceValue = 0.0;
        }

        // Obliczamy final_price = price + 10
        double finalPriceValue = priceValue + 10.0;

        // Formatowanie cen do 2 miejsc po przecinku
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedPrice = df.format(priceValue);
        String formattedFinalPrice = df.format(finalPriceValue);

        // Generowanie numeru zamówienia – 8-cyfrowy ciąg (pierwsza cyfra nie może być zerem)
        String orderNumber = generateOrderNumber(8);

        // Wkładamy zmienne do mapy (klucze odpowiadają placeholderom w szablonie e-mail)
        variables.put("product_name", productName);
        variables.put("photo", photo);
        variables.put("product_color", productColor);
        variables.put("size", size);
        variables.put("price", formattedPrice);
        variables.put("final_price", formattedFinalPrice);
        variables.put("user_name", userName);
        variables.put("address", address);
        variables.put("city", city);
        variables.put("country", country);
        variables.put("zip_code", zipCode);
        variables.put("order_number", orderNumber);

        return variables;
    }

    private static String generateOrderNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        // Upewnij się, że pierwszy znak nie jest zerem (od 1 do 9)
        sb.append(random.nextInt(9) + 1);
        for (int i = 1; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
