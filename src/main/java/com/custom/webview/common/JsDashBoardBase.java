package com.custom.webview.common;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class JsDashBoardBase {

    public interface switchCommandJS {
    }

    @JavascriptInterface
    public void mobAppSDKexecute(String command, String params) {
        Log.d("AppSDKexecute,","command: "+command+"\n params: "+params);
        switch (command) {
            default:
                break;
        }
    }

}
