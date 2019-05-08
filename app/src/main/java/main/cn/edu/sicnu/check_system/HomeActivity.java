package main.cn.edu.sicnu.check_system;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import main.cn.edu.sicnu.check_system.Constances.Constances;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG="HomeActivity";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        webView = findViewById(R.id.home_page);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        Log.d(TAG, "onCreate: "+Constances.getHomepageServerUrl()+"/"+Constances.getCurrentCid());
        webView.loadUrl(Constances.getHomepageServerUrl()+"/"+Constances.getCurrentCid());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
