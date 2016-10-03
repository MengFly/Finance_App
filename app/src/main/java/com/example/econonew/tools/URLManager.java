package com.example.econonew.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.UserInfo;
import com.example.econonew.view.activity.FinanceApplication;

import java.net.URLEncoder;

/**
 * 用于管理URL和生成URL
 * Created by mengfei on 2016/9/13.
 */
public class URLManager {

    // 中间层服务器的常量
    private static final String URL = "http://115.28.188.113:8080/Finance_Server1.0.0"; // 中间服务器地址
    private static final String OPERATION_CONNECTION = "connect.action"; // 连接操作
    private static final String OPERATION_LOGIN = "login.action"; // 登录操作
    private static final String OPERATION_LOGOUT = "logout.action"; // 注销操作
    private static final String OPERATION_REGIST = "regist.action"; // 注册操作
    private static final String OPERATION_SETCHNL = "setchnl.action"; // 设置频道操作
    private static final String OPERATION_GETCHNL = "getchnl.action"; // 获取自定义频道操作
    private static final String OPERATION_SETPWD = "uppswd.action"; // 修改密码的操作
    private static final String OPERATION_MSG = "message.action";// 显示消息内容的操作
    private static final String OPERATION_SET_VIP = "setvip.action";
    private static final String OPERATION_DELETE_CHANNEL = "delchnl.action";// 删除频道操作
    private static final String CAI_BIAN_URL = "http://115.29.139.72/caibian1";

    public static String getCaiBianURL() {
        return CAI_BIAN_URL;
    }

    public static String getURL() {
        if (Constant.isDeBug) {
            SharedPreferences spf = FinanceApplication.getInstance().getSharedPreferences("ip", Context.MODE_PRIVATE);
            String currentIp = spf.getString("current_ip", null);
            if (!TextUtils.isEmpty(currentIp)) {
                return "http://" + currentIp + ":8080/Finance_Server1.0.0";
            } else {
                return URL;
            }
        } else {
            return URL;
        }
    }

    /**
     * 获取获取消息的URL
     */
    public static String getConnectURL() {
        return getURL() + "/" + OPERATION_CONNECTION + "?phone=''";
    }

    /**
     * 获取用户登录的URL
     *
     * @param userName 用户名
     * @param userPass 用户密码
     * @return 用户登录的URL
     */
    public static String getLoginURL(String userName, String userPass) {
        return getURL() + "/" + OPERATION_LOGIN + "?phone=" + userName
                + "&password=" + userPass;
    }

    /**
     * 获取用户登录的URL
     *
     * @param user 用户
     * @return 用户登录的URL
     */
    public static String getLoginURL(UserInfo user) {
        return getLoginURL(user.getName(), user.getPwd());
    }

    /**
     * 获取注册的URL
     *
     * @param registName 注册的用户名
     * @param registPass 注册的用户密码
     * @return 注册的URL
     */
    public static String getRegistURL(String registName, String registPass) {
        return getURL() + "/" + OPERATION_REGIST
                + "?phone=" + registName + "&password=" + registPass;
    }

    /**
     * 获取用户注销的URL
     *
     * @param userName 用户名
     * @return 用户注销的URL
     */
    public static String getLogoutURL(String userName) {
        return getURL() + "/" + OPERATION_LOGOUT + "?" + "phone=" + userName;
    }

    /**
     * 获取用户修改密码的URL
     *
     * @param userName 用户名
     * @param oldPass  老密码
     * @param newPass  新密码
     * @return 用户修改密码的URL
     */
    public static String getSetPassWordURL(String userName, String oldPass, String newPass) {
        return getURL() + "/" + OPERATION_SETPWD
                + "?phone=" + userName + "&password=" + newPass
                + "&oldPswd=" + oldPass;
    }


    /**
     * 获取用户注册Vip的URL
     *
     * @param userName 用户名
     * @return 用户注册VIp的URL
     */
    public static String getSetVipURL(String userName) {
        return getURL() + "/" + OPERATION_SET_VIP + "?phone=" + userName;
    }

    /**
     * 获取获取用户频道的URL
     *
     * @param userName 用户名
     * @return 获取用户频道的URL
     */
    public static String getChannelURL(String userName) {
        return getURL() + "/" + OPERATION_GETCHNL + "?phone=" + userName;
    }

    /**
     * 获取添加频道的URL
     *
     * @param userName 用户名
     * @param entity   用户要添加的频道实体类
     * @return 添加频道的URL
     */
    public static String getSetChannelURL(String userName, ChannelEntity entity) {

        StringBuilder msg = new StringBuilder(getURL() + "/" + OPERATION_SETCHNL + "?");
        try {
            msg.append("phone=");
            msg.append(userName);
            msg.append("&typeName=");
            msg.append(URLEncoder.encode(entity.getName(), "utf-8"));
            msg.append("&domainName=");
            msg.append(URLEncoder.encode(entity.getType(), "utf-8"));
            msg.append("&stairName=");
            msg.append(URLEncoder.encode(entity.getAttribute(), "utf-8"));
            msg.append("&stockCode=");
            msg.append(URLEncoder.encode(entity.getCode(), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg.toString();
    }

    /**
     * 获取删除频道的URL
     *
     * @param userName 用户名
     * @param entity   频道实体类
     * @return 删除频道URL
     */
    public static String getDeleteChannelURL(String userName, ChannelEntity entity) {
        return getURL() + "/" + OPERATION_DELETE_CHANNEL + "?phone="
                + userName + "&channelId=" + entity.getId();
    }

    /**
     * 获取查看消息内容的URL
     *
     * @param msgId 消息ID
     * @return 消息内容的URL
     */
    public static String getMsgContentURL(int msgId) {
        return getURL() + "/" + OPERATION_MSG + "?id=" + msgId;
    }


}
