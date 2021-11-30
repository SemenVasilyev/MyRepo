package com.example.demo;

import java.util.ArrayList;

/**
 * @author Semen V
 * @created 30|11|2021
 */
public class Task1 {
    public static String replase(String src, String cutStr, String insertStr) {
        char[] charsSrc = src.toCharArray();
        char[] charsCutStr = cutStr.toCharArray();

        int indexOnCutStr = 0;

        StringBuilder temp = new StringBuilder();
        StringBuilder result = new StringBuilder();

        if (src.length() > cutStr.length()) {
            for (int i = 0; i < charsSrc.length; i++) {
                char s = charsSrc[i];
                char c = charsCutStr[indexOnCutStr];

                if (s != c) {
                    result.append(temp);
                    temp.setLength(0);
                    indexOnCutStr = 0;
                    result.append(s);
                }else {
                    temp.append(s);
                    indexOnCutStr++;
                }
                if (indexOnCutStr == charsCutStr.length) {
                    result.append(insertStr);
                    indexOnCutStr = 0;
                    temp.setLength(0);
                }
            }
            return result.toString();
        }
        return src;
    }
}
