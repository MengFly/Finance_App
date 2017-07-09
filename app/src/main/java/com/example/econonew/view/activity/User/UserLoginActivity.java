package com.example.econonew.view.activity.user;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.econonew.R;
import com.example.econonew.databinding.ActUserLogin2Binding;
import com.example.econonew.presenter.BaseUserPresenter;


/**
 * UserLoginActivity 此类是用户管理用户登录的类，并且提供用户注册的接口
 *
 * @author agnes
 */
public class UserLoginActivity extends BaseUserActivity<BaseUserPresenter<UserActivity>> {

    private ActUserLogin2Binding mBinding;

    // 判断登陆界面是刚开始进入应用的登陆界面还是用户点击UserActivity里面的登陆按钮进入的登陆界面
    // 如果是刚进入应用的界面就自动设置用户名和 密码，并显示跳过登陆按钮
    // 如果是点击UserActivity里面的登陆按钮进入的登陆界面则不设置，并且不显示跳过登陆按钮
    private boolean isStartLogin = true;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_user_login2);
        initListener();
    }

    private void initListener() {
        mBinding.btnLogin.setOnClickListener(viewClickListener);
        mBinding.btnFogetPassword.setOnClickListener(viewClickListener);
        mBinding.ivBack.setOnClickListener(viewClickListener);
    }

    @Override
    protected void initDatas() {
        isStartLogin = getIntent().getBooleanExtra("isStartLogin", true);
        bindPresenter(new BaseUserPresenter(this));
    }

    private View.OnClickListener viewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    mPresenter.userLoginThread(mBinding.etUsername.getText().toString(),
                            mBinding.etPassword.getText().toString(), isStartLogin);
                    break;
                case R.id.btn_foget_password:
                    openOtherActivity(UserSetPwdActivity.class, true);
                    break;
                case R.id.iv_back:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

}
