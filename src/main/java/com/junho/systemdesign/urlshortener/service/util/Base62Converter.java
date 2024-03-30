package com.junho.systemdesign.urlshortener.service.util;

public final class Base62Converter {

    private Base62Converter() {}

    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    public static String encode(Long input) {
        validateInput(input);
        final StringBuilder sb = new StringBuilder();
        while (input > 0) {
            sb.append(BASE62[(int) (input % 62)]);
            input /= 62;
        }
        return sb.reverse().toString();
    }

    private static void validateInput(Long input) {
        if (input == null || input <= 0) {
            throw new IllegalArgumentException("invalid input");
        }
    }

}
