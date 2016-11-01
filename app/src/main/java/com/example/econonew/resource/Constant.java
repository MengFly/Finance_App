
package com.example.econonew.resource;

import com.example.econonew.entity.UserEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此类里面存储一些App中要用到的一些常量
 */
public class Constant {

    public static final String SPF_KEY_USER = "userInfo";
    public static final String SPF_KEY_UPDATE_DATE = "updateDate";

    public static boolean isDeBug = true;

    public static final List<String> tabList = Arrays.asList("股票", "理财", "基金", "期货", "外汇", "自定义");

    // 用户对象
    public static UserEntity user;

    /**
     * 各个公共栏目的名称
     */
    public static final String[] publicItemNames = new String[]{"股票", "理财", "基金", "期货", "外汇"};


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