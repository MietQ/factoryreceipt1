package com.factoryreceipt.factoryreceipt.customshops;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ZalandoGenerator {
    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();
        String order_number = generateOrderNumber(); // Generujemy unikalny numer zamówienia

        // Mapowanie pól zgodnie z wymaganiami funkcji send_receipt_email:
        // delivery_time     <- orderDate
        // delivery_arrived  <- deliveryArrived
        // address           <- address
        // city              <- city
        // user_name         <- userName (pełna nazwa użytkownika)
        // first_name        <- pierwsza część userName (lub "Klient", jeśli puste)
        // product_name      <- productName
        // product_cat       <- productCat (lub domyślnie "Brak")
        // product_color     <- productColor (lub domyślnie "Brak")
        // size              <- size
        // price             <- sformatowana cena
        // order_number      <- wygenerowany numer zamówienia

        variables.put("delivery_time", request.get("orderDate"));
        variables.put("delivery_arrived", request.get("deliveryArrived"));
        variables.put("address", request.get("address"));
        variables.put("city", request.get("city"));
        variables.put("photo", request.get("photo"));

        String userName = request.get("userName");
        variables.put("user_name", userName);
        variables.put("first_name", (userName != null && !userName.isEmpty()) ? userName.split(" ")[0] : "Klient");

        variables.put("product_name", request.get("productName"));
        variables.put("product_cat", request.containsKey("productCat") ? request.get("productCat") : "Brak");
        variables.put("product_color", request.containsKey("productColor") ? request.get("productColor") : "Brak");
        variables.put("size", request.get("size"));

        // Normalizacja pola price – zawsze wyświetlamy np. "729,00"
        variables.put("price", formatPrice(request.get("price")));

        variables.put("order_number", order_number);

        return variables;
    }

    private static String generateOrderNumber() {
        long randomNum = 10000000000000L + (long)(Math.random() * 90000000000000L);
        return String.valueOf(randomNum);
    }

    // Metoda formatująca cenę na postać "0,00"
    private static String formatPrice(String input) {
        if (input == null || input.isEmpty()) {
            return "0,00";
        }
        // Usuwamy wszystko poza cyframi, przecinkiem i kropką
        String sanitized = input.replaceAll("[^0-9,\\.]", "");
        // Jeśli występuje tylko przecinek i nie występuje kropka – traktujemy przecinek jako separator dziesiętny
        if (sanitized.contains(",") && !sanitized.contains(".")) {
            sanitized = sanitized.replace(",", ".");
        } else if (sanitized.contains(",") && sanitized.contains(".")) {
            // Jeśli oba występują, zakładamy, że kropki to separatory tysięcy, a przecinek – separator dziesiętny
            sanitized = sanitized.replace(".", "");
            sanitized = sanitized.replace(",", ".");
        }
        double value;
        try {
            value = Double.parseDouble(sanitized);
        } catch (NumberFormatException e) {
            value = 0.0;
        }
        // Ustawiamy DecimalFormat z polskimi symbolami – przecinek jako separator dziesiętny
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pl", "PL"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        return df.format(value);
    }
}
