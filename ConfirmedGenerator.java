package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ConfirmedGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Generujemy numer zamówienia (np. 10-cyfrowy numer)
        String orderNumber = generateConfirmedOrderNumber();

        // Parsujemy cenę – zamieniamy przecinek na kropkę, aby umożliwić parsowanie
        String priceStr = request.getOrDefault("price", "0").replace(',', '.');
        double price = 0.0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            price = 0.0;
        }

        // Obliczamy podatek (21% ceny) i finalną cenę (cena minus podatek)
        double tax = Math.round(price * 0.21 * 100) / 100.0;
        double finalPrice = price - tax;

        // Ustawiamy DecimalFormat z Locale.US, by separator dziesiętny był kropką i zawsze były 2 miejsca po kropce
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(','); // opcjonalnie, jeśli chcesz grupowanie
        DecimalFormat df = new DecimalFormat("0.00", symbols);

        String formattedPrice = df.format(price);
        String formattedTax = df.format(tax);
        String formattedFinalPrice = df.format(finalPrice);

        // Odczytujemy pozostałe dane z request
        String email = request.getOrDefault("email", "");
        String userName = request.getOrDefault("userName", "");
        String street = request.getOrDefault("street", "");
        String city = request.getOrDefault("city", "");
        String wojew = request.getOrDefault("wojew", "");
        String productName = request.getOrDefault("productName", "");
        String productColor = request.getOrDefault("productColor", "");
        String size = request.getOrDefault("size", "");
        String photo = request.getOrDefault("photo", "");
        String productCode = request.getOrDefault("productCode", "");

        // Mapujemy zmienne – klucze muszą odpowiadać placeholderom w szablonie e-mail
        variables.put("order_number", orderNumber);
        variables.put("price", formattedPrice);
        variables.put("tax", formattedTax);
        variables.put("final_price", formattedFinalPrice);
        variables.put("email", email);
        variables.put("user_name", userName);
        variables.put("street", street);
        variables.put("city", city);
        variables.put("wojew", wojew);
        variables.put("product_name", productName);
        variables.put("product_color", productColor);
        variables.put("size", size);
        variables.put("photo", photo);
        variables.put("product_code", productCode);

        return variables;
    }

    private static String generateConfirmedOrderNumber() {
        // Generujemy 10-cyfrowy numer zamówienia
        long randomNum = 1000000000L + (long) (Math.random() * 9000000000L);
        return String.valueOf(randomNum);
    }
}
