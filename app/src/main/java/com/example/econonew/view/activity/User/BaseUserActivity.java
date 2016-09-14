package com.example.econonew.view.activity.User;

import android.content.Intent;
import android.content.SharedPreferences;

import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.presenter.UserPresenter;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.UserInfo;

/**
 * 所有用户的Activity的父类
 * Created by mengfei on 2016/9/13.
 */
public abstract class BaseUserActivity extends BaseActivity<UserPresenter>{


    // 向缓存中设置user信息
    // 用户登录成功后调用这个方法
    public void saveUserInfo(UserInfo userInfo) {
        SharedPreferences pref = getSharedPreferences(Constant.SPF_KEY_USER, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userName", userInfo.getName());
        editor.putString("userPassWord", userInfo.getPwd());
        editor.putBoolean("user_vip", userInfo.isVIP());
        editor.apply();
    }

    /**
     * 进入登录界面
     *
     * @param isStartLogin
     *            是不是应用刚开始要进入的登录界面（因为应用开启的时候进入的登陆界面有跳过登陆按钮）
     */
    public void startLoginActivity(boolean isStartLogin) {
        Intent intent = new Intent(mContext, UserLoginActivity.class);
        intent.putExtra("isStartLogin", isStartLogin);
        startActivity(intent);
    }

}
