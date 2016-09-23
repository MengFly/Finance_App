package com.example.econonew.resource;

import org.json.JSONObject;

import java.util.List;

public interface InterMessage<T> {

	JSONObject getInfo();

	void setInfo();

	/**
	 * 获取消息列表
	 */
	List<T> getMsgList();

	/**
	 * 获取消息的数量
	 */
	int getMsgCount();

}