package com.example.econonew.view.activity.User;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.presenter.UserPresenter;
import com.example.econonew.resource.Constant;
import com.example.econonew.server.jpush.SettingActivity;
import com.example.econonew.tools.Voice;
import com.example.econonew.utils.URLSettingActivity;


/**
 * UserActivity 此类是用户登陆界面的类
 *
 * @author agnes
 */
public class UserActivity extends BaseUserActivity {

    private Button registBtn;
    private ViewStub isVipVS;

    private TextView userLogout, settingPush, setPwd, voice_repeat;

    // 用户登录的一系列选项
    private LinearLayout userLayout;
    private TextView userTv, userLoginTv, userRegistTv, userSetVipTv;

    private TextView text_user;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_user);

        registBtn = (Button) findViewById(R.id.user_regist);
        text_user = (TextView) findViewById(R.id.user_name);

        settingPush = (TextView) findViewById(R.id.settingPush);
        userLogout = (TextView) findViewById(R.id.logout);
        voice_repeat = (TextView) findViewById(R.id.repeat_voice);
        setPwd = (TextView) findViewById(R.id.setPwd);

        userTv = (TextView) findViewById(R.id.act_user_user_user_tv);
        userLayout = (LinearLayout) findViewById(R.id.act_user_user_ly);
        userLoginTv = (TextView) findViewById(R.id.act_user_login_tv);
        userRegistTv = (TextView) findViewById(R.id.act_user_regist_tv);
        userSetVipTv = (TextView) findViewById(R.id.act_user_setVip_tv);

        isVipVS = (ViewStub) findViewById(R.id.act_user_isVip_vs);
        if(Constant.user != null && Constant.user.isVIP()) {
            isVipVS.inflate();
        }

        TextView setIpTv = (TextView) findViewById(R.id.act_user_set_ip_tv);
        if (Constant.isDeBug) {
            setIpTv.setVisibility(View.VISIBLE);
            setIpTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOtherActivity(URLSettingActivity.class, false);
                }
            });
        }
        initListener();
        initActionBar("用户", true);
    }

    private void initListener() {
        registBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {registClick();}
                                     });
        userTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandUserItem(v);
            }
        });
        userLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLoginClick();
            }
        });
        userRegistTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registClick();
            }
        });
        userSetVipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSetVipClick();
            }
        });
        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.userLogoutThread(Constant.user);
                text_user.setText("未登录");
//                vipTv.setVisibility(View.GONE);
            }
        });

        settingPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openOtherActivity(SettingActivity.class, false);
            }
        });
        setPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPwdClick();
            }
        });
        voice_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceResetClick();
            }
        });
    }

    private void registClick() {
        if (Constant.user == null) {
            openOtherActivity(UserRegistActivity.class, true);
        } else {
            showToast("请先注销后再进行注册");
        }
    }

    private void userSetVipClick() {
        if (Constant.user == null) {
            showNoLoginDialog();
        } else {
            openOtherActivity(UserSetVipActivity.class, true);
        }
    }

    /**
     * 展开或是收起用户的选项
     */
    protected void expandUserItem(View v) {
        if (userLayout.getVisibility() == View.GONE) {
            v.setBackgroundColor(Color.GRAY);
            userLayout.setVisibility(View.VISIBLE);
        } else {
            v.setBackgroundColor(Color.WHITE);
            userLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initDatas() {
        if (Constant.user != null) {
            text_user.setText(Constant.user.getName());
        }
        bindPresenter(new UserPresenter(this));
    }

    // 消息重置的按钮点击事件
    private void voiceResetClick() {
        Voice.getInstance().resetVoice();
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {backHomeActivity();
            }
        };
        showTipDialog(null, "重读设置成功", listener, listener);
    }

    // 用户登录按钮的点击事件的处理函数
    private void userLoginClick() {
        if (Constant.user != null) {
            showTipDialog("提示", "您已经登陆，请注销后再重新登录", null, null);
        } else {
            startLoginActivity(false);
        }
    }

    // 点击修改密码的处理事件函数
    private void setPwdClick() {
        if (Constant.user == null) {
            showNoLoginDialog();
        } else {
            openOtherActivity(UserSetPwdActivity.class, true);
        }
    }
}