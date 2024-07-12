package org.example.service;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private static final String LOGIN = "583453038";
    private static final String PASSWORD = "ded3cb1c0b4a02de12ce683e609a1355e12206ad";
    private static final String SMS_URL = "https://smsc.kz/sys/send.php";
    private static final String CHARSET = StandardCharsets.UTF_8.name();

    public String sendVerificationCode(String phoneNumber) throws IOException {
        String verificationCode = generateVerificationCode();
        String message = "Ваш код подтверждения: " + verificationCode;

        try {
            String encodedLogin = URLEncoder.encode(LOGIN, CHARSET);
            String encodedPassword = URLEncoder.encode(PASSWORD, CHARSET);
            String encodedPhone = URLEncoder.encode(phoneNumber, CHARSET);
            String encodedMessage = URLEncoder.encode(message, CHARSET);

            HttpResponse<String> response = Unirest.get(SMS_URL)
                    .queryString("login", encodedLogin)
                    .queryString("psw", encodedPassword)
                    .queryString("phones", encodedPhone)
                    .queryString("mes", encodedMessage)
                    .queryString("fmt", 1)
                    .queryString("charset", CHARSET)
                    .asString();

            logger.info("SMS response: {}", response.getBody());
            handleSmsResponse(response.getBody());
        } catch (Exception e) {
            String errorMessage = "Ошибка при отправке СМС: " + e.getMessage();
            logger.error(errorMessage);
            throw new IOException(errorMessage, e);
        }

        return verificationCode;
    }

    private void handleSmsResponse(String response) throws IOException {
        logger.info("Handling SMS response: {}", response);
        String[] parts = response.split(",");
        if (parts.length > 1) {
            if (Integer.parseInt(parts[1]) > 0) {
                logger.info("Сообщение отправлено успешно. ID: {}, всего SMS: {}, стоимость: {}, баланс: {}",
                        parts[0], parts[1], parts.length > 2 ? parts[2] : "N/A", parts.length > 3 ? parts[3] : "N/A");
            } else {
                String errorMessage = "Ошибка при отправке СМС: " + parts[1];
                logger.error(errorMessage);
                throw new IOException(errorMessage);
            }
        } else {
            String errorMessage = "Ошибка при отправке СМС: Недостаточно данных в ответе сервера.";
            logger.error(errorMessage);
            throw new IOException(errorMessage);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        logger.info("Сгенерирован код подтверждения: {}", code);
        return String.valueOf(code);
    }
}
