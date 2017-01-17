
package com.example.econonew.resource;

import com.example.econonew.entity.UserEntity;

import java.util.Arrays;
import java.util.List;

/**
 * 此类里面存储一些App中要用到的一些常量
 */
public class Constant {

    public static final String SPF_KEY_USER = "userInfo";

    public static boolean isDeBug = true;

    //实时信息的类型
    public static final String[] MESSAGE_CURRENT_TYPES =
            {"公司基本信息",
            "股票基本信息",
            "财务指标行业排名函数"};
    //缓存消息类型
    public static final String[] MESSAGE_CACHE_TYPES = {"定期报告披露事件函数"};
    //存库消息类型
    public static final String[] MESSAGE_SAVE_TYPES =
            {
                    "业绩预告披露事件函数",
                    "公开信息/龙虎榜披露事件函数" ,
                    "分红披露事件函数",
                    "融资披露事件函数",
                    "高管交易披露事件函数",
                    "股东交易披露事件函数",
                    "大宗交易披露事件函数",
                    "投资日历事件函数",
                    "猜一猜事件函数",
                    "宏观信息事件函数"
            };

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

    // 朗读列表
    public static int read_tab = 0;

}