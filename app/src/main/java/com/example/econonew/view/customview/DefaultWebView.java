package com.example.econonew.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * WebView 的功能封装类
 * Created by mengfei on 2016/11/5.
 */

public class DefaultWebView extends WebView {

    private ProgressBar progressBar;

    public DefaultWebView(Context context) {
        this(context, null);
    }

    public DefaultWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView();
    }

    /**
     * 设置要进行提示的ProgressBar
     */
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    private void initWebView() {
        WebSettings setting = this.getSettings();
        setting.setJavaScriptEnabled(true);// 启用JavaScript
        setting.setDefaultTextEncodingName("utf-8");//设置默认的字符编码
        this.setWebViewClient(new DefaultWebViewClient());
        this.setWebChromeClient(new DefaultWebChromeClient());

    }

    public void loadUrl(String url) {
        if (url == null) {
            super.loadUrl("file:///android_asset/tip/not_fund_url_tip.html");
        } else {
            super.loadUrl(url);
        }
    }

    private class DefaultWebViewClient extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            loadUrl("file:///android_asset/tip/not_fund_url_tip.html");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;// 返回值为true的时候在WebView里面显示
        }
    }

    private class DefaultWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                closeDialog();// 页面加载完毕
            } else {

                openDialog(newProgress);// 页面正在加载
            }
            super.onProgressChanged(view, newProgress);
        }

        // 显示progress
        private void openDialog(int progress) {
            if (progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE);
            }
            progressBar.setProgress((int) (Math.sqrt(progress) * 10));

        }

        // 隐藏progress
        private void closeDialog() {
            progressBar.setVisibility(View.GONE);
        }
    }
}
