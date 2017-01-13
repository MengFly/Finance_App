package com.example.econonew.view.activity.User;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.presenter.BaseUserPresenter;
import com.example.econonew.resource.Constant;

public class UserSetVipActivity extends BaseUserActivity<BaseUserPresenter<UserActivity>> {

    private EditText userNameEt;
    private TextView setUserNameTv;
    private Button vipRegistBtn;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_user_setvip);
        userNameEt = (EditText) findViewById(R.id.act_set_vip_user_name);
        userNameEt.setFocusable(false);
        setUserNameTv = (TextView) findViewById(R.id.act_set_vip_user_name_tv);
        vipRegistBtn = (Button) findViewById(R.id.act_set_vip_regist_btn);
        initListener();
    }

    @Override
    protected void initDatas() {
        initActionBar(false, "注册VIP", true);
        bindPresenter(new BaseUserPresenter(this));
        userNameEt.setText(Constant.user == null ? "" : Constant.user.getName());
    }

    private void initListener() {
        vipRegistBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String userName = userNameEt.getText().toString();
                        if (TextUtils.isEmpty(userName)) {
                            showToast("用户名为空");
                        } else {
                            if (userName.equals(Constant.user == null ? null : Constant.user.getName())) {
                                mPresenter.userSetVipThread(userName);
                            } else {
                                showTipDialog("提示", "输入的用户名和当前用户名不一致，请确认，是否要为" + userName + "开启会员？",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mPresenter.userSetVipThread(userName);
                                            }
                                        }, null);
                            }
                        }
                    }
                });

        setUserNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(UserSetVipActivity.this);
                Builder builder1 = new Builder(mContext);
                builder1.setView(et);
                builder1.setTitle("输入用户名");
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(et.getText().toString())) {
                            showToast("输入内容为空");
                        } else {
                            userNameEt.setText(et.getText().toString());
                        }
                    }
                });
                builder1.setNegativeButton("取消", null);
                builder1.create().show();
            }
        });

    }

}
