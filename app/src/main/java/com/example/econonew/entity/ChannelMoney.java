package com.example.econonew.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ChannelMoney
 * @author agnes
 *理财频道添加类
 */
public class ChannelMoney extends AddChannel{
	private String name;   //频道名字
	private JSONObject channelInfo;	//频道信息
	private String type;	//类型
	private String attribute;	//属性
	private String code;	//交易代码

	public ChannelMoney(String name, JSONObject channelInfo) {
		// TODO Auto-generated constructor stub
		super(name, channelInfo); //继承父类的构造方法，并在下面扩展
		this.name=name;
		try {
			type=channelInfo.get("理财名称").toString();
			attribute=channelInfo.get("信息类型").toString();
			code=channelInfo.get("交易代码").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("数据取出错误");
		}
		this.channelInfo=channelInfo;
	}

	public JSONObject getChannelInfo() {
		return channelInfo;
	}
	/*设置频道信息
	 * (non-Javadoc)
	 * @see com.example.ob.ChannelInter#setChannelInfo()
	 */
	public boolean setChannelInfo() {
		JSONObject obj=new JSONObject();
		try {
			obj.put("理财名称", type);
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