package com.example.econonew.view.activity.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.econonew.R;
import com.example.econonew.databinding.ActMsgItemContentBinding;
import com.example.econonew.view.activity.BaseActivity;

public class MsgContentActivity extends BaseActivity {

	private ActMsgItemContentBinding mBinding;
	private String msgContentUrl;//用于显示界面的URL地址
	private String msgContent;

	@Override
	protected void initView(Bundle savedInstanceState) {
		mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_msg_item_content);
		mBinding.msgItemContentView.setProgressBar(mBinding.progressBar1);
	}

	@Override
	protected void initDatas() {
		Intent intent = getIntent();
		msgContentUrl = intent.getStringExtra("msgContentUrl");
		initActionBar(false, intent.getStringExtra("msgTitle"), true);
		msgContent = intent.getStringExtra("msgContext");
		mBinding.msgItemContentView.loadUrl(msgContentUrl, msgContent);
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
				mBinding.msgItemContentView.loadUrl(msgContentUrl, msgContent);
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
		if (keyCode == KeyEvent.KEYCODE_BACK && mBinding.msgItemContentView.canGoBack()) {
			mBinding.msgItemContentView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

