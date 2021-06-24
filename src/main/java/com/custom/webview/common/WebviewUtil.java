package com.custom.webview.common;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.HashSet;

public class WebviewUtil {
    public static void setCookieToURLWebview(Context context, String url) {
        try {
            HashSet<String> cookies = (HashSet<String>) Preference.getStringSet(context, Constants.SHARED_PREF_COOKIES, new HashSet<String>());
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            for (String cookie : cookies) {
                cookieManager.setCookie(url, cookie);
            }
            cookieSyncManager.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
