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

	private DefaultWebView msgContent;
	private String msgContentUrl;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_msg_item_content);

		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		msgContent = (DefaultWebView) findViewById(R.id.msg_item_content_view);
		msgContent.setProgressBar(progressBar);
	}

	@Override
	protected void initDatas() {
		Intent intent = getIntent();
		msgContentUrl = intent.getStringExtra("msgContentUrl");
		initActionBar(false, intent.getStringExtra("msgTitle"), true);
		msgContent.loadUrl(msgContentUrl);
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
				msgContent.loadUrl(msgContentUrl);
				return true;
			case R.id.menu_open_with_browser :
				openWithBrowser();
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
		if (keyCode == KeyEvent.KEYCODE_BACK && msgContent.canGoBack()) {
			msgContent.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

