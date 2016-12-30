package com.example.econonew.view.customview;

import android.content.Context;
import android.text.TextUtils;
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

    private String getUTF8Html(String msg) {
        String moban = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><p style=\"font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Helvetica,Arial,sans-serif,'Apple Color Emoji','Segoe UI Emoji','Segoe UI Symbol';font-size: 16px;line-height: 1.5;text-align: center;\">{1}</p></html>";
        return moban.replace("{1}", msg);
    }

    /**
     * 加载URL，如果URL为空则显示后面的content内容，否则就去加载之前的URL地址
     *
     * @param url     url地址
     * @param content 如果url为空要去加载的内容
     */
    public void loadUrl(String url, String content) {
        if (url == null) {
            if (TextUtils.isEmpty(content.trim())) {
                super.loadUrl("file:///android_asset/tip/not_fund_url_tip.html");
            } else {
                this.loadData(getUTF8Html(content), "text/html; charset=UTF-8", null);
            }
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
