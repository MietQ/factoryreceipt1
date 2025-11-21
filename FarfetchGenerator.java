package com.factoryreceipt.factoryreceipt.customshops;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class FarfetchGenerator {
    public static Map<String, Object> generate(Map<String, String> request) {
        Map<String, Object> variables = new HashMap<>();

        // Generujemy numer zamówienia jako losowy 6-znakowy ciąg alfanumeryczny (np. "2EFBHN")
        String orderNumber = generateOrderNumber();

        // Parsujemy cenę – zamieniamy przecinek na kropkę, aby umożliwić parsowanie
        double price = 0.0;
        try {
            price = Double.parseDouble(request.getOrDefault("price", "0").replace(',', '.'));
        } catch (NumberFormatException e) {
            price = 0.0;
        }

        // Finalna cena = price + 55
        double finalPrice = price + 55;

        // Przygotowanie formatera cenowego dla stylu US:
        // Separator tysięcy: przecinek, separator dziesiętny: kropka, zawsze 2 miejsca po kropce
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

        String formattedPrice = df.format(price);
        String formattedFinalPrice = df.format(finalPrice);

        // Odczytujemy pozostałe wartości z request
        String email         = request.getOrDefault("email", "");
        String userName      = request.getOrDefault("userName", "");
        String street        = request.getOrDefault("street", "");
        String city          = request.getOrDefault("city", "");
        String wojew         = request.getOrDefault("wojew", "");
        String country       = request.getOrDefault("country", "");
        String productName   = request.getOrDefault("productName", "");
        String productCat    = request.getOrDefault("product_cat", "");
        String productColor  = request.getOrDefault("productColor", "");
        String size          = request.getOrDefault("size", "");
        String photo         = request.getOrDefault("photo", "");
        String productCode   = request.getOrDefault("productCode", "");
        String deliveryTimeStr  = request.getOrDefault("delivery_time", "");

        // Automatyczne generowanie delivery_arrived na podstawie delivery_time (+2 dni robocze)
        String deliveryArrived = "";
        if (!deliveryTimeStr.isEmpty()) {
            try {
                // Przyjmujemy format wejściowy "M/d/yyyy", np. "2/18/2025"
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy", Locale.US);
                Date deliveryTimeDate = sdf.parse(deliveryTimeStr);
                Date arrivedDate = addBusinessDays(deliveryTimeDate, 2);
                deliveryArrived = sdf.format(arrivedDate);
            } catch (Exception e) {
                deliveryArrived = "";
            }
        }

        // Wkładamy zmienne do mapy – klucze odpowiadają placeholderom w szablonie e-mail
        variables.put("order_number", orderNumber);
        variables.put("price", formattedPrice);
        variables.put("final_price", formattedFinalPrice);
        variables.put("email", email);
        variables.put("user_name", userName);
        variables.put("street", street);
        variables.put("city", city);
        variables.put("wojew", wojew);
        variables.put("country", country);
        variables.put("product_name", productName);
        variables.put("product_cat", productCat);
        variables.put("product_color", productColor);
        variables.put("size", size);
        variables.put("photo", photo);
        variables.put("productCode", productCode);
        variables.put("delivery_time", deliveryTimeStr);
        variables.put("delivery_arrived", deliveryArrived);

        return variables;
    }

    // Generuje 6-znakowy alfanumeryczny ciąg, np. "2EFBHN"
    private static String generateOrderNumber() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    // Metoda dodająca określoną liczbę dni roboczych (pomijając weekendy)
    private static Date addBusinessDays(Date date, int daysToAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        while (daysToAdd > 0) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                daysToAdd--;
            }
        }
        return cal.getTime();
    }
}
