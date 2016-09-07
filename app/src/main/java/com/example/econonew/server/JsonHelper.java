package com.example.econonew.server;

import java.util.List;

/**
 * 解析Json字符串的统一接口
 *
 * @author mengfei
 *
 */
public interface JsonHelper<T> {

	/**
	 * 对Json的信息进行
	 *
	 * @param jsonString
	 *            要解析的Json数据
	 * @return Json信息是否请求
	 */
	boolean stateIsSuccess(String jsonString);

	T getItemFromJson(String jsonString);

	List<T> getItemAllFromJson(String jsonString);

	/**
	 * 当Json解析错误，或者Json信息里面的state状态不为Success的时候会调用这个方法
	 */
	void jsonErrorOrJsonFail();// 当Json解析异常的时候会调用这个方法

}
