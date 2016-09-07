package com.example.econonew.activity;


import android.app.ProgressDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.econonew.R;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.UserInfo;

import cn.jpush.android.api.JPushInterface;


/**
 * 继承此类的Activity甚至不需要重写OnCreate方法，只需重写initVariables， initView和loadData方法即可
 *
 * @author mengfei
 *
 */
public abstract class BaseActivity extends AppCompatActivity {

	private AlertDialog.Builder builder;
	private ProgressDialog mProDialog;

	/** Activity的对象。这样就不需要再使用XXActivity.this了 */
	protected BaseActivity mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initView(savedInstanceState);
		initDatas();
		FinanceApplication.getInstance().getmActManager().add(this);
	}

	/**
	 * 初始化界面的控件所需要的方法，所有继承BaseActivity的类都要实现这个方法 另外setContentView方法也要在这个方法里面声明
	 *
	 * @param savedInstanceState
	 */
	abstract protected void initView(Bundle savedInstanceState);

	/**
	 * 调用MobileApi获取数据
	 */
	abstract protected void initDatas();

	/**
	 * 展示一个Dialog给用户提示这几个参数均可为空
	 */
	public void showTipDialog(String title, String message, OnClickListener okListener,
							  OnClickListener cancelListener) {
		if (null == builder) {
			builder = new Builder(this);
			builder.setCancelable(true);
			builder.setIcon(R.mipmap.icon_app);
		}
		builder.setTitle(title == null ? getResources().getString(R.string.app_name) : title);
		if (message != null) {
			builder.setMessage(message);
		}
		builder.setPositiveButton("确定", okListener);
		if (okListener != null && okListener != cancelListener) {
			builder.setNegativeButton("取消", cancelListener);
		}
		if (!this.isFinishing()) {
			builder.create().show();
		}
	}

	/** 显示一个等待提示的对话框 */
	public void showProDialog() {
		showProDialog("请稍后...");
	}

	/** 显示一个带提示文字的对话框 */
	public void showProDialog(String showContent) {
		if (mProDialog == null) {
			mProDialog = ProgressDialog.show(this, null, showContent);
			mProDialog.setCancelable(true);
		} else {
			mProDialog.setMessage(showContent);
			mProDialog.show();
		}
	}

	/** 取消等待提示的对话框 */
	public void hintProDialog() {
		if (mProDialog != null && mProDialog.isShowing()) {
			mProDialog.dismiss();
		}
	}

	protected void initActionBar(String title, boolean isBack) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title);
			if (isBack) {
				actionBar.setHomeButtonEnabled(true);
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		}
	}

	// 向缓存中设置user信息
	// 用户登录成功后调用这个方法
	protected void saveUserInfo(UserInfo userInfo) {
		SharedPreferences pref = getSharedPreferences(Constant.SPF_KEY_USER, MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		editor.putString("userName", userInfo.getName());
		editor.putString("userPassWord", userInfo.getPwd());
		editor.putBoolean("user_vip", userInfo.isVIP());
		editor.apply();
	}

	// 从缓存中获取用户信息，如果用户之前注销过，则缓存中没有用户信息
	// 此时获取信息信息为空
	protected UserInfo getUserInfo() {
		SharedPreferences pref = getSharedPreferences(Constant.SPF_KEY_USER, MODE_PRIVATE);
		String userName = pref.getString("userName", null);
		String userPassWord = pref.getString("userPassWord", null);
		if (userName != null && userPassWord != null) {
			UserInfo user = new UserInfo(userName, userPassWord);
			user.setVIP(pref.getBoolean("user_vip", false));
			return user;
		}
		return null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				backHomeActivity();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	public void showToast(String toastStr) {
		Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 用于在用户注销的时候移除缓存中的用户信息
	 */
	public void removeUserAndCookie() {
		Constant.user = null;
		SharedPreferences pref = getSharedPreferences(Constant.SPF_KEY_USER, MODE_PRIVATE);
		SharedPreferences.Editor userEdit = pref.edit();
		SharedPreferences cookiePref = getSharedPreferences("set-cookie", MODE_PRIVATE);
		SharedPreferences.Editor cookieEdit = cookiePref.edit();
		cookieEdit.clear();
		cookieEdit.apply();
		userEdit.clear();
		userEdit.apply();
	}

	/** 回到主界面 */
	protected void backHomeActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		FinanceApplication.getInstance().getmActManager().remove(this);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
}