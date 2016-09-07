package com.example.econonew.channel.object;

import org.json.JSONObject;
/**
 * InterChannel
 增加了接口的三个方法分别是：
 getName();
 getType();以及
 getAttribute();
 主要是为了AddChannelClickListener使用泛型方便
 */

/**
 * ChannelInter
 * @author agnes
 *这个接口是抽象出了频道的方法
 *
 */
public interface InterChannel {

	public JSONObject getChannelInfo();

	public boolean setChannelInfo();

	public void setName(String name);

	public String getName();

	public String getCode();

	public String getType();

	public String getAttribute();

}