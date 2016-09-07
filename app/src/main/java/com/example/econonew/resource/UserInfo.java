package com.example.econonew.resource;

import java.util.HashMap;

/**
 * UserInfo
 *
 * @author agnes 用于创建用户的对象，来存储用户信息和用户行为记录
 */
public class UserInfo {

	private String name;
	private String pwd;
	private boolean isVIP;// 标识是不是Vip

	public void setMessageMap(HashMap<String, EconoNewInfo> messageMap) {
		this.messageMap = messageMap;
	}

	private HashMap<String, EconoNewInfo> messageMap;

	public UserInfo(String name, String pwd) {
		this.name = name;
		this.pwd = pwd;
		messageMap = new HashMap<String, EconoNewInfo>();
	}

	public HashMap<String, EconoNewInfo> getMessageMap() {
		return messageMap;
	}

	public void addMessageMap(EconoNewInfo info) {
		messageMap.put(info.getTitle(), info);
	}

	public boolean isVIP() {
		return isVIP;
	}

	public void setVIP(int isVip) {
		this.isVIP = isVip == 1;
	}

	public void setVIP(boolean isVip) {
		this.isVIP = isVip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
