package com.example.econonew.main.object;

import org.json.JSONObject;

import java.util.List;

public interface InterMessage<T> {

	public JSONObject getInfo();

	public void setInfo();

	/**
	 * 获取消息列表
	 */
	public List<T> getMsgList();

	/**
	 * 获取消息的数量
	 */
	public int getMsgCount();

}