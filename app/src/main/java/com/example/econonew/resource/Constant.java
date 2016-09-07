
package com.example.econonew.resource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此类里面存储一些App中要用到的一些常量
 */
public class Constant {

    // 中间层服务器的常量
    public static final String URL = "http://115.28.188.113:8080/Finance_Server1.0.0"; // 中间服务器地址
    public static final String OPERATION_CONNECTION = "connect.action"; // 连接操作
    public static final String OPERATION_LOGIN = "login.action"; // 登录操作
    public static final String OPERATION_LOGOUT = "logout.action"; // 注销操作
    public static final String OPERATION_REGIST = "regist.action"; // 注册操作
    public static final String OPERATION_SETCHNL = "setchnl.action"; // 设置频道操作
    public static final String OPERATION_GETCHNL = "getchnl.action"; // 获取自定义频道操作
    public static final String OPERATION_SETPWD = "uppswd.action"; // 修改密码的操作
    public static final String OPERATION_MSG = "message.action";// 显示消息内容的操作
    public static final String OPERATION_SET_VIP = "setvip.action";
    public static final String OPERATION_DELETE_CHANNEL = "delchnl.action";// 删除频道操作

    public static final String SPF_KEY_USER = "userInfo";
    public static final String SPF_KEY_UPDATE_DATE = "updateDate";

    public static final List<String> tabList = Arrays.asList("股票", "理财", "基金", "期货", "外汇", "自定义");

    // 用户对象
    public static UserInfo user;

    /**
     * 各个公共栏目的名称
     */
    public static final String[] publicItemNames = new String[]{"股票", "理财", "基金", "期货", "外汇"};
    private static final Map<String, String> publicDataTableNames = new HashMap<>();

    static {
        publicDataTableNames.put("股票", "stock_info");
        publicDataTableNames.put("理财", "money_info");
        publicDataTableNames.put("基金", "funds_info");
        publicDataTableNames.put("期货", "futures_info");
        publicDataTableNames.put("外汇", "exchange_info");
    }

    ;

    /**
     * 根据消息的名称获取公共消息有关的数据库
     *
     * @param messageName 消息名称
     * @return 数据库名称
     */
    public static String getTableName(String messageName) {
        return publicDataTableNames.get(messageName);
    }

    /**
     * 用户自定义的频道信息的各表名称 其顺序依次为： "stock_channel", "money_channel",
     * "funds_channel", "exchange_channel", "futures_channel"
     */
    public static final String[] selfDataTableNames = new String[]{"stock_channel", "money_channel", "funds_channel",
            "futures_channel", "exchange_channel"};
    private static final Map<String, String> _selfDataTableNames = new HashMap<>();

    static {
        _selfDataTableNames.put("股票", "stock_channel");
        _selfDataTableNames.put("理财", "money_channel");
        _selfDataTableNames.put("基金", "funds_channel");
        _selfDataTableNames.put("期货", "futures_channel");
        _selfDataTableNames.put("外汇", "exchange_channel");
    }

    /**
     * 根据频道的类型名称获取相应的数据表名称
     *
     * @param channelName 频道类型
     * @return 频道相关的数据表的名称
     */
    public static String getSelfTableName(String channelName) {
        return _selfDataTableNames.get(channelName);
    }

    // 朗读列表
    public static int read_tab = 0;

}