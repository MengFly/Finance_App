package com.example.econonew.view.activity.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.example.econonew.R;
import com.example.econonew.databinding.ActRegistOrLoginBinding;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.user.UserLoginActivity;
import com.example.econonew.view.activity.user.UserRegistActivity;

/**
 * 注册或是登陆的界面
 * Created by mengfei on 2017/6/18.
 */
public class RigestOrLoginActivity extends BaseActivity {

    private ActRegistOrLoginBinding mBinding;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_regist_or_login);
        initListener();
    }

    private void initListener() {
        mBinding.btnRegist.setOnClickListener(viewListener);
        mBinding.tvLogin.setOnClickListener(viewListener);
        mBinding.tvNoLogin.setOnClickListener(viewListener);
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_regist:
                    openOtherActivity(UserRegistActivity.class, false);
                    break;
                case R.id.tv_login:
                    openOtherActivity(UserLoginActivity.class, false);
                    break;
                case R.id.tv_no_login:
                    openOtherActivity(MainActivity.class, true);
                default:
                    break;

            }
        }
    };
}
