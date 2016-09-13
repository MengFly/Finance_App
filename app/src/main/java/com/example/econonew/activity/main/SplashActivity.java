package com.example.econonew.activity.main;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.econonew.R;
import com.example.econonew.activity.BaseActivity;
import com.example.econonew.activity.User.UserLoginActivity;
import com.example.econonew.jpush.ExampleUtil;
import com.example.econonew.jpush.PushSwitch;
import com.example.econonew.resource.Constant;
import com.example.econonew.server.NetClient;
import com.example.econonew.tools.URLManager;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;



/**
 * SplashActivity 此类是作为一个缓冲界面，加载数据 实现了中间服务器中下载数据
 *
 * @author agnes
 */
public class SplashActivity extends BaseActivity {

	private TextView textTipTv;// 显示提示的文字
	private TextView versionNameTv;//版本信息

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_splash);
		textTipTv = (TextView) findViewById(R.id.act_splash_test_tip_tv);
		versionNameTv = (TextView) findViewById(R.id.act_splash_version);
		versionNameTv.setText(getVersionName());
		ObjectAnimator animator = ObjectAnimator.ofFloat(textTipTv, "alpha", 0.0f, 1.0f);
		animator.setDuration(1000).setStartDelay(500);
		animator.start();
	}

	@Override
	protected void initDatas() {
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		registerMessageReceiver();// used for receive msg
		PushSwitch switchPush = new PushSwitch(getApplicationContext());
		boolean isPush = switchPush.isReceivePush();
		if (!isPush) {
			Toast.makeText(SplashActivity.this, "消息推送关闭，可在配置中打开",
					Toast.LENGTH_LONG).show();
		}
		initTimer();
	}

	//获取版本号信息
	public String getVersionName() {
		return versionNameTv.getText().toString() + " V " + getResources().getString(R.string.versionName);
	}

	/** 设置时间延迟 */
	private void initTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Intent intent = new Intent();
				// 如果用户信息过期则进入登陆界面，如果没有过期则初始化用户并进入主界面
				if (cookieIsGuoqi()) {
					intent.setClass(mContext, UserLoginActivity.class);
				} else {
					Constant.user = getUserInfo();
					String url = URLManager.getLoginURL(Constant.user);
					NetClient.getInstance().excuteGetForString(mContext, url, null);// 模拟登录
					intent.setClass(mContext, MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		}, 2000);
	}

	/** 判断Cookie是否过期 */
	public boolean cookieIsGuoqi() {
		return getUserInfo() == null;// 如果本地存储本地存储的User为空，则Cookie过期
	}

	// for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

}
