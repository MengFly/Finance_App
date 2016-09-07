package com.example.econonew.entity;

import java.io.Serializable;


/**
 * 这个类是用来管理从服务器哪里接收到的消息 将从服务器哪里接收到的消息内容存储在这个类中
 *
 * @author mengfei
 *
 */
public class MsgItemEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	// 存储消息标题
	private String msgTitle;

	// 存储消息内容
	private String msgContent;

	private String msgContentUrl;

	// 存储消息摘要内容，主要用于朗读使用
	private String msgGeneral;

	// 存储消息的图片，即在ListView里面显示的图片
	private String imageTitleUrl;

	private boolean isLove;// 是否收藏


	/**
	 * @param msgTitle
	 *            消息标题
	 * @param msgContent
	 *            消息内容-主要是用于展示在webView里面
	 * @param msgGeneral
	 *            消息摘要--主要是用在朗读部分
	 * @param msgContentUrl
	 *            消息内容目标网页的url
	 * @param imageTitleUrl
	 *            消息列表的图片的Url地址
	 */
	public MsgItemEntity(String msgTitle, String msgContent, String msgContentUrl, String msgGeneral,
						 String imageTitleUrl) {
		this.msgTitle = msgTitle;
		this.msgContent = msgContent;
		this.msgContentUrl = msgContentUrl;
		this.msgGeneral = msgGeneral;
		this.imageTitleUrl = imageTitleUrl;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public String getImageTitleUrl() {
		return imageTitleUrl;
	}

	public String getMsgGeneral() {
		return msgGeneral;
	}

	public String getMsgContentUrl() {
		return msgContentUrl;
	}

	public boolean isLove() {
		return isLove;
	}

	public void setLove(boolean isLove) {
		this.isLove = isLove;
	}

	private boolean isVip; // 是否是VIP信息

	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean isVip) {
		this.isVip = isVip;
	}

}
