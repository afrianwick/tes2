package com.pertamina.portal.iam.activity;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.utils.WebViewUtil;

import static com.pertamina.portal.iam.utils.WebViewUtil.INFO_URL_INTENT_KEY;

public class WebViewA extends AppCompatActivity {

    private WebView web_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        web_view = findViewById(R.id.web_view);

        WebViewUtil.webViewSetup(web_view, "https://apps.pertamina.com/sdmonline_iam/personal/COC_Read.aspx");
    }
}
