package com.weiwu.ql.main.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.weiwu.ql.R;

/**
 * @author Joseph_Yan
 * on 2020/7/21
 * 新闻公告
 */
public class NewsActivity extends Activity {

    private TitleBarLayout mTitleBar;
    private String url;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
        }
        mWebView = findViewById(R.id.webView);
        mTitleBar = findViewById(R.id.webview_title);
        mTitleBar.setTitle(getResources().getString(R.string.announcement_title), TitleBarLayout.POSITION.MIDDLE);
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 设置无边框
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);                      //自适应屏幕
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);       //缓存模式
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //按返回键操作并且能回退网页
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        //后退
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
// 设置回退
// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }

        return super.onKeyDown(keyCode,event);
    }
}
