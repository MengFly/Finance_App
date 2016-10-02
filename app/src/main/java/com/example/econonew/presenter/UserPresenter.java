package com.example.econonew.presenter;

import android.content.DialogInterface;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.example.econonew.view.activity.FinanceApplication;
import com.example.econonew.view.activity.User.BaseUserActivity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.UserInfo;
import com.example.econonew.server.json.JsonCast;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.json.UserJsonHelper;
import com.example.econonew.tools.EncodeStrTool;
import com.example.econonew.tools.URLManager;

import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * 管理用户操作的管理类，其中包括用户登录，注册，注销，设置Vip等等
 * Created by mengfei on 2016/9/14.
 */
public class UserPresenter extends BasePresenter<BaseUserActivity> {

    private Thread userLoginThread;//用户登录线程
    private Thread userLogoutThread;//用户注销线程
    private Thread userRigistThread;//用户注册线程
    private Thread userSetPassThread;//用户修改密码线程
    private Thread userSetVipThread;//用户设置Vip线
    

    public UserPresenter(BaseUserActivity activity) {
        super(activity);
    }

    //开启一个用户登录的线程
    public void userLoginThread(String userName, String userPass, boolean isStartLogin) {
        if(checkUserNameAndPass(userName, userPass)) {
            mActivity.showProDialog();
            initUserLoginThread(userName, userPass, isStartLogin);
            userLoginThread.start();
        }
    }

    //开启一个用户注销的线程
    public void userLogoutThread(UserInfo user) {
        if (user != null) {
            initLogoutThread(user);
            mActivity.showProDialog();
            userLogoutThread.start();
        } else {
            DialogInterface.OnClickListener loginListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mActivity.startLoginActivity(false);
                }
            };
            mActivity.showTipDialog(null, "当前未登录，是否现在登陆？", loginListener, null);
        }
    }

    //开启一个用户注册的线程
    public void userRegistThread(String userName, String userPass1, String userPass2) {
        if (checkUserAndPass(userName,userPass1, userPass2)) {
            mActivity.showProDialog();
            initUserRegistThread(userName, userPass1);
            userRigistThread.start();
        }
    }

    //开启一个用户修改密码的线程
    public void userSetPassThread(String oldPass, String newPass1, String newPass2) {
        if(checkPass(oldPass, newPass1, newPass2)) {
            mActivity.showProDialog();
            initUserSetPassThread(oldPass, newPass1);
            userSetPassThread.start();
        }
    }

    //开启一个用户设置vip的线程
    public void userSetVipThread(String userName) {
        mActivity.showProDialog();
        initUserSetVipThread(userName);
        userSetVipThread.start();
    }

    //初始化用户设置Vip的线程
    private void initUserSetVipThread(final String userName) {
        final String setVipUrl = URLManager.getSetVipURL(userName);
        final NetClient.OnResultListener listener = new NetClient.OnResultListener() {

            @Override
            public void onSuccess(String response) {
                JSONObject obj = JsonCast.getJsonObject(response);
                if (obj == null) {
                    mActivity.showToast("链接失败");
                } else {
                    String result = JsonCast.getString(obj, "result");
                    if ("success".equals(result)) {
                        mActivity.showTipDialog(null, "Vip注册成功", null, null);
                        Constant.user.setVIP(true);
                        mActivity.saveUserInfo(Constant.user);
                    } else {
                        mActivity.showTipDialog(null, result, null, null);
                    }
                }
                mActivity.hintProDialog();
            }

            public void onError(com.android.volley.VolleyError error) {
                super.onError(error);
                mActivity.hintProDialog();
            }
        };
        userSetVipThread =  new Thread() {
            public void run() {
                NetClient.getInstance().excuteGetForString(mActivity, setVipUrl, listener);
            }
        };
    }

    //初始化修改密码的线程
    private void initUserSetPassThread(String oldPass, String newPass1) {
        String oldEncodePass = EncodeStrTool.getInstance().getEncodeMD5Str(oldPass);
        String newEncodePass = EncodeStrTool.getInstance().getEncodeMD5Str(newPass1);
        final String url = URLManager.getSetPassWordURL(Constant.user.getName(), oldEncodePass, newEncodePass);

        final DialogInterface.OnClickListener setPwdOkListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.startLoginActivity(false);
            }
        };

        final NetClient.OnResultListener listener = new NetClient.OnResultListener() {

            @Override
            public void onSuccess(String response) {
                JSONObject obj = JsonCast.getJsonObject(response);
                if (obj != null) {
                    String result = JsonCast.getString(obj, "result");
                    if ("success".equals(JsonCast.getString(obj, "status"))) {
                        mActivity.showTipDialog(null, "修改成功，请重新登录", setPwdOkListener, setPwdOkListener);
                        mActivity.removeUserAndCookie();//修改密码成功的时候移除用户和之前的Cookie信息
                    } else {
                        mActivity.showTipDialog(null, result, null, null);
                    }
                } else {
                    mActivity.showToast("密码修改失败");
                }
                mActivity.hintProDialog();
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                mActivity.hintProDialog();
            }
        };
        userSetPassThread = new Thread() {
            public void run() {
                NetClient.getInstance().excuteGetForString(mActivity, url, listener);
            }
        };
    }

    //初始化注册线程
    private void initUserRegistThread(String userName, String userPass) {
        String encodePass = EncodeStrTool.getInstance().getEncodeMD5Str(userPass);
        final String url = URLManager.getRegistURL(userName, encodePass);
        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mActivity.startLoginActivity(false);
                mActivity.finish();
            }
        };
        final NetClient.OnResultListener resultListener = new NetClient.OnResultListener() {

            @Override
            public void onSuccess(String response) {
                JSONObject obj = JsonCast.getJsonObject(response);
                if (obj != null) {
                    String status = JsonCast.getString(obj, "status");
                    if ("success".equals(status)) {
                        mActivity.showTipDialog(null, "注册成功", listener, null);
                    } else {
                        mActivity.showToast(JsonCast.getString(obj, "result"));
                    }
                } else {
                    mActivity.showToast("注册失败");
                }
                mActivity.hintProDialog();
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                mActivity.hintProDialog();
            }
        };
        userRigistThread = new Thread() {
            public void run() {
                NetClient.getInstance()
                        .excuteGetForString(mActivity, url, resultListener);
            }
        };
    }

    //初始化登录线程
    private void initUserLoginThread(final String userName, final String userPass, final boolean isStartLogin) {
        final String encodePass = EncodeStrTool.getInstance().getEncodeMD5Str(userPass);
        final String loginUrl = URLManager.getLoginURL(userName, encodePass);
        final NetClient.OnResultListener userLoginListener = new NetClient.OnResultListener() {
            @Override
            public void onSuccess(String response) {
                UserJsonHelper helper = new UserJsonHelper(mActivity);
                UserInfo user = helper.excuteJsonForItem(response);// 通过Json数据获取User
                if (user != null) {
                    Constant.user = new UserInfo(userName, encodePass);
                    Constant.user.setVIP(user.isVIP());
                    if (!isStartLogin) {
                        FinanceApplication.getInstance().refreshUserData(Constant.user);
                        FinanceApplication.getInstance().refreshPublicData();
                    }
                    mActivity.saveUserInfo(Constant.user);
                    mActivity.backHomeActivity();
                } else {
                    mActivity.showToast("登录失败");
                }
                mActivity.hintProDialog();
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                mActivity.hintProDialog();
            }
        };
        userLoginThread = new Thread() {
            @Override
            public void run() {
                NetClient.getInstance().excuteGetForString(mActivity, loginUrl, userLoginListener);
            }
        };
    }

    /** 开启一个用户注销的线程 启动线程链接服务器根据用户名以及电话号码进行注销操作 */
    private void initLogoutThread(UserInfo user) {
        final String url = URLManager.getLogoutURL(user.getName());
        final NetClient.OnResultListener requestListener = new NetClient.OnResultListener() {// 注销成功后的回调事件

            @Override
            public void onSuccess(String response) {
                JSONObject responseObject = JsonCast.getJsonObject(response);
                if (responseObject != null) {
                    mActivity.removeUserAndCookie();
                    FinanceApplication.getInstance().refreshUserData(Constant.user);
                    FinanceApplication.getInstance().refreshPublicData();
                    String result = JsonCast.getString(responseObject, "result");
                    if (result != null) {
                        if ("success".equals(result)) {
                            mActivity.showTipDialog(null, "注销成功", null, null);
                        } else {
                            mActivity.showTipDialog(null, result, null, null);
                        }
                    } else {
                        mActivity.showToast("注销失败");
                    }
                }
                mActivity.hintProDialog();
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                mActivity.hintProDialog();
                mActivity.removeUserAndCookie();
                FinanceApplication.getInstance().refreshUserData(Constant.user);
                FinanceApplication.getInstance().refreshPublicData();
            }
        };
        userLogoutThread = new Thread() {
            public void run() {
                NetClient.getInstance().excuteGetForString(mActivity, url,
                        requestListener);
            }
        };
    }

    //检查UserName和UserPass 是否符合标准
    private boolean checkUserNameAndPass(String userName, String userPass) {
        if (TextUtils.isEmpty(userName)) { // 没有输入用户名
            mActivity.showToast("请输入用户名");
            return false;
        } else if (userPass.length() > 10 || userPass.length() < 6) { // 密码长度（6~10个数字之间）
            mActivity.showToast("密码长度不对");
            return false;
        }
        return true;
    }

    // 判断用户名和密码是否符合
    private boolean checkUserAndPass(String userName, String passWord, String passWord2) {
        // 如果为空
        if (TextUtils.isEmpty(userName)) {
            mActivity.showToast("请输入用户名");
            return false;
        }
        if (!Pattern.matches("1\\d{10}", userName)) {
            mActivity.showToast("手机号码格式不正确");
            return false;
        }
        // 输入密码长度不对（应该在6~10个数字之间）
        else if (passWord.length() > 10 || passWord.length() < 6) {
            mActivity.showToast("密码长度不对");
            return false;
        }
        // 用户两次输入的密码不一致
        else if (!passWord.equals(passWord2)) {
            mActivity.showToast("两次输入密码不一致");
            return false;
        } else {
            return true;
        }
    }

    // 判断用户密码是否符合标准
    private boolean checkPass(String oldPassWord, String newPassWord, String newPassWord1) {
        String encodePass = EncodeStrTool.getInstance().getEncodeMD5Str(oldPassWord);
        if (!encodePass.equals(Constant.user.getPwd())) {
            mActivity.showToast("输入旧密码错误");
            return false;
        } else {
            if (!newPassWord.equals(newPassWord1)) {
                mActivity.showToast("两次输入新密码不一样");
                return false;
            } else if (newPassWord.length() > 10 || newPassWord.length() < 6) {
                mActivity.showToast("密码长度必须在6-10之间");
                return false;
            } else {
                return true;
            }
        }
    }

}