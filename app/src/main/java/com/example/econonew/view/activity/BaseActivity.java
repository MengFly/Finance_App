package com.example.econonew.view.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.econonew.R;
import com.example.econonew.entity.UserEntity;
import com.example.econonew.presenter.BasePresenter;
import com.example.econonew.resource.Constant;
import com.example.econonew.view.activity.User.UserLoginActivity;
import com.example.econonew.view.activity.main.MainActivity;

import cn.jpush.android.api.JPushInterface;


/**
 * 继承此类的Activity甚至不需要重写OnCreate方法，只需重写initVariables， initView和loadData方法即可
 *
 * @author mengfei
 *
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

	protected  T mPresenter;

	private AlertDialog.Builder builder;
	private ProgressDialog mProDialog;

	/** Activity的对象。这样就不需要再使用XXActivity.this了 */
	protected BaseActivity mContext;

	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initView(savedInstanceState);
		initDatas();


		FinanceApplication.getInstance().getActManager().add(this);
	}


	protected void bindPresenter(T presenter) {
		this.mPresenter = presenter;
	}

	/**
	 * 初始化界面的控件所需要的方法，所有继承BaseActivity的类都要实现这个方法 另外setContentView方法也要在这个方法里面声明
	 */
	abstract protected void initView(Bundle savedInstanceState);

	/**
	 * 调用MobileApi获取数据
	 */
	protected void initDatas(){
		hideKeyboard();
	}

	/**
	 * 展示一个Dialog给用户提示这几个参数均可为空
	 */
	public void showTipDialog(String title, String message, OnClickListener okListener, OnClickListener cancelListener) {
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
		if(!this.isFinishing()) {
			if (mProDialog == null) {
				mProDialog = ProgressDialog.show(this, null, showContent);
				mProDialog.setCancelable(true);
			} else {
				mProDialog.setMessage(showContent);
				mProDialog.show();
			}
		}
	}

	/** 取消等待提示的对话框 */
	public void hintProDialog() {
		if (mProDialog != null && mProDialog.isShowing()) {
			mProDialog.dismiss();
		}
	}

	/**
	 * 初始化标题栏
	 * @param title	ActionBar的标题
	 * @param isBack	是否可以返回到MainActivity
     */
	protected void initActionBar(boolean isHide,String title, boolean isBack) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			if(isHide) {//如果设置了隐藏标题栏就隐藏
				actionBar.hide();
			} else {
				actionBar.setTitle(title);
				if (isBack) {
					actionBar.setHomeButtonEnabled(true);
					actionBar.setDisplayHomeAsUpEnabled(true);
				}
			}
		}
	}


	// 从缓存中获取用户信息，如果用户之前注销过，则缓存中没有用户信息
	// 此时获取信息信息为空
	protected UserEntity getUserInfo() {
		SharedPreferences pref = getSharedPreferences(Constant.SPF_KEY_USER, MODE_PRIVATE);
		String userName = pref.getString("userName", null);
		String userPassWord = pref.getString("userPassWord", null);
		if (userName != null && userPassWord != null) {
			UserEntity user = new UserEntity(userName, userPassWord);
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
		if(mToast == null) {
			mToast = Toast.makeText(this, toastStr, Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	/**
	 * 打开另一个Activity
	 * @param actClass	另一个Activity
	 * @param isFinish	打开另一个Activity后此Activity是否退出
     */
	public void openOtherActivity(Class<?> actClass, boolean isFinish) {
		Intent intent = new Intent(mContext, actClass);
		mContext.startActivity(intent);
		if(isFinish) {
			mContext.finish();
		}
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
	public void backHomeActivity() {
		Intent intent = new Intent(mContext, MainActivity.class);
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
		FinanceApplication.getInstance().getActManager().remove(this);
		if(mPresenter != null) {
			mPresenter.onDestroy();
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}

	/**
	 * 当用户没有登录的时候弹出的提示框
	 */
	public void showNoLoginDialog() {
		showTipDialog(null, "你还没有登陆,是否登录", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {openOtherActivity(UserLoginActivity.class, false);}
		},null);
	}

	/**
	 * 隐藏键盘
	 */
	protected void hideKeyboard() {
		View view = getCurrentFocus();
		if (view != null) {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
					hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}