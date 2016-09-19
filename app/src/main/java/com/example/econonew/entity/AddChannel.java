package com.example.econonew.entity;

import org.json.JSONObject;
/**
 * 实现了接口里面额外定义的三个方法
 */

/**
 * AddChannel
 *
 * @author agens 频道添加的类，是实现各个具体的频道添加的父类
 */
public class AddChannel implements InterChannel {
    private String name;
    private JSONObject channelInfo;

    private String type;
    private String code;
    private String attribute;

    public AddChannel(String name, JSONObject channelInfo) {
        this.name = name;
        this.channelInfo = channelInfo;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JSONObject getChannelInfo() {
        return channelInfo;
    }

    @Override
    public boolean setChannelInfo() {
        return false;
    }

    public void setChannelInfo(JSONObject channelInfo) {
        this.channelInfo = channelInfo;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}