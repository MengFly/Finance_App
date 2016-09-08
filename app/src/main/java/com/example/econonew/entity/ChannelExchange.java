package com.example.econonew.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ChannelExchange
 * @author agnes 外汇频道添加类
 */
public class ChannelExchange extends AddChannel {
	private String name; // 频道名字
	private JSONObject channelInfo; // 频道信息
	private String type; // 外汇类型
	private String attribute; // 外汇属性
	private String code; // 外汇代码

	public ChannelExchange(String name, JSONObject channelInfo) {
		super(name, channelInfo);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.channelInfo = channelInfo;
		try {
			type = channelInfo.get("外汇名称").toString();
			attribute = channelInfo.get("信息类型").toString();
			code = channelInfo.get("交易代码").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("数据取出错误");
		}
	}

	public JSONObject getChannelInfo() {
		return channelInfo;
	}

	/*
	 * 设置频道信息 (non-Javadoc)
	 *
	 * @see com.example.ob.ChannelInter#setChannelInfo()
	 */
	public boolean setChannelInfo() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("外汇名称：", type);
			obj.put("外汇类型：", attribute);
			obj.put("交易代码：", code);
			channelInfo.put(name, obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("输入数据有错误");
			return false;
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
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