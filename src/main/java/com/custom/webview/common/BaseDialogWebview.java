package com.custom.webview.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.db.dashboard.R;
import com.custom.webview.CustomWebView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseDialogWebview extends DialogFragment implements KeyboardHeightObserver {

    public Activity mActivity;
    View tmp;

    public CustomWebView webView;
    public static String URL_WEBVIEW = "URL_WEBVIEW";
    private IWebViewClientListener clientListener;
    LinearLayout layoutKeyboardSpace;
    private KeyboardHeightProvider keyboardProvider;
    public static boolean isShowLogo = false;
    ImageView btn_close;
    Map<String, String> dataHeader;

    /**
     * Layout of activity
     *
     * @return id of resource layout
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogTheme);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_custom_webview, container, false);
        //Setting dialog background and animation
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setDimAmount(0.8f);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public static boolean isShow() {
        return isShowLogo;
    }

    @Override
    public void onResume() {
        isShowLogo = true;
        super.onResume();
        keyboardProvider.setKeyboardHeightObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardProvider.setKeyboardHeightObserver(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        keyboardProvider.close();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void showProgressDialog(boolean show, String mess) {
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    protected void initView(View v) {
        clientListener = getWebListener();
        tmp = v.findViewById(R.id.layout_tmp);
        webView = (CustomWebView) v.findViewById(R.id.webview);
        initWebView();
        btn_close = (ImageView) v.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        layoutKeyboardSpace = (LinearLayout) v.findViewById(R.id.layout_keyboard_space);
        keyboardProvider = new KeyboardHeightProvider(mActivity);
        keyboardProvider.start();

    }

    protected void setTitleForm(String title) {

    }


    public void initWebView() {
        try {
            webView.clearCache(true);
            webView.setInitialScale((int) DeviceUtils.getDensity(Objects.requireNonNull(getActivity())));
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setBuiltInZoomControls(false);
            webSettings.setAllowFileAccess(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadsImagesAutomatically(true);

            webView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onProgressChanged(WebView view, int progress) {
//                    progressbarPredict.setVisibility(View.VISIBLE);
//                    progressbarPredict.setProgress(progress);
                }
            });

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    showProgressDialog(true, "");
                    if (Utils.isValidUrl(url)) {
                        loadUrlWithLibraryHeaders(url);
                        return true;
                    }
                    if (clientListener != null)
                        clientListener.shouldOverrideUrlLoading(view, url);
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @SuppressLint("JavascriptInterface")
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    showProgressDialog(true,"");
                    view.addJavascriptInterface(BaseDialogWebview.this, "android");
                    if (clientListener != null)
                        clientListener.onPageStarted(view, url, favicon);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {
                    showProgressDialog(false, "");
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    showProgressDialog(false, "");
                    if (clientListener != null)
                        clientListener.onPageFinished(view, url);
                }


            });
            if (getJsHandler() != null)
                webView.addJavascriptInterface(getJsHandler(), "JsHandler");
            String url = getArguments().getString(URL_WEBVIEW, "");
            if (Utils.isValidUrl(url)) {
                loadUrlWithLibraryHeaders(url);
            }
            webView.setTag("RegisterFragment");
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }


    public void loadUrlWithLibraryHeaders(String url) {
        try {
            if (Utils.isValidUrl(url)) {
                Context context = Objects.requireNonNull(getActivity()).getApplicationContext();
                Uri uri = Uri.parse(url);
                WebviewUtil.setCookieToURLWebview(webView.getContext(), url);

                webView.stopLoading();
                if (dataHeader == null)
                    dataHeader = new HashMap<>();
                webView.loadUrl(uri.toString(), dataHeader);
            } else {
                loadUrl(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUrl(String url) {
        if (isAdded()) {
            webView.stopLoading();
            webView.loadUrl(url);
        }
    }

    public void goBack() {
        if (webView != null)
            webView.post(new Runnable() {
                @Override
                public void run() {
                    if (webView.canGoBack())
                        webView.goBack();
                }
            });
    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {
        changeKeyboardHeight(height);
    }

    private void changeKeyboardHeight(int height) {
        try {
            if (height > 100) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                layoutKeyboardSpace.setLayoutParams(params);
                layoutKeyboardSpace.setVisibility(View.VISIBLE);
                return;
            }

            layoutKeyboardSpace.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract JsDashBoardBase getJsHandler();

    protected abstract IWebViewClientListener getWebListener();

    public void setDataHeader(Map<String, String> dataHeader) {
        this.dataHeader = dataHeader;
    }
}
