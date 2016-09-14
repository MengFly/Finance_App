package com.example.econonew.entity;

import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * ChannelFutures 期货频道添加类 添加期货频道，用于存储选择的期货信息
 *
 * @author agnes
 *
 */
public class ChannelFutures extends AddChannel {
	private String name; // 频道名字
	private JSONObject channelInfo; // 频道信息
	private String type; // 类型
	private String attribute; // 属性
	private String code; // 交易代码

	public ChannelFutures(String name, JSONObject channelInfo) {
		super(name, channelInfo);
		this.name = name;
		try {
			type = channelInfo.get("期货名称").toString();
			attribute = channelInfo.get("信息类型").toString();
			code = channelInfo.get("交易代码").toString();
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.print("数据取出错误");
		}
		this.channelInfo = channelInfo;
	}

	/*
	 * 设置频道信息
	 */
	public boolean setChannelInfo() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("期货名称", type);
			obj.put("外汇类型：", attribute);
			obj.put("交易代码：", code);
			channelInfo.put(name, obj);
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.print("输入数据有错误");
			return false;
		}
		return true;
	}

	public JSONObject getChannelInfo() {
		return channelInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribite(String attribute) {
		this.attribute = attribute;
	}
}