package com.example.econonew.server.jpush;

public class MyTime {
	private int week;
	private int hour;
	private int minute;
	public MyTime(int week,int hour,int minute){
		this.week=week;
		this.hour=hour;
		this.minute=minute;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
}
