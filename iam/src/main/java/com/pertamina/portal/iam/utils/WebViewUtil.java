package com.pertamina.portal.iam.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pertamina.portal.iam.activity.WebViewActivity;

public class WebViewUtil {

    public static String INFO_URL_INTENT_KEY = "infourlintentkey";

    public static void webViewSetup(WebView webView, String URL) {
        webView.setWebViewClient(new WebViewBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(URL);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView v, String url) {
                super.onPageFinished(v, url);
            }
        });
    }

    public static void openWebView(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INFO_URL_INTENT_KEY, url);
        context.startActivity(intent);
    }
}
