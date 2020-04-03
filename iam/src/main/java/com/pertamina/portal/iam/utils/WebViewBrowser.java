package com.pertamina.portal.iam.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewBrowser extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }


}
