package com.example.econonew.server.jpush;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.econonew.db.DBInformation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
		Log.v("isBetweenTime", isOpen + "");
		if (isOpen) {
			if (JPushInterface.isPushStopped(context))
				startPush();
			Log.v("打开", 123 + "");
		} else {
			stopPush();
		}
		Log.v("关闭", isOpen + "");
		return isOpen;
	}

	private boolean isBetweenTime() {
		boolean flag = false;
		List<String> days = new ArrayList<>();
		String str;
		SQLiteDatabase db = new DBInformation(context).getReadableDatabase();
		Cursor time_cursor = db.query("time", null, null, null, null, null, null);
		Cursor cursor = db.rawQuery("select * from time", null);
		if (cursor.getCount() != 0) {
			time_cursor.moveToFirst();
			str = time_cursor.getString(time_cursor.getColumnIndex("weekend"));
			String subStr = str.substring(str.indexOf('[') + 1, str.indexOf(']'));
			String[] strArr = subStr.split(",");
			for (int i = 0; i < strArr.length; i++) {
				String trim = strArr[i].trim();
				days.add(trim);
			}
			Log.v("days", days + "");
			double startTime = Double.valueOf(time_cursor.getString(time_cursor.getColumnIndex("startTime")));
			double endTime = Double.valueOf(time_cursor.getString(time_cursor.getColumnIndex("endTime")));
			if (days.contains(time.getWeek() + "")) {
				double t = time.getHour() + time.getMinute() * 0.01;
				if (startTime < t && t < endTime) {
					flag = true;
				}
			}
		}
		cursor.close();
		time_cursor.close();
		db.close();
		return flag;
	}

	public long timeSurplus() {
		long timeSur = 0;
		SQLiteDatabase db = new DBInformation(context).getReadableDatabase();
		Cursor time_cursor = db.query("time", null, null, null, null, null, null);
		Cursor cursor = db.rawQuery("select * from time", null);
		if (cursor.getCount() != 0) {
			time_cursor.moveToFirst();
			double startTime = Double.valueOf(time_cursor.getString(time_cursor.getColumnIndex("startTime")));
			timeSur = ((long) startTime * 3600000) - (time.getHour() * 3600000 + time.getMinute() * 60000);
		}
		cursor.close();
		time_cursor.close();
		db.close();
		return timeSur;
	}

	private void stopPush() {
		JPushInterface.stopPush(context);
	}

	private void startPush() {
		JPushInterface.resumePush(context);
	}
}
