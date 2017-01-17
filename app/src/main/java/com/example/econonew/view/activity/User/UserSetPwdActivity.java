package com.example.econonew.view.activity.User;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

import com.example.econonew.R;
import com.example.econonew.databinding.ActUserSetpwdBinding;
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

	private ActUserSetpwdBinding mBinding;

	private UserSetPassFragment setPassFrag;

	@Override
	protected void initView(Bundle savedInstanceState) {
		mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_user_setpwd);
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
		manager.beginTransaction().hide(setPassFrag).commit();
	}

	//显示修改密码的Fragment
	private void showFrag() {
		android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction().show(setPassFrag).commit();
	}

	private void initListener() {
		mBinding.userSetpwdSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setPwdClick();
			}
		});
		mBinding.actSetPassRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.act_set_pass_use_quest_rb) {
					mBinding.actSetPassUsePassLy.setVisibility(View.GONE);
					mBinding.actSetPassUseQuestLy.setVisibility(View.VISIBLE);
				} else if (checkedId == R.id.act_set_pwd_use_pass_rb) {
					mBinding.actSetPassUsePassLy.setVisibility(View.VISIBLE);
					mBinding.actSetPassUseQuestLy.setVisibility(View.GONE);
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
		if(mBinding.actSetPassUsePassLy.getVisibility() == View.VISIBLE) {
			String oldPwd = mBinding.actSetPasswordOldPwdEt.getText().toString();
			if(Constant.user != null && EncodeStrTool.getInstance().getEncodeMD5Str(oldPwd).equals(Constant.user.getPwd())) {
				showToast("密码正确，请设置密码");
				mBinding.actSetPassUsePassLy.setVisibility(View.GONE);
				mBinding.userSetpwdSure.setVisibility(View.GONE);
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
