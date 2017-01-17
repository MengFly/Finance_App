package com.example.econonew.view.activity.User;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.example.econonew.R;
import com.example.econonew.databinding.ActUserBinding;
import com.example.econonew.presenter.UserPresenter;
import com.example.econonew.resource.Constant;
import com.example.econonew.utils.URLSettingActivity;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


/**
 * UserActivity 此类是用户登陆界面的类
 *
 * @author agnes
 */
public class UserActivity extends BaseUserActivity<UserPresenter> {
    private ActUserBinding mBinding;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_user);
        OverScrollDecoratorHelper.setUpOverScroll(mBinding.actUserSelectSv);
        if (Constant.user != null && Constant.user.isVIP()) {
            mBinding.actUserIsVipVs.getViewStub().inflate();
        }
        if (Constant.isDeBug) {
            mBinding.actUserSetIpTv.setVisibility(View.VISIBLE);
            mBinding.actUserSetIpTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOtherActivity(URLSettingActivity.class, false);
                }
            });
        }
        initActionBar(true, "用户", true);
    }

    @Override
    protected void initDatas() {
        if (Constant.user != null) {
            mBinding.userName.setText(Constant.user.getName());
        }
        bindPresenter(new UserPresenter(this));
        mBinding.setPresenter(mPresenter);
    }


    public void clearUser() {
        mBinding.userName.setText("未登录");
        removeUserAndCookie();
    }
}