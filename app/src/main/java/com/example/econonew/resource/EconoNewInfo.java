package com.example.econonew.resource;

/**
 * EconoNewInfo
 * @author agnes
 *用于创建单条新闻信息，便于保存用户的行为记录。
 */
public class EconoNewInfo {
	private String title;
	public EconoNewInfo(String title){
		this.title=title;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
