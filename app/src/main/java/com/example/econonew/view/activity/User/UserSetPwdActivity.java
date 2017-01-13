package com.example.econonew.view.activity.User;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.econonew.R;
import com.example.econonew.presenter.BaseUserPresenter;
import com.example.econonew.resource.Constant;
import com.example.econonew.tools.EncodeStrTool;
import com.example.econonew.view.fragment.UserSetPassFragment;

/**
 * UserRegistActivity
 *
 * @author Agnes 此类是管理用户修改密码的类
 */
public class UserSetPwdActivity extends BaseUserActivity<BaseUserPresenter<UserActivity>> {

	private LinearLayout setPassUsePassLy, setPassUseQuestLy;

	private EditText oldPwdEt;

	private RadioGroup setPassRg;

	private Button setPwdSureBtn;

	private UserSetPassFragment setPassFrag;


	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_user_setpwd);

		setPassUsePassLy = (LinearLayout) findViewById(R.id.act_set_pass_use_pass_ly);
		setPassUseQuestLy = (LinearLayout)findViewById(R.id.act_set_pass_use_quest_ly);

		oldPwdEt = (EditText) findViewById(R.id.act_set_password_old_pwd_et);
		setPwdSureBtn = (Button) findViewById(R.id.user_setpwd_sure);
		setPassRg = (RadioGroup) findViewById(R.id.act_set_pass_rg);
		initListener();
		initFragment();
		hideFrag();
	}

	//初始化修改密码的Fragment
	private void initFragment() {
		android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transa = manager.beginTransaction();
		setPassFrag = new UserSetPassFragment();
		transa.replace(R.id.act_set_pass_fl, setPassFrag);
		transa.commit();
	}

	//隐藏修改密码的Fragment
	private void hideFrag() {
		android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transa = manager.beginTransaction();
		transa.hide(setPassFrag);
		transa.commit();
	}

	//显示修改密码的Fragment
	private void showFrag() {
		android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transa = manager.beginTransaction();
		transa.show(setPassFrag);
		transa.commit();
	}

	//显示用密码修改密码的布局
	private void showUsePassLy() {
		setPassUsePassLy.setVisibility(View.VISIBLE);
		setPassUseQuestLy.setVisibility(View.GONE);
		setPwdSureBtn.setVisibility(View.VISIBLE);
		hideFrag();
	}

	//显示用密保修改密码布局
	private void showUseQuestLy() {
		setPassUsePassLy.setVisibility(View.GONE);
		setPassUseQuestLy.setVisibility(View.VISIBLE);
		setPwdSureBtn.setVisibility(View.VISIBLE);
		hideFrag();
	}

	private void initListener() {
		setPwdSureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setPwdClick();
			}
		});
		setPassRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.act_set_pass_use_quest_rb) {
					showUseQuestLy();
				} else if (checkedId == R.id.act_set_pwd_use_pass_rb) {
					showUsePassLy();
				}
			}
		});
	}



	@Override
	protected void initDatas() {
		initActionBar(false, "修改密码",true);
		bindPresenter(new BaseUserPresenter(this));
	}

	// 修改按钮的按钮点击事件的处理函数
	private void setPwdClick() {
		if(setPassUsePassLy.getVisibility() == View.VISIBLE) {
			String oldPwd = oldPwdEt.getText().toString();
			if(Constant.user != null && EncodeStrTool.getInstance().getEncodeMD5Str(oldPwd).equals(Constant.user.getPwd())) {
				showToast("密码正确，请设置密码");
				setPassUsePassLy.setVisibility(View.GONE);
				setPwdSureBtn.setVisibility(View.GONE);
				showFrag();
			} else if(Constant.user == null) {
				showToast("当前还没有登录，要登录之后才能通过密码进行修改密码");
			} else {
				showToast("密码错误");
			}
		} else {
			// TODO: 2016/10/7 使用密保修改密码的逻辑 
		}

	}


}
