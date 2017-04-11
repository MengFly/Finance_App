
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

    public static final List<String> tabList = Arrays.asList("股票", "理财", "基金", "期货", "外汇", "自定义");

    // 用户对象
    public static UserEntity user;

    /**
     * 各个公共栏目的名称
     */
    public static final String[] publicItemNames = new String[]{"股票", "理财", "基金", "期货", "外汇"};


    // 朗读列表
    public static int read_tab = 0;

}