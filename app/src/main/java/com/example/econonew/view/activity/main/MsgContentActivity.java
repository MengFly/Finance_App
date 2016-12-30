package com.example.econonew.view.activity.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.econonew.R;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.customview.DefaultWebView;


@SuppressLint("SetJavaScriptEnabled")
public class MsgContentActivity extends BaseActivity {

	private DefaultWebView msgContentWV;
	private String msgContentUrl;//用于显示界面的URL地址
	private String msgContent;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_msg_item_content);

		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		msgContentWV = (DefaultWebView) findViewById(R.id.msg_item_content_view);
		msgContentWV.setProgressBar(progressBar);
	}

	@Override
	protected void initDatas() {
		Intent intent = getIntent();
		msgContentUrl = intent.getStringExtra("msgContentUrl");
		initActionBar(false, intent.getStringExtra("msgTitle"), true);
		msgContent = intent.getStringExtra("msgContext");
		msgContentWV.loadUrl(msgContentUrl, msgContent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.act_msg_content_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh :
				msgContentWV.loadUrl(msgContentUrl, msgContent);
				return true;
			case R.id.menu_open_with_browser :
				if(msgContentUrl != null) {
					openWithBrowser();
				} else {
					showToast("这个页面不支持在浏览器中打开");
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void openWithBrowser() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(msgContentUrl));
		startActivity(intent);
	}

	// 重写返回按键的方法
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && msgContentWV.canGoBack()) {
			msgContentWV.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

