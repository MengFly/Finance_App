package com.example.econonew.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.resource.Constant;
import com.example.econonew.server.JsonCast;
import com.example.econonew.server.NetClient;

import org.json.JSONObject;

public class UserSetVipActivity extends BaseActivity {

	private EditText userNameEt;
	private TextView setUserNameTv;
	private Button vipRegistBtn;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_user_setvip);
		userNameEt = (EditText) findViewById(R.id.act_set_vip_user_name);
		userNameEt.setFocusable(false);
		setUserNameTv = (TextView) findViewById(R.id.act_set_vip_user_name_tv);
		vipRegistBtn = (Button) findViewById(R.id.act_set_vip_regist_btn);
		initActionBar("注册VIP", true);
		initListener();
	}

	@Override
	protected void initDatas() {
		userNameEt
				.setText(Constant.user == null ? "" : Constant.user.getName());
	}

	private void initListener() {
		vipRegistBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String userName = userNameEt.getText().toString();
				if (TextUtils.isEmpty(userName)) {
					showToast("用户名为空");
				} else {
					if (userName.equals(Constant.user == null ? null
							: Constant.user.getName())) {
						registVip(userName);
					} else {
						showTipDialog("提示", "输入的用户名和当前用户名不一致，请确认，是否要为"
										+ userName + "开启会员？",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
														int which) {
										registVip(userName);
									}
								}, null);
					}
				}
			}

		});

		setUserNameTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final EditText et = new EditText(UserSetVipActivity.this);
				AlertDialog.Builder builder = new Builder(mContext);
				builder.setView(et);
				builder.setTitle("输入用户名");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								if (TextUtils.isEmpty(et.getText().toString())) {
									showToast("输入内容为空");
								} else {
									userNameEt.setText(et.getText().toString());
								}
							}
						});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});

	}

	private void registVip(final String userName) {
		showProDialog();
		new Thread() {
			public void run() {
				String url = Constant.URL + "/" + Constant.OPERATION_SET_VIP
						+ "?phone=" + userName;
				NetClient.OnResultListener listener = new NetClient.OnResultListener() {

					@Override
					public void onSuccess(String response) {
						JSONObject obj = JsonCast.getJsonObject(response);
						if (obj == null) {
							showToast("链接失败");
						} else {
							String result = JsonCast.getString(obj, "result");
							if ("success".equals(result)) {
								showTipDialog(null, "Vip注册成功", null, null);
								Constant.user.setVIP(true);
								saveUserInfo(Constant.user);
							} else {
								showTipDialog(null, result, null, null);
							}
						}
						hintProDialog();
					}

					public void onError(com.android.volley.VolleyError error) {
						super.onError(error);
						hintProDialog();
					};
				};
				NetClient.getInstance().excuteGetForString(mContext, url, listener);
			};
		}.start();
	}
}
