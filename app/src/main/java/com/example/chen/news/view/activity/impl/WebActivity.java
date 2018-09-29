package com.example.chen.news.view.activity.impl;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.chen.news.R;
import com.example.chen.news.view.activity.i.IWebActivity;

/**
 * Created by chen on 2018/9/19.
 */
public class WebActivity extends AppCompatActivity implements IWebActivity {

    private WebView mWebView;
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        if (getIntent().getStringExtra("uri") != null) {
            mUrl = getIntent().getStringExtra("uri");
        }
        bindView();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void bindView() {
        mWebView = findViewById(R.id.wv_news);
        mWebView.loadUrl(mUrl);
        mWebView.addJavascriptInterface(this,"android");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
    }
}
