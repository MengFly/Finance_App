package com.example.econonew.view.activity.User;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.econonew.R;
import com.example.econonew.presenter.UserPresenter;


/**
 * UserRegistActivity 此类是管理用户注册的类
 *
 * @author Agnes
 */
public class UserRegistActivity extends BaseUserActivity {

    private EditText user_regist_name;
    private EditText user_regist_password1;
    private EditText user_regist_password2;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_user_regist);
        user_regist_name = (EditText) findViewById(R.id.act_regist_user_name_et);
        user_regist_password1 = (EditText) findViewById(R.id.act_regist_user_password_et);
        user_regist_password2 = (EditText) findViewById(R.id.act_regist_user_password2_et);
        Button user_regist_sure = (Button) findViewById(R.id.act_regist_sure_btn);
        user_regist_sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                userRegistBtnClick();
            }
        });

    }

    @Override
    protected void initDatas() {
        initActionBar("用户注册", true);
        bindPresenter(new UserPresenter(this));
    }

    // 用户注册按钮的点击事件处理逻辑
    private void userRegistBtnClick() {
        String regist_username = user_regist_name.getText().toString();
        String regist_password = user_regist_password1.getText().toString();
        String regist_passWord2 = user_regist_password2.getText().toString();
        mPresenter.userRegistThread(regist_username, regist_password, regist_passWord2);
    }

}
