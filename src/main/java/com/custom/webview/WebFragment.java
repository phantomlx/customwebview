package com.custom.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.custom.webview.common.BaseDialogWebview;
import com.custom.webview.common.IWebViewClientListener;
import com.custom.webview.common.JsDashBoardBase;

import java.util.HashMap;
import java.util.Map;


public class WebFragment extends BaseDialogWebview implements IWebViewClientListener {
    public static String DB_URL = "dashboard_url";
    JsDashBoardBase jsDashboard;

    public static WebFragment newInstance(Map<String, String> header, String url, JsDashBoardBase jsDashBoard) {
        WebFragment frag = new WebFragment();
        Bundle args = new Bundle();
        Log.d(WebFragment.class.getSimpleName(), "url: " + url);
        args.putString(URL_WEBVIEW, url);
        frag.setArguments(args);
        frag.setDataHeader(header);
        frag.setJsDashboard(jsDashBoard);
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected JsDashBoardBase getJsHandler() {
        return jsDashboard;
    }

    @Override
    protected IWebViewClientListener getWebListener() {
        return this;
    }


    @Override
    public void shouldOverrideUrlLoading(WebView view, String url) {

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {

    }

    public void setJsDashboard(JsDashBoardBase jsDashboard) {
        this.jsDashboard = jsDashboard;
    }
}

