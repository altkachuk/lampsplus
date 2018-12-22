package com.zugara.atproj.lampsplus.utils;

/**
 * Created by andre on 21-Dec-18.
 */

public class StringUtil {

    public static boolean isSessionNameLegal(String name) {
        if (name.length() < 4) return false;

        String reservedChars = "?:\"*|/\\<>";
        for (int i = 0;  i < name.length(); i++) {
            String ch = name.substring(i, i+1);
            if (reservedChars.indexOf(ch) > -1)
                return false;
        }
        return true;
    }
}
