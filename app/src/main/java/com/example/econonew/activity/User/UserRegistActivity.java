package com.example.econonew.activity.User;

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
import com.example.econonew.activity.BaseActivity;
import com.example.econonew.resource.Constant;
import com.example.econonew.server.JsonCast;
import com.example.econonew.server.NetClient;
import com.example.econonew.tools.EncodeStrTool;
import com.example.econonew.tools.URLManager;

import org.json.JSONObject;

import java.util.regex.Pattern;


/**
 * UserRegistActivity 此类是管理用户注册的类
 *
 * @author Agnes
 */
public class UserRegistActivity extends BaseActivity {

    private EditText user_regist_name;
    private EditText user_regist_password1;
    private EditText user_regist_password2;
    private Button user_regist_sure;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_user_regist);
        initActionBar("用户注册", true);
        user_regist_name = (EditText) findViewById(R.id.act_regist_user_name_et);
        user_regist_password1 = (EditText) findViewById(R.id.act_regist_user_password_et);
        user_regist_password2 = (EditText) findViewById(R.id.act_regist_user_password2_et);
        user_regist_sure = (Button) findViewById(R.id.act_regist_sure_btn);

        user_regist_sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                userRegistBtnClick();
            }
        });

    }

    @Override
    protected void initDatas() {
    }

    // 用户注册按钮的点击事件处理逻辑
    private void userRegistBtnClick() {
        String regist_username = user_regist_name.getText().toString();
        String regist_password = user_regist_password1.getText().toString();
        String regist_passWord2 = user_regist_password2.getText().toString();
        if (isFormat(regist_username, regist_password, regist_passWord2)) {
            registThread(regist_username, regist_password, regist_passWord2);// 开启注册线程
        }
    }

    /**
     * 开启一个注册的线程
     */
    private void registThread(final String registName, final String password,
                              String password2) {
        showProDialog();
        String encodePass = EncodeStrTool.getInstance().getEncodeMD5Str(password);
        final String url = URLManager.getRegistURL(registName, encodePass);
        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(mContext, UserLoginActivity.class);
                intent.putExtra("isStartLogin", false);
                startActivity(intent);
                finish();
            }
        };
        final NetClient.OnResultListener resultListener = new NetClient.OnResultListener() {

            @Override
            public void onSuccess(String response) {
                JSONObject obj = JsonCast.getJsonObject(response);
                if (obj != null) {
                    String status = JsonCast.getString(obj, "status");
                    if ("success".equals(status)) {
                        showTipDialog(null, "注册成功", listener, null);
                    } else {
                        showTipDialog(null, JsonCast.getString(obj, "result"),
                                null, null);
                    }
                } else {
                    showToast("注册失败");
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
                NetClient.getInstance()
                        .excuteGetForString(mContext, url, resultListener);
            }

            ;
        }.start();

    }

    // 判断用户名和密码是否符合
    private boolean isFormat(String userName, String passWord, String passWord2) {
        // 如果为空
        if (TextUtils.isEmpty(userName)) {
            showToast("请输入用户名");
            return false;
        }
        if (!Pattern.matches("1\\d{10}", userName)) {
            showToast("手机号码格式不正确");
            return false;
        }
        // 输入密码长度不对（应该在6~10个数字之间）
        else if (passWord.length() > 10 || passWord.length() < 6) {
            showToast("密码长度不对");
            return false;
        }
        // 用户两次输入的密码不一致
        else if (!passWord.equals(passWord2)) {
            showToast("两次输入密码不一致");
            return false;
        } else {
            return true;
        }
    }

}
