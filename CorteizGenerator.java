package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CorteizGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Generujemy unikalny numer zamówienia – przykładowo 10-cyfrowy
        String orderNumber = generateCorteizOrderNumber();

        // Pobieramy i parsujemy cenę
        String priceStr = request.getOrDefault("price", "0");
        double price = 0.0;
        // Zamieniamy ewentualny przecinek na kropkę, by poprawnie sparsować double
        priceStr = priceStr.replace(',', '.');
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            price = 0.0;
        }

        // Obliczamy podatek oraz finalną cenę (np. dodajemy koszt przesyłki)
        double tax = price * 0.20;
        double finalPrice = price + 28.06;

        // Przygotowujemy formatera cenowego – ustawienia US:
        // Separator tysięcy: przecinek, separator dziesiętny: kropka, zawsze 2 miejsca po kropce
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);

        String formattedPrice = df.format(price);
        String formattedFinalPrice = df.format(finalPrice);
        String formattedTax = df.format(tax);

        // Odczytujemy dane z formularza
        String email       = request.getOrDefault("email", "");
        String productName = request.getOrDefault("product_name", "");
        String size        = request.getOrDefault("size", "");
        String userName    = request.getOrDefault("userName", "");
        String street      = request.getOrDefault("street", "");
        String city        = request.getOrDefault("city", "");
        String country     = request.getOrDefault("country", "");
        String photo       = request.getOrDefault("photo", "");

        // Mapujemy zmienne – klucze muszą odpowiadać placeholderom w szablonie e-mail
        variables.put("order_number", orderNumber);
        variables.put("price", formattedPrice);
        variables.put("tax", formattedTax);
        variables.put("final_price", formattedFinalPrice);
        variables.put("visa_number", generateVisaCardSuffix());
        variables.put("email", email);
        variables.put("user_name", userName);
        variables.put("address", street);
        variables.put("city", city);
        variables.put("country", country);
        variables.put("product_name", productName);
        variables.put("size", size);
        variables.put("photo", photo);

        return variables;
    }

    private static String generateCorteizOrderNumber() {
        // Generujemy 10-cyfrowy numer zamówienia
        long randomNum = 1000000000L + (long)(Math.random() * 9000000000L);
        return String.valueOf(randomNum);
    }

    private static String generateVisaCardSuffix() {
        // Generujemy 4-cyfrowy suffix karty Visa
        Random random = new Random();
        int suffix = 1000 + random.nextInt(9000);
        return String.valueOf(suffix);
    }
}
