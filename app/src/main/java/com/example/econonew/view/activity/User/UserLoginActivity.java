package com.example.econonew.view.activity.User;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.example.econonew.R;
import com.example.econonew.databinding.ActUserLoginBinding;
import com.example.econonew.presenter.BaseUserPresenter;


/**
 * UserLoginActivity 此类是用户管理用户登录的类，并且提供用户注册的接口
 *
 * @author agnes
 */
public class UserLoginActivity extends BaseUserActivity<BaseUserPresenter<UserActivity>> {

    private ActUserLoginBinding mBinding;

    // 判断登陆界面是刚开始进入应用的登陆界面还是用户点击UserActivity里面的登陆按钮进入的登陆界面
    // 如果是刚进入应用的界面就自动设置用户名和 密码，并显示跳过登陆按钮
    // 如果是点击UserActivity里面的登陆按钮进入的登陆界面则不设置，并且不显示跳过登陆按钮
    private boolean isStartLogin = true;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding =  DataBindingUtil.setContentView(mContext,R.layout.act_user_login);
        initListener();
    }

    private void initListener() {
        mBinding.actLoginRegistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openOtherActivity(UserRegistActivity.class, true);
            }
        });
        mBinding.actLoginSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtnClick();
            }
        });
        mBinding.loginRegistTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtnClick();
            }
        });
        mBinding.actUserLoginNotLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHomeActivity();
            }
        });
        mBinding.actLoginForgetPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openOtherActivity(UserSetPwdActivity.class, true);}
        });
    }

    @Override
    protected void initDatas() {
        isStartLogin = getIntent().getBooleanExtra("isStartLogin", true);
        bindPresenter(new BaseUserPresenter(this));
        initActionBar(true , "登录", !isStartLogin);
    }

    // 登陆按钮的事件的处理逻辑
    private void loginBtnClick() {
        String userNameStr = mBinding.actLoginUserNameEt.getText().toString();
        String userPasswordStr = mBinding.actLoginPasswordEt.getText().toString();
        mPresenter.userLoginThread(userNameStr, userPasswordStr, isStartLogin);
    }

}
