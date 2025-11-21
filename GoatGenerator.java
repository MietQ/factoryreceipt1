package com.factoryreceipt.factoryreceipt.customshops;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GoatGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Pobieramy dane z request
        String photo        = request.getOrDefault("photo", "");
        String sku          = request.getOrDefault("sku", "");
        String productName  = request.getOrDefault("product_name", "");
        String size         = request.getOrDefault("size", "");
        String brandName    = request.getOrDefault("brand_name", "");
        String productColor = request.getOrDefault("product_color", "");
        String priceStr     = request.getOrDefault("price", "0");
        // final_price nie jest pobierany z formularza – obliczamy automatycznie
        String userName     = request.getOrDefault("user_name", "");
        String address      = request.getOrDefault("address", "");
        String city         = request.getOrDefault("city", "");
        String wojew        = request.getOrDefault("wojew", "");
        String country      = request.getOrDefault("country", "");

        // Przetwarzamy cenę – usuwamy spacje oraz zamieniamy przecinek na kropkę
        double priceValue = parseDouble(priceStr);
        // Obliczamy final_price = price + 10
        double finalPriceValue = priceValue + 10;

        // Formatowanie cen: bez miejsc po przecinku (np. "150" zamiast "150.00")
        DecimalFormat df = new DecimalFormat("0");
        String formattedPrice = df.format(priceValue);
        String formattedFinalPrice = df.format(finalPriceValue);

        // Generujemy order_number jako losowy ciąg numeryczny o długości 9 znaków
        String orderNumber = generateOrderNumber(9);

        // Wkładamy wszystkie zmienne do mapy – klucze odpowiadają placeholderom w szablonie e-mail
        variables.put("photo", photo);
        variables.put("sku", sku);
        variables.put("product_name", productName);
        variables.put("size", size);
        variables.put("brand_name", brandName);
        variables.put("product_color", productColor);
        variables.put("price", formattedPrice);
        variables.put("final_price", formattedFinalPrice);
        variables.put("user_name", userName);
        variables.put("address", address);
        variables.put("city", city);
        variables.put("wojew", wojew);
        variables.put("country", country);
        variables.put("order_number", orderNumber);

        return variables;
    }

    private static double parseDouble(String str) {
        // Usuń spacje i zamień przecinek na kropkę
        str = str.replace(" ", "").replace(",", ".");
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static String generateOrderNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        // Upewnij się, że pierwszy znak nie jest zerem
        sb.append(random.nextInt(9) + 1);
        for (int i = 1; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
