package com.factoryreceipt.factoryreceipt.util;

import java.util.Map;
import java.util.Random;

public class StockxDataFormatter {

    public static Map<String, String> formatStockxData(String productName, String priceInput, String styleid, String size, String deliveryTime, String photo) {
        // Parsowanie ceny
        String cleanPrice = priceInput.replaceAll("[^\\d.]", "");
        double priceValue;
        try {
            priceValue = cleanPrice.isEmpty() ? 0.0 : Double.parseDouble(cleanPrice);
        } catch (NumberFormatException e) {
            priceValue = 0.0;
        }

        Random random = new Random();
        double feePercentage = (4 + random.nextDouble() * (6 - 4)) / 100.0;
        double fee = Math.round(priceValue * feePercentage * 100.0) / 100.0;
        double shipping = 15.41 + random.nextDouble() * (24.94 - 15.41);
        shipping = Math.round(shipping * 100.0) / 100.0;
        double finalPrice = Math.round((priceValue + fee + shipping) * 100.0) / 100.0;

        long part1 = 10000000L + random.nextInt(90000000);
        long part2 = 10000000L + random.nextInt(90000000);
        String orderNumber = part1 + "-" + part2;

        // Formatowanie cen
        String formattedPrice = (priceValue == Math.floor(priceValue))
                ? "$" + (int) priceValue
                : String.format("$%.2f", priceValue);
        String formattedFee = (fee == Math.floor(fee))
                ? "$" + (int) fee
                : String.format("$%.2f", fee);
        String formattedShipping = (shipping == Math.floor(shipping))
                ? "$" + (int) shipping
                : String.format("$%.2f", shipping);
        String formattedFinalPrice = (finalPrice == Math.floor(finalPrice))
                ? "$" + (int) finalPrice
                : String.format("$%.2f", finalPrice);

        return Map.of(
                "productName", productName,
                "price", formattedPrice,
                "fee", formattedFee,
                "shipping", formattedShipping,
                "finalPrice", formattedFinalPrice,
                "orderNumber", orderNumber,
                "styleid", (styleid != null && !styleid.isEmpty()) ? styleid : "Brak",
                "size", (size != null && !size.isEmpty()) ? size : "Brak",
                "deliveryTime", (deliveryTime != null && !deliveryTime.isEmpty()) ? deliveryTime : "Brak",
                "photo", (photo != null && !photo.isEmpty()) ? photo : "Brak"
        );
    }
}
