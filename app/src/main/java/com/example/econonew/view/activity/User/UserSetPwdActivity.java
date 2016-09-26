package com.example.econonew.view.activity.User;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.presenter.UserPresenter;

/**
 * UserRegistActivity
 *
 * @author Agnes 此类是管理用户修改密码的类
 */
public class UserSetPwdActivity extends BaseUserActivity {

	private EditText oldPwdEt;
	private EditText newPwdEt;
	private EditText newPwd2Et;

	private TextView isFormatPwdTv;

	private Button setpwdSureBtn;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_user_setpwd);

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
		initActionBar("修改密码",true);
		bindPresenter(new UserPresenter(this));
	}

	// 修改按钮的按钮点击事件的处理函数
	private void setPwdClick() {
		final String old_pwd = oldPwdEt.getText().toString();
		final String new_pwd1 = newPwdEt.getText().toString();
		final String new_pwd2 = newPwd2Et.getText().toString();
		mPresenter.userSetPassThread(old_pwd, new_pwd1, new_pwd2);
	}

	private class ContentExchangeListener implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String pass1 = newPwdEt.getText().toString();
			String pass2 = newPwd2Et.getText().toString();
			if (!TextUtils.isEmpty(pass1)) {
				if (TextUtils.isEmpty(pass2)) {
					isFormatPwdTv.setText("请再次输入新密码");
				} else if (pass1.equals(pass2)) {
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
