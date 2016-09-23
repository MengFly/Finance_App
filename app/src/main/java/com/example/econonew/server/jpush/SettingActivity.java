package com.example.econonew.server.jpush;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.econonew.R;
import com.example.econonew.resource.DB_Information;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.InstrumentedActivity;

public class SettingActivity extends InstrumentedActivity {
	TimePicker startTime;
	TimePicker endTime;
	CheckBox mMonday;
	CheckBox mTuesday;
	CheckBox mWednesday;
	CheckBox mThursday;
	CheckBox mFriday;
	CheckBox mSaturday;
	CheckBox mSunday;
	Button mSetTime;
	SharedPreferences mSettings;
	Editor mEditor;
	CheckBox timeRule;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.act_set_push_time);
		initView();
		initListener();
		Calendar c = Calendar.getInstance();
		Log.v("System.week", c.get(Calendar.DAY_OF_WEEK) + "");
		Log.v("System.hour", c.get(Calendar.HOUR_OF_DAY) + "");
		Log.v("System.week", c.get(Calendar.MINUTE) + "");
	}

	@Override
	public void onStart() {
		super.onStart();
		initData();
	}

	private void initView() {
		startTime = (TimePicker) findViewById(R.id.start_time);
		endTime = (TimePicker) findViewById(R.id.end_time);
		timeRule = (CheckBox) findViewById(R.id.timeRule);
		timeRule.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				startTime.setIs24HourView(isChecked);
				endTime.setIs24HourView(isChecked);
			}
		});

		mSetTime = (Button) findViewById(R.id.bu_setTime);
		mMonday = (CheckBox) findViewById(R.id.act_set_time_monday_cb);
		mTuesday = (CheckBox) findViewById(R.id.act_set_time_tuesday_cb);
		mWednesday = (CheckBox) findViewById(R.id.act_set_time_wednesday_cb);
		mThursday = (CheckBox) findViewById(R.id.act_set_time_thursday_cb);
		mFriday = (CheckBox) findViewById(R.id.act_set_time_friday_cb);
		mSaturday = (CheckBox) findViewById(R.id.act_set_time_saturday_cb);
		mSunday = (CheckBox) findViewById(R.id.act_set_time_sunday_cb);
	}

	private void initListener() {
		mSetTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.requestFocus();
				v.requestFocusFromTouch();
				setPushTime();
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void initData() {
		mSettings = getSharedPreferences(ExampleUtil.PREFS_NAME, MODE_PRIVATE);
		String days = mSettings.getString(ExampleUtil.PREFS_DAYS, "");
		if (!TextUtils.isEmpty(days)) {
			initAllWeek(false);
			String[] sArray = days.split(",");
			for (String day : sArray) {
				setWeek(day);
			}
		} else {
			initAllWeek(true);
		}
		int startTimeStr = mSettings.getInt(ExampleUtil.PREFS_START_TIME, 0);
		startTime.setCurrentHour(Integer.valueOf(startTimeStr));
		int endTimeStr = mSettings.getInt(ExampleUtil.PREFS_END_TIME, 23);
		endTime.setCurrentHour(Integer.valueOf(endTimeStr));
	}

	/**
	 * 设置允许接收通知时间
	 */
	@SuppressWarnings("deprecation")
	private void setPushTime() {
		double startime = startTime.getCurrentHour() + startTime.getCurrentMinute() * 0.01;
		double endtime = endTime.getCurrentHour() + endTime.getCurrentMinute() * 0.01;
		if (startime > endtime) {
			Toast.makeText(SettingActivity.this, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
			return;
		}
		Set<Integer> days = new HashSet<Integer>();
		if (mSunday.isChecked()) {
			days.add(1);
		}
		if (mMonday.isChecked()) {
			days.add(2);
		}
		if (mTuesday.isChecked()) {
			days.add(3);
		}
		if (mWednesday.isChecked()) {
			days.add(4);
		}
		if (mThursday.isChecked()) {
			days.add(5);
		}
		if (mFriday.isChecked()) {
			days.add(6);
		}
		if (mSaturday.isChecked()) {
			days.add(7);
		}

		// 调用JPush api设置Push时间
		Log.v("startime", startime + "");
		Log.v("endtime", endtime + "");
		Log.v("days", days + "");
		SQLiteDatabase db = new DB_Information(SettingActivity.this).getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("weekend", days + "");
		values.put("startTime", startime + "");
		values.put("endTime", endtime + "");

		Cursor cursor = db.rawQuery("select * from time", null);
		int n = cursor.getCount();
		Log.v("n", n + "");
		if (n == 0) {
			db.insert("time", null, values);
		} else {
			db.update("time", values, "_id=?", new String[] { "1" });
		}
		Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
	}


	private void setWeek(String day) {
		int dayId = Integer.valueOf(day);
		switch (dayId) {
			case 1:
				mSunday.setChecked(true);
				break;
			case 2:
				mMonday.setChecked(true);
				break;
			case 3:
				mTuesday.setChecked(true);
				break;
			case 4:
				mWednesday.setChecked(true);
				break;
			case 5:
				mThursday.setChecked(true);
				break;
			case 6:
				mFriday.setChecked(true);
				break;
			case 7:
				mSaturday.setChecked(true);
				break;
		}
	}

	private void initAllWeek(boolean isChecked) {
		mSunday.setChecked(isChecked);
		mMonday.setChecked(isChecked);
		mTuesday.setChecked(isChecked);
		mWednesday.setChecked(isChecked);
		mThursday.setChecked(isChecked);
		mFriday.setChecked(isChecked);
		mSaturday.setChecked(isChecked);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}
