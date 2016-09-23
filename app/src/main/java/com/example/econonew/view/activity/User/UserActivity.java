package com.example.econonew.view.activity.User;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.server.jpush.SettingActivity;
import com.example.econonew.presenter.UserPresenter;
import com.example.econonew.resource.Constant;
import com.example.econonew.utils.URLSettingActivity;


/**
 * UserActivity 此类是用户登陆界面的类
 *
 * @author agnes
 */
public class UserActivity extends BaseUserActivity {

	private TextView userLogout, settingPush, setPwd, voice_repeat;

	// 用户登录的一系列选项
	private LinearLayout userLayout;
	private TextView userTv, userLoginTv, userRigistTv, userSetVipTv;

	private TextView setIpTv;

	private Button regist_btn;
	private TextView text_user;

	private TextView vipTv;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_user);

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

		setIpTv = (TextView) findViewById(R.id.act_user_set_ip_tv);
		if(Constant.isDeBug) {
			setIpTv.setVisibility(View.VISIBLE);
			setIpTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					openOtherActivity(URLSettingActivity.class, false);
				}
			});
		}

		initListener();
		initActionBar("用户", true);
	}

	private void initListener() {
		userTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				expandeUserItem(v);
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
				if(Constant.user == null) {
					openOtherActivity(UserRegistActivity.class, true);
				} else {
					showToast("请先注销后再进行注册");
				}
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
				mPresenter.userLogoutThread(Constant.user);
				text_user.setText("未登录");
				vipTv.setVisibility(View.GONE);
			}

		});
		settingPush.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openOtherActivity(SettingActivity.class, false);
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
			openOtherActivity(UserSetVipActivity.class, true);
		}
	}

	/** 展开或是收起用户的选项 */
	protected void expandeUserItem(View v) {
		if (userLayout.getVisibility() == View.GONE) {
			v.setBackgroundColor(Color.GRAY);
			userLayout.setVisibility(View.VISIBLE);
		} else {
			v.setBackgroundColor(Color.WHITE);
			userLayout.setVisibility(View.GONE);
		}
	}

	@Override
	protected void initDatas() {
		if (Constant.user != null) {
			text_user.setText(Constant.user.getName());
			vipTv.setVisibility(Constant.user.isVIP() ? View.VISIBLE : View.GONE);
		}
		bindPresenter(new UserPresenter(this));
	}

	// 消息重置的按钮点击事件
	private void voiceResetClick() {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				backHomeActivity();
			}
		};
		showTipDialog(null, "重读设置成功", listener, listener);
	}

	// 用户登录按钮的点击事件的处理函数
	private void userLoginClick() {
		if (Constant.user != null) {
			showTipDialog("提示", "您已经登陆，请注销后再重新登录", null, null);
		} else {
			startLoginActivity(false);
		}
	}

	// 点击修改密码的处理事件函数
	private void setPwdClick() {
		if (Constant.user == null) {
			showTipDialog(null, "您当前未登录，请先登录", null, null);
		} else {
			openOtherActivity(UserSetPwdActivity.class, true);
		}
	}
}