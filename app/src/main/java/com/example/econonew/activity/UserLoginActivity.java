package com.example.econonew.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.example.econonew.R;
import com.example.econonew.main.object.AllMessage;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.UserInfo;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.UserJsonHelper;
import com.example.econonew.tools.EncodeStrTool;

/**
 * UserLoginActivity 此类是用户管理用户登录的类，并且提供用户注册的接口
 *
 * @author agnes
 */
public class UserLoginActivity extends BaseActivity {

    private EditText userNameET;
    private EditText userPasswordET;

    private Button registBtn;
    private Button loginSureBtn;
    private Button noLoginBtn;

    private String userNameStr = "";
    private String userPasswordStr = "";

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
        noLoginBtn = (Button) findViewById(R.id.act_login_no_login_btn);

        initListener();
    }

    private void initListener() {
        registBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                registBtnClick();
            }
        });

        loginSureBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtnClick();
            }
        });

        noLoginBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                noLoginBtnClick();
            }
        });

    }

    @Override
    protected void initDatas() {
        isStartLogin = getIntent().getBooleanExtra("isStartLogin", true);
        if (isStartLogin) {
            showToast("用户信息过期，请重新登录");
        } else {
            noLoginBtn.setVisibility(View.GONE);
        }
        initActionBar("登录", !isStartLogin);

    }

    // 注册按钮的点击事件函数
    private void registBtnClick() {
        Intent intent = new Intent(mContext, UserRegistActivity.class);
        startActivity(intent);
        this.finish();
    }

    // 登陆按钮的事件的处理逻辑
    private void loginBtnClick() {
        // 用户输入正确，把信息放入对应的变量中
        userNameStr = userNameET.getText().toString();
        userPasswordStr = userPasswordET.getText().toString();
        if (isFormat(userNameStr, userPasswordStr)) {// 如果用户名和密码符合规范就进入登录线程
            showProDialog();// 进入线程之前显示提示框
            loginThread(userNameStr, userPasswordStr);
        }
    }

    /**
     * 用户登录的线程
     */
    private void loginThread(final String userNameStr, final String userPasswordStr) {
        final String encodePass = EncodeStrTool.getInstance().getEncodeMD5Str(userPasswordStr);
        final String url = Constant.URL + "/" + Constant.OPERATION_LOGIN
                + "?phone=" + userNameStr + "&password=" + encodePass;
        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                backHomeActivity();
            }
        };
        final NetClient.OnResultListener responseListener = new NetClient.OnResultListener() {

            @Override
            public void onSuccess(String response) {
                UserJsonHelper helper = new UserJsonHelper(mContext);
                UserInfo user = helper.excuteJsonForItem(response);// 通过Json数据获取User
                if (user != null) {
                    showTipDialog(null, "登录成功", listener, null);
                    Constant.user = new UserInfo(userNameStr, encodePass);
                    Constant.user.setVIP(user.isVIP());
                    if (!isStartLogin) {
                        FinanceApplication.getInstance().refreshUserData(
                                Constant.user);
                        AllMessage.refreshPublicMsg();
                    }
                    saveUserInfo(Constant.user);
                } else {
                    showToast("登录失败");
                }
                hintProDialog();
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                hintProDialog();
            }
        };
        new Thread() {
            public void run() {
                NetClient.getInstance().excuteGetForString(mContext, url,
                        responseListener);
            }

            ;
        }.start();

    }

    // 判断用户的输入是否符合规范
    private boolean isFormat(String userName, String passWord) {
        // 没有输入用户名
        if (TextUtils.isEmpty(userName)) {
            showToast("请输入用户名");
            return false;
        }
        // 用户输入的登录密码长度不对（应该在6~10个数字之间）
        else if (passWord.length() > 10 || passWord.length() < 6) {
            showToast("密码长度不对");
            return false;
        }
        return true;
    }

    // 跳过登陆的按钮的点击事件函数
    private void noLoginBtnClick() {
        backHomeActivity();
    }

}
