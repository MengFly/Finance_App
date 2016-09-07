package com.example.econonew.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.econonew.R;

@SuppressLint("SetJavaScriptEnabled")
public class MsgContentActivity extends BaseActivity {

	private ImageButton backBtn, refreshBtn;
	private TextView msgTitle;
	private ProgressBar progressBar;

	private WebView msgContent;

	private String msgContentUrl;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_msg_item_content);

		msgTitle = (TextView) findViewById(R.id.msg_item_content_title);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		msgContent = (WebView) findViewById(R.id.msg_item_content_view);
		msgContent.setWebViewClient(new MsgWebViewClient());
		msgContent.setWebChromeClient(new MsgWebChromeClient());
		backBtn = (ImageButton) findViewById(R.id.act_msg_content_back_ib);
		refreshBtn = (ImageButton) findViewById(R.id.act_msg_content_refresh_ib);
		WebSettings webSettings = msgContent.getSettings();
		webSettings.setJavaScriptEnabled(true);// 启用JavaScript
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 设置缓存
		initListener();
	}

	private void initListener() {
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MsgContentActivity.this.finish();
			}
		});
		refreshBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadUrl(msgContentUrl);
			}
		});
	}

	@Override
	protected void initDatas() {
		Intent intent = getIntent();
		msgTitle.setText(intent.getStringExtra("msgTitle"));// 设置title
		msgContentUrl = intent.getStringExtra("msgContentUrl");
		loadUrl(msgContentUrl);
	}

	private void loadUrl(String url) {
		if (url == null) {
			msgContent.loadUrl("file:///android_asset/tip/not_fund_url_tip.html");
		} else {
			msgContent.loadUrl(url);
		}
	}

	// 重写返回按键的方法
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && msgContent.canGoBack()) {
			msgContent.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class MsgWebViewClient extends WebViewClient {

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

	// 当网页加载过程中要进行的操作
	class MsgWebChromeClient extends WebChromeClient {

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

