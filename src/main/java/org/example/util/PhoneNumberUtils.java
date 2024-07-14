package org.example.util;

public class PhoneNumberUtils {

    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        // Удаление всего, кроме цифр и плюса
        String cleanedPhoneNumber = phoneNumber.replaceAll("[^0-9+]", "");

        // Если номер начинается не с "+", добавляем "+"
        if (!cleanedPhoneNumber.startsWith("+")) {
            cleanedPhoneNumber = "+" + cleanedPhoneNumber;
        }

        // Удаление пробелов
        cleanedPhoneNumber = cleanedPhoneNumber.replaceAll("\\s+", "");

        return cleanedPhoneNumber;
    }
}
