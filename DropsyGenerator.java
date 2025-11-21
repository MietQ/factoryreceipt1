package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DropsyGenerator {
    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Generacja 6-cyfrowego numeru zamówienia
        String orderNumber = generateOrderNumber();

        // Pobranie i parsowanie ceny (jeśli to możliwe)
        double price = 0.0;
        String priceInput = request.get("price");
        if (priceInput != null && !priceInput.isEmpty()) {
            // Zamiana przecinka na kropkę, aby umożliwić parsowanie do double
            priceInput = priceInput.replace(',', '.');
            try {
                price = Double.parseDouble(priceInput);
            } catch (NumberFormatException e) {
                price = 0.0;
            }
        }

        // Obliczenia: finalna cena (cena + 15) oraz podatek (23% ceny)
        double finalPrice = price + 15;
        double tax = Math.round(price * 0.23 * 100.0) / 100.0;

        // Przygotowanie formatera ceny – ustawienia polskie (przecinek jako separator dziesiętny)
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pl", "PL"));
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);

        // Formatowanie ceny – wyjście zawsze z dwoma miejscami dziesiętnymi
        String formattedPrice = df.format(price);

        // Mapowanie pól – pamiętaj, że formularz dla Dropsy powinien przesyłać dane pod następującymi kluczami:
        // deliveryTime, address, city, userName, productName, price, paczkomat, phoneNumber, emailDane
        variables.put("delivery_time", request.get("deliveryTime"));
        variables.put("address", request.get("address"));
        variables.put("city", request.get("city"));
        // Ustalamy first_name na podstawie userName, jeśli dostępne
        String userName = request.get("userName");
        variables.put("first_name", (userName != null && !userName.isEmpty()) ? userName.split(" ")[0] : "Klient");
        variables.put("user_name", userName);
        variables.put("product_name", request.get("productName"));
        // Używamy sformatowanej ceny
        variables.put("price", formattedPrice);
        variables.put("paczkomat", request.get("paczkomat"));
        variables.put("phone_number", request.get("phoneNumber"));
        variables.put("email_dane", request.get("emailDane"));
        variables.put("orderNumber", orderNumber);
        variables.put("final_price", df.format(finalPrice));
        variables.put("tax", df.format(tax));

        return variables;
    }

    private static String generateOrderNumber() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000); // 6-cyfrowy numer
        return String.valueOf(number);
    }
}
