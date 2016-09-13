package com.example.econonew.activity.User;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.econonew.R;
import com.example.econonew.activity.BaseActivity;
import com.example.econonew.activity.FinanceApplication;
import com.example.econonew.activity.main.MainActivity;
import com.example.econonew.jpush.SettingActivity;
import com.example.econonew.main.object.AllMessage;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.UserInfo;
import com.example.econonew.server.JsonCast;
import com.example.econonew.server.NetClient;
import com.example.econonew.tools.URLManager;

import org.json.JSONObject;


/**
 * UserActivity 此类是用户登陆界面的类
 *
 * @author agnes
 */
@SuppressLint("HandlerLeak")
public class UserActivity extends BaseActivity {

	private TextView userLogout, settingPush, setPwd, voice_repeat;

	// 用户登录的一系列选项
	private LinearLayout userLayout;
	private TextView userTv, userLoginTv, userRigistTv, userSetVipTv;

	private Button regist_btn;
	private TextView text_user;

	private TextView vipTv;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_user);

		regist_btn = (Button) findViewById(R.id.user_regist);
		text_user = (TextView) findViewById(R.id.user_name);

		settingPush = (TextView) findViewById(R.id.settingPush);
		userLogout = (TextView) findViewById(R.id.logout);
		voice_repeat = (TextView) findViewById(R.id.repeat_voice);
		setPwd = (TextView) findViewById(R.id.setPwd);

		userTv = (TextView) findViewById(R.id.act_user_user_user_tv);
		userLayout = (LinearLayout) findViewById(R.id.act_user_user_ly);
		userLoginTv = (TextView) findViewById(R.id.act_user_login_tv);
		userRigistTv = (TextView) findViewById(R.id.act_user_regist_tv);
		userSetVipTv = (TextView) findViewById(R.id.act_user_setVip_tv);

		vipTv = (TextView) findViewById(R.id.vip_tv);
		vipTv.setVisibility(View.GONE);

		initListener();
		initActionBar("用户", true);
	}

	private void initListener() {
		userTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				expandeUserItem(v);// 展开或是隐藏用户的选项
			}
		});
		userLoginTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userLoginClick();
			}
		});
		userRigistTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userRigistClick();
			}
		});
		userSetVipTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userSetVipClick();
			}
		});
		userLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userLogoutClick();
			}

		});
		settingPush.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settingPushClick();
			}
		});
		setPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPwdClick();
			}

		});
		voice_repeat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				voiceResetClick();
			}
		});
	}

	private void userSetVipClick() {
		if (Constant.user == null) {
			showTipDialog(null, "当前还未登录，是否登录？",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							userLoginClick();
						}

					}, null);
		} else {
			Intent intent = new Intent(this, UserSetVipActivity.class);
			startActivity(intent);
			finish();
		}
	}

	/** 用户注册的按钮的点击事件 */
	protected void userRigistClick() {
		Intent intent = new Intent(this, UserRegistActivity.class);
		startActivity(intent);
		finish();
	}

	/** 展开或是收起用户的选项 */
	protected void expandeUserItem(View v) {
		if (userLayout.getVisibility() == View.GONE) {
			v.setBackgroundColor(Color.GRAY);
			userLayout.setVisibility(View.VISIBLE);
			if (Constant.user != null) {
				userRigistTv.setClickable(false);
				userLoginTv.setClickable(false);
			} else {
				userRigistTv.setClickable(true);
				userLoginTv.setClickable(true);
			}
		} else {
			v.setBackgroundColor(Color.WHITE);
			userLayout.setVisibility(View.GONE);
		}
	}

	@Override
	protected void initDatas() {
		if (Constant.user != null) {
			text_user.setText(Constant.user.getName());
			vipTv.setVisibility(Constant.user.isVIP() ? View.VISIBLE
					: View.GONE);
		}

	}

	// 消息重置的按钮点击事件
	private void voiceResetClick() {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(UserActivity.this, MainActivity.class);
				startActivity(intent);
			}
		};
		showTipDialog(null, "重读设置成功", listener, listener);
	}

	// 设置推送时间的按钮
	private void settingPushClick() {
		Intent intent = new Intent();
		intent.setClass(UserActivity.this, SettingActivity.class);
		startActivity(intent);
	}

	// 用户登录按钮的点击事件的处理函数
	private void userLoginClick() {
		if (Constant.user != null) {
			showTipDialog("提示", "您已经登陆，请注销后再重新登", null, null);
		} else {
			startLoginActivity(false);
		}
	}

	// 用户点击注销按钮的处理函数
	private void userLogoutClick() {
		if (Constant.user != null) {
			showProDialog();
			userRigistTv.setClickable(true);
			userLoginTv.setClickable(true);
			userLogoutThread(Constant.user);// 进入注销的线程
		} else {
			DialogInterface.OnClickListener loginListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					startLoginActivity(false);
				}
			};
			showTipDialog(null, "当前未登录，是否现在登陆？", loginListener, null);
		}
	}

	/** 开启一个用户注销的线程 启动线程链接服务器根据用户名以及电话号码进行注销操作 */
	private void userLogoutThread(UserInfo user) {
		final String url = URLManager.getLogoutURL(user.getName());
		final NetClient.OnResultListener requestListener = new NetClient.OnResultListener() {// 注销成功后的回调事件

			@Override
			public void onSuccess(String response) {
				JSONObject responseObject = JsonCast.getJsonObject(response);
				if (responseObject != null) {
					String result = JsonCast
							.getString(responseObject, "result");
					if (result != null) {
						if ("success".equals(result)) {
							showTipDialog(null, "注销成功", null, null);
							text_user.setText("未登录");
							removeUserAndCookie();
							FinanceApplication.getInstance().refreshUserData(
									Constant.user);
							AllMessage.refreshPublicMsg();
							regist_btn
									.setBackgroundResource(R.drawable.user_login_btn);
						} else {
							removeUserAndCookie();
							FinanceApplication.getInstance().refreshUserData(
									Constant.user);
							showTipDialog(null, result, null, null);
						}
					} else {
						showToast("注销失败");
					}
				}
				hintProDialog();
			}

			@Override
			public void onError(VolleyError error) {
				super.onError(error);
				hintProDialog();
			}
		};
		new Thread() {
			public void run() {
				NetClient.getInstance().excuteGetForString(UserActivity.this, url,
						requestListener);
			}
		}.start();
	}

	// 点击修改密码的处理事件函数
	private void setPwdClick() {
		if (Constant.user == null) {
			showTipDialog(null, "您当前未登录，请先登录", null, null);
		} else {
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(UserActivity.this, UserSetPwdActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	/**
	 * 进入登录界面
	 *
	 * @param isStartLogin
	 *            是不是应用刚开始要进入的登录界面（因为应用开启的时候进入的登陆界面有跳过登陆按钮）
	 */
	private void startLoginActivity(boolean isStartLogin) {
		Intent intent = new Intent(UserActivity.this, UserLoginActivity.class);
		intent.putExtra("isStartLogin", isStartLogin);
		startActivity(intent);
	}
}