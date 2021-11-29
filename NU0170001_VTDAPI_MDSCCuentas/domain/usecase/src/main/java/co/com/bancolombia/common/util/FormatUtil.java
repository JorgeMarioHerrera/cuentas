package co.com.bancolombia.common.util;

import co.com.bancolombia.model.redis.UserTransactional;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FormatUtil {
    public static String toTitleCase(String text) {
        return Arrays
                .stream(text.split(" "))
                .map(word -> word.isEmpty() || "de".equalsIgnoreCase(word)
                        ? word.toLowerCase()
                        : (Character.toTitleCase(word.charAt(0)) + word
                        .substring(1)
                        .toLowerCase()))
                .collect(Collectors.joining(" "));
    }
    public static String formatName(UserTransactional user) {
        return Stream.of(user.getFirstName(),
                user.getSecondName(),
                user.getFirstSurName(),
                user.getSecondSurName())
                .filter(string -> string != null && !string.isEmpty())
                .collect(Collectors.joining(" "));
    }

    @SuppressWarnings("java:S2301")
    public static String booleanToConfirmation(boolean value) {
        if (value) {
            return "Si";
        } else {
            return "No";
        }
    }
}
