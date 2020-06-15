package com.test.weatherforcast;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.test.weatherforcast.R;
//用来加载中国天气网的天气数据
public class LoadUrl extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_main);
        // 获取布局管理器中添加的WebView控件
        WebView webview=(WebView)findViewById(R.id.webView);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webview.loadUrl(url); // 指定要加载的网页
        //实现放大缩小的功能
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        ImageView ig = findViewById(R.id.note_back);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) { // 4.0以上必须要加
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }
        });
    }
}
