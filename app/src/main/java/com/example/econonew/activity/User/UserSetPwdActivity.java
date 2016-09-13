package com.example.econonew.activity.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.econonew.R;
import com.example.econonew.activity.BaseActivity;
import com.example.econonew.resource.Constant;
import com.example.econonew.server.JsonCast;
import com.example.econonew.server.NetClient;
import com.example.econonew.tools.EncodeStrTool;
import com.example.econonew.tools.URLManager;

import org.json.JSONObject;
/**
 * UserRegistActivity
 *
 * @author Agnes 此类是管理用户修改密码的类
 */
public class UserSetPwdActivity extends BaseActivity {

	private EditText oldPwdEt;
	private EditText newPwdEt;
	private EditText newPwd2Et;

	private TextView isFormatPwdTv;

	private Button setpwdSureBtn;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_user_setpwd);
		initActionBar("修改密码",true);
		oldPwdEt = (EditText) findViewById(R.id.act_set_password_old_pwd_et);
		newPwdEt = (EditText) findViewById(R.id.act_set_password_new_pwd_et);
		newPwd2Et = (EditText) findViewById(R.id.act_set_password_new_pwd2_tv);

		isFormatPwdTv = (TextView) findViewById(R.id.act_set_pwd_is_ok_tv);

		setpwdSureBtn = (Button) findViewById(R.id.user_setpwd_sure);

		initListener();
	}

	private void initListener() {
		setpwdSureBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPwdClick();
			}
		});
		newPwdEt.addTextChangedListener(new ContentExchangeListener());
		newPwd2Et.addTextChangedListener(new ContentExchangeListener());
	}

	@Override
	protected void initDatas() {

	}

	// 修改按钮的按钮点击事件的处理函数
	private void setPwdClick() {
		final String old_pwd = oldPwdEt.getText().toString();
		final String new_pwd1 = newPwdEt.getText().toString();
		final String new_pwd2 = newPwd2Et.getText().toString();
		if (isFormat(old_pwd, new_pwd1, new_pwd2)) {
			setPwdThread(old_pwd, new_pwd1, new_pwd2);// 开启一个修改密码的线程
		}
	}

	private void setPwdThread(String old_pwd, String new_pwd1, String new_pwd2) {
		showProDialog();
		String oldEncodePass = EncodeStrTool.getInstance().getEncodeMD5Str(old_pwd);
		String newEncodePass = EncodeStrTool.getInstance().getEncodeMD5Str(new_pwd1);
		final String url = URLManager.getSetPassWordURL(Constant.user.getName(), oldEncodePass, newEncodePass);

		final DialogInterface.OnClickListener setPwdOkListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(mContext, UserLoginActivity.class);
				startActivity(intent);
			}
		};

		final NetClient.OnResultListener listener = new NetClient.OnResultListener() {

			@Override
			public void onSuccess(String response) {
				JSONObject obj = JsonCast.getJsonObject(response);
				if (obj != null) {
					String result = JsonCast.getString(obj, "result");
					if ("success".equals(JsonCast.getString(obj, "status"))) {
						showTipDialog(null, "修改成功，请重新登录", setPwdOkListener,
								setPwdOkListener);
						removeUserAndCookie();//修改密码成功的时候移除用户和之前的Cookie信息
					} else {
						showTipDialog(null, result, null, null);
					}
				} else {
					showToast("密码修改失败");
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
				NetClient.getInstance().excuteGetForString(mContext, url, listener);
			};
		}.start();
	}

	// 判断用户密码书否符合标准
	private boolean isFormat(String oldPassWord, String newPassWord,
							 String newPassWord1) {
		String encodePass = EncodeStrTool.getInstance().getEncodeMD5Str(oldPassWord);
		if (!encodePass.equals(Constant.user.getPwd())) {
			showToast("输入旧密码错误");
			return false;
		} else {
			if (!newPassWord.equals(newPassWord1)) {
				showToast("两次输入新密码不一样");
				return false;
			} else if (newPassWord.length() > 10 || newPassWord.length() < 6) {
				showToast("密码长度必须在6-10之间");
				return false;
			} else {
				return true;
			}
		}
	}

	private class ContentExchangeListener implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			if (newPwdEt.getText().toString() != null) {
				if (newPwd2Et.getText().toString() == null) {
					isFormatPwdTv.setText("请再次输入新密码");
				} else if (newPwdEt.getText().toString()
						.equals(newPwd2Et.getText().toString())) {
					isFormatPwdTv.setText("两次输入的密码一致");
				} else {
					isFormatPwdTv.setText("两次输入密码不一致");
				}
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

	}

}
