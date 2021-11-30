package com.example.demo;

/**
 * @author Semen V
 * @created 30|11|2021
 */
public class Task2 {
    private static final String regExInt = "[-+]?\\d+";
    private static final String regExDouble = "((-|\\\\+)?[0-9]+(\\\\.[0-9]+)?)+";

    public static int stringToInt(String string) {
        // без проверки принадлежности к инт диапазону
        if (string.matches(regExInt)) {
            boolean negative = false;
            int i = 0;
            int len = string.length();

            char firstChar = string.charAt(0);
            if (firstChar < '0') {
                if (firstChar == '-') {
                    negative = true;
                }
                i++;
            }
            int result = 0;
            while (i < len) {
                int digit = Character.digit(string.charAt(i++), 10);
                result *= 10;
                result -= digit;
            }
            return negative ? result : -result;
        }
        throw new IllegalArgumentException(string);
    }

    public static double stringToDouble(String string) {
        if (string.matches(regExDouble)) {
            return Double.parseDouble(string);  // ¯\_(*_*)_/¯
        }
        throw new IllegalArgumentException(string);
    }

}
