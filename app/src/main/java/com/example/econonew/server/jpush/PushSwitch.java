package com.example.econonew.server.jpush;

import android.content.Context;

import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;

public class PushSwitch {
	private Context context; // getApplicationContext()
	private MyTime time; // 得到的系统当前的时间

	public PushSwitch(Context context) {
		this.context = context;
		Calendar c = Calendar.getInstance();
		time = new MyTime(c.get(Calendar.DAY_OF_WEEK), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
	}

	public boolean isReceivePush() {
		boolean isOpen = isBetweenTime();
		if (isOpen) {
			if (JPushInterface.isPushStopped(context))
				startPush();
		} else {
			stopPush();
		}
		return isOpen;
	}

	private boolean isBetweenTime() {
		return true;
	}


	private void stopPush() {
		JPushInterface.stopPush(context);
	}

	private void startPush() {
		JPushInterface.resumePush(context);
	}
}
