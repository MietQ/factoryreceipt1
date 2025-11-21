package com.factoryreceipt.factoryreceipt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Wysyła e-mail na podstawie szablonu HTML.
     * W zależności od sklepu ładuje odpowiedni szablon, ustawia domyślny tytuł oraz nadawcę.
     *
     * @param to        adres odbiorcy
     * @param subject   tytuł wiadomości; jeśli null lub pusty, ustalany jest domyślnie
     * @param variables mapa zmiennych, które zastąpią placeholdery w szablonie
     * @param store     nazwa sklepu (np. "StockX", "MediaExpert" lub "Zalando")
     */
    public void sendStockxEmail(String to, String subject, Map<String, Object> variables, String store) {
        // Ustal domyślny tytuł
        if (subject == null || subject.isEmpty()) {
            if ("StockX".equalsIgnoreCase(store)) {
                subject = "\uD83C\uDF89Order Delivered: {{ productName }}";
            } else if ("MediaExpert".equalsIgnoreCase(store)) {
                subject = "Twoje zamówienie zostało opłacone";
            } else if ("Zalando".equalsIgnoreCase(store)) {
                subject = "Otrzymaliśmy Twoje zamówienie";
            } else if ("Dropsy".equalsIgnoreCase(store)) {
                subject = "Potwierdzenie zamówienia nr: {{ orderNumber }}";
            } else if ("Vitkac".equalsIgnoreCase(store)) {
                subject = "Otrzymaliśmy Twoje zamówienie";
            } else if ("Grailed".equalsIgnoreCase(store)) {
                subject = "Your order has been shipped!";
            } else if ("Farfetch".equalsIgnoreCase(store)) {
                subject = "Thank you for placing your order. Here's what you can expect next";
            } else if ("Dorawa".equalsIgnoreCase(store)) {
                subject = "Potwierdzenie zamówienia nr: {{ order_number }}";
            } else if ("Corteiz".equalsIgnoreCase(store)) {
                subject = "Order {{ order_number }} confirmed";
            } else if ("Nike".equalsIgnoreCase(store)) {
                subject = "Właśnie dotarło do nas Twoje zamówienie";
            } else if ("Confirmed".equalsIgnoreCase(store)) {
                subject = "Właśnie dotarło do nas Twoje zamówienie";
            } else if ("Snkrs".equalsIgnoreCase(store)) {
                subject = "Właśnie dotarło do nas Twoje zamówienie";
            } else if ("grail point".equalsIgnoreCase(store)) {
                subject = "[Grail Point] Otrzymaliśmy twoje zamówienie!";
            } else if ("Moncler".equalsIgnoreCase(store)) {
                subject = "Order {{ order_number }} confirmed";
            } else if ("Trapstar".equalsIgnoreCase(store)) {
                subject = "Order {{ order_number }} confirmed";
            } else if ("Notino".equalsIgnoreCase(store)) {
                subject = "Zamówienie zostało złożone! Numer zamówienia to {{ order_number }}.";
            } else if ("Supreme".equalsIgnoreCase(store)) {
                subject = "Your order has been shipped!";
            } else if ("Goat".equalsIgnoreCase(store)) {
                subject = "Your order has been shipped!";
            } else if ("Ralph Lauren".equalsIgnoreCase(store)) {
                subject = "Your order has been shipped!";
            } else if ("Louis Vuitton".equalsIgnoreCase(store)) {
                subject = "Your order has been shipped!";
            } else if ("Balenciaga".equalsIgnoreCase(store)) {
                subject = "Your order has been shipped!";

            } else {
                subject = "Potwierdzenie zamówienia";
            }
        }

        // Wybierz szablon w zależności od sklepu
        String templateName;
        if ("StockX".equalsIgnoreCase(store)) {
            templateName = "stockxTemplate";
        } else if ("MediaExpert".equalsIgnoreCase(store)) {
            templateName = "mediaexpertTemplate";
        } else if ("Zalando".equalsIgnoreCase(store)) {
            templateName = "zalandoTemplate";
        } else if ("Dropsy".equalsIgnoreCase(store)) {
            templateName = "dropsyTemplate";
        } else if ("Vitkac".equalsIgnoreCase(store)) {
            templateName = "vitkacTemplate";
        } else if ("Grailed".equalsIgnoreCase(store)) {
            templateName = "grailedTemplate";
        } else if ("Farfetch".equalsIgnoreCase(store)) {
            templateName = "farfetchTemplate";
        } else if ("Dorawa".equalsIgnoreCase(store)) {
            templateName = "dorawaTemplate";
        } else if ("Corteiz".equalsIgnoreCase(store)) {
            templateName = "corteizTemplate";
        } else if ("Nike".equalsIgnoreCase(store)) {
            templateName = "nikeTemplate";
        } else if ("Confirmed".equalsIgnoreCase(store)) {
            templateName = "confirmedTemplate";
        } else if ("Snkrs".equalsIgnoreCase(store)) {
            templateName = "snkrsTemplate";
        } else if ("grail point".equalsIgnoreCase(store)) {
            templateName = "grailpointTemplate";
        } else if ("moncler".equalsIgnoreCase(store)) {
            templateName = "monclerTemplate";
        } else if ("trapstar".equalsIgnoreCase(store)) {
            templateName = "trapstarTemplate";
        } else if ("notino".equalsIgnoreCase(store)) {
            templateName = "notinoTemplate";
        } else if ("supreme".equalsIgnoreCase(store)) {
            templateName = "supremeTemplate";
        } else if ("goat".equalsIgnoreCase(store)) {
            templateName = "goatTemplate";
        } else if ("Ralph Lauren".equalsIgnoreCase(store)) {
            templateName = "ralphlaurenTemplate";
        } else if ("Balenciaga".equalsIgnoreCase(store)) {
            templateName = "balenciagaTemplate";
        } else if ("Louis Vuitton".equalsIgnoreCase(store)) {
            templateName = "louisvuittonTemplate";


        } else {
            templateName = "defaultTemplate";
        }

        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String key = entry.getKey();
            String valueStr = (entry.getValue() == null) ? "" : entry.getValue().toString();
            String placeholder1 = "{{ " + key + " }}";
            String placeholder2 = "{{" + key + "}}";
            subject = subject.replace(placeholder1, valueStr);
            subject = subject.replace(placeholder2, valueStr);
        }


        try {
            // Wczytaj szablon HTML
            ClassPathResource resource = new ClassPathResource("templates/" + templateName + ".html");
            String template = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            // Zamień placeholdery (np. {{ orderNumber }}) na wartości z mapy
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                String key = entry.getKey();
                String valueStr = (entry.getValue() == null) ? "" : entry.getValue().toString();
                String placeholder1 = "{{ " + key + " }}";
                String placeholder2 = "{{" + key + "}}";
                template = template.replace(placeholder1, valueStr);
                template = template.replace(placeholder2, valueStr);
            }

            // Utwórz wiadomość e-mail
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            // Ustawienie nadawcy w zależności od sklepu
            if ("StockX".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@stockx.com", "StockX");
            } else if ("MediaExpert".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@mediaexpert.pl", "MediaExpert");
            } else if ("Zalando".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@zalando.com", "Zalando Team");
            } else if ("Dropsy".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@Dropsy.pl", "Dropsy Store");
            } else if ("Vitkac".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@Vitkac.pl", "Vitkac");
            } else if ("Grailed".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@grailed.com", "Grailed");
            } else if ("Farfetch".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@farfetch.com", "Farfetch");
            } else if ("Dorawa".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@Dorawa.pl", "dorawastore");
            } else if ("Corteiz".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@Corteiz.xyz", "CRTZRTW");
            } else if ("Nike".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@nike.com", "Nike");
            } else if ("Confirmed".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@confirmed.com", "Confirmed");
            } else if ("Snkrs".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@Snkrs.com", "Snkrs");
            } else if ("grail point".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@grailpoint.com", "Grail Point");
            } else if ("moncler".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@moncler.com", "Moncler");
            } else if ("trapstar".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@trapstar.com", "Trapstar London");
            } else if ("notino".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@notino.com", "Notino");
            } else if ("supreme".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@supreme.com", "Supreme");
            } else if ("goat".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@goat.com", "Goat");
            } else if ("Ralph Lauren".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@ralphlauren.com", "Ralph Lauren");
            } else if ("Balenciaga".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@balenciaga.com", "Balenciaga");
            } else if ("Louis Vuitton".equalsIgnoreCase(store)) {
                helper.setFrom("no-reply@louisvuitton.com", "Louis Vuitton");
            } else {
                helper.setFrom("no-reply@default.com", "Default Store");


            }

            helper.setText(template, true); // true – e-mail HTML

            mailSender.send(mimeMessage);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
