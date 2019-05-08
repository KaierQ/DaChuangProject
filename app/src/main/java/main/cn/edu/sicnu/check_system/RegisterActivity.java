package main.cn.edu.sicnu.check_system;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import main.cn.edu.sicnu.check_system.Constances.Constances;

/**
 * Created by Kaier on 2019/4/25.
 */

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        //注册新的企业号
        WebView webView = findViewById(R.id.register_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(Constances.getRegisterCompanyUrl());

    }
}
