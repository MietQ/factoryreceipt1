package com.factoryreceipt.factoryreceipt.customshops;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NotinoGenerator {

    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Pobieramy wartości z request
        String email          = request.getOrDefault("email", "");
        String productName    = request.getOrDefault("product_name", "");
        String productCat     = request.getOrDefault("product_cat", "");
        String priceInput     = request.getOrDefault("price", "0");
        String deliveryTime   = request.getOrDefault("delivery_time", "Brak");
        String deliveryArrived = request.getOrDefault("delivery_arrived", "Brak");
        String photo          = request.getOrDefault("photo", "Brak");

        // Normalizujemy cenę – usuwamy spacje i zamieniamy przecinek na kropkę
        priceInput = priceInput.replace(" ", "").replace(",", ".");
        double priceValue = 0.0;
        try {
            priceValue = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            priceValue = 0.0;
        }

        // Obliczamy finalną cenę – dodajemy stałą opłatę 14,90 zł
        double finalPriceValue = priceValue + 14.90;

        // Formatowanie cen: zawsze 2 cyfry po przecinku, przy czym wynik ma być wyświetlany z przecinkiem jako separatorem dziesiętnym
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedPrice = df.format(priceValue).replace(".", ",");
        String formattedFinalPrice = df.format(finalPriceValue).replace(".", ",");
        // Dodajemy dodatkową zmienną formatted_price = price + 14,90
        String formattedPriceWithFee = formattedFinalPrice; // identyczne jak formattedFinalPrice

        // Generujemy numer zamówienia – prefiks "NT" + 7 losowych cyfr (np. NT1316540)
        String orderNumber = generateNotinoOrderNumber();

        // Wkładamy zmienne do mapy – klucze muszą odpowiadać placeholderom w szablonie e-mail
        variables.put("order_number", orderNumber);
        variables.put("email", email);
        variables.put("product_name", productName);
        variables.put("product_cat", productCat);
        variables.put("price", formattedPrice);
        variables.put("final_price", formattedFinalPrice);
        variables.put("formatted_price", formattedPriceWithFee);
        variables.put("delivery_time", deliveryTime);
        variables.put("delivery_arrived", deliveryArrived);
        variables.put("photo", photo);

        return variables;
    }

    private static String generateNotinoOrderNumber() {
        Random random = new Random();
        int randomNumber = 1000000 + random.nextInt(9000000); // Losowa liczba 7-cyfrowa
        return "NT" + randomNumber;
    }
}
