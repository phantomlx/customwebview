package com.custom.webview.common;

import java.util.Locale;

public class Utils {
    public static boolean isValidUrl(String url) {
        try {
            String u = url.toLowerCase(Locale.ENGLISH);
            return u.startsWith("http://")
                    || u.startsWith("https://");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
