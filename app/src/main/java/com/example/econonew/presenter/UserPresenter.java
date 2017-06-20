package com.example.econonew.presenter;

import android.content.DialogInterface;

import com.example.econonew.resource.Constant;
import com.example.econonew.server.jpush.SettingActivity;
import com.example.econonew.tools.Voice;
import com.example.econonew.view.activity.user.UserActivity;
import com.example.econonew.view.activity.user.UserRegistActivity;
import com.example.econonew.view.activity.user.UserSetPwdActivity;
import com.example.econonew.view.activity.user.UserSetVipActivity;

/**
 * Created by mengfei on 2017/1/13.
 */
public class UserPresenter extends BaseUserPresenter<UserActivity> {

    public UserPresenter(UserActivity activity) {
        super(activity);
    }

    public void registClick() {
        if (Constant.user == null) {
            mActivity.openOtherActivity(UserRegistActivity.class, true);
        } else {
            mActivity.showToast("请先注销后再进行注册");
        }
    }

    public void userSetVipClick() {
        if (Constant.user == null) {
            mActivity.showNoLoginDialog();
        } else {
            mActivity.openOtherActivity(UserSetVipActivity.class, true);
        }
    }

    // 用户登录按钮的点击事件的处理函数
    public void userLoginClick() {
        if (Constant.user != null) {
            mActivity.showTipDialog("提示", "您已经登陆，请注销后再重新登录", null, null);
        } else {
            mActivity.startLoginActivity(false);
        }
    }

    // 点击修改密码的处理事件函数
    public void setPwdClick() {
        if (Constant.user == null) {
            mActivity.showNoLoginDialog();
        } else {
            mActivity.openOtherActivity(UserSetPwdActivity.class, true);
        }
    }

    public void logoutClick() {
        userLogoutThread(Constant.user);
        mActivity.clearUser();
    }

    // 消息重置的按钮点击事件
    public void voiceResetClick() {
        Voice.getInstance().resetVoice();
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.backHomeActivity();
            }
        };
        mActivity.showTipDialog(null, "重读设置成功", listener, listener);
    }

    public void settingsClick() {
        mActivity.openOtherActivity(SettingActivity.class, false);
    }
}
