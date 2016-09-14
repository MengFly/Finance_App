package com.example.econonew.view.activity.User;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.presenter.UserPresenter;

/**
 * UserLoginActivity 此类是用户管理用户登录的类，并且提供用户注册的接口
 *
 * @author agnes
 */
public class UserLoginActivity extends BaseUserActivity {

    private EditText userNameET;
    private EditText userPasswordET;

    private Button registBtn;
    private Button loginSureBtn;
    private TextView noLoginTv;

    // 判断登陆界面是刚开始进入应用的登陆界面还是用户点击UserActivity里面的登陆按钮进入的登陆界面
    // 如果是刚进入应用的界面就自动设置用户名和 密码，并显示跳过登陆按钮
    // 如果是点击UserActivity里面的登陆按钮进入的登陆界面则不设置，并且不显示跳过登陆按钮
    private boolean isStartLogin = true;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_user_login);
        userNameET = (EditText) findViewById(R.id.act_login_user_name_et);
        userPasswordET = (EditText) findViewById(R.id.act_login_password_et);

        loginSureBtn = (Button) findViewById(R.id.act_login_sure_btn);
        registBtn = (Button) findViewById(R.id.act_login_regist_btn);
        noLoginTv = (TextView) findViewById(R.id.act_user_login_not_login_tv);
        noLoginTv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        initListener();
    }

    private void initListener() {
        registBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                openOtherActivity(UserRegistActivity.class, true);
            }
        });
        loginSureBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtnClick();
            }
        });
        noLoginTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                backHomeActivity();
            }
        });

    }

    @Override
    protected void initDatas() {
        isStartLogin = getIntent().getBooleanExtra("isStartLogin", true);
        bindPresenter(new UserPresenter(this));
        initActionBar("登录", !isStartLogin);
    }

    // 登陆按钮的事件的处理逻辑
    private void loginBtnClick() {
        String userNameStr = userNameET.getText().toString();
        String userPasswordStr = userPasswordET.getText().toString();
        mPresenter.userLoginThread(userNameStr, userPasswordStr, isStartLogin);
    }

}
