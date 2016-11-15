package com.example.econonew.server.jpush;

import android.content.ContentValues;
import android.content.SharedPreferences;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.econonew.R;
import com.example.econonew.db.DBInformation;
import com.example.econonew.view.activity.BaseActivity;

import java.util.HashSet;
import java.util.Set;

public class SettingActivity extends BaseActivity {
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
	CheckBox timeRule;


	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.act_set_push_time);
		startTime = (TimePicker) findViewById(R.id.start_time);
		endTime = (TimePicker) findViewById(R.id.end_time);
		timeRule = (CheckBox) findViewById(R.id.timeRule);

		mSetTime = (Button) findViewById(R.id.bu_setTime);
		mMonday = (CheckBox) findViewById(R.id.act_set_time_monday_cb);
		mTuesday = (CheckBox) findViewById(R.id.act_set_time_tuesday_cb);
		mWednesday = (CheckBox) findViewById(R.id.act_set_time_wednesday_cb);
		mThursday = (CheckBox) findViewById(R.id.act_set_time_thursday_cb);
		mFriday = (CheckBox) findViewById(R.id.act_set_time_friday_cb);
		mSaturday = (CheckBox) findViewById(R.id.act_set_time_saturday_cb);
		mSunday = (CheckBox) findViewById(R.id.act_set_time_sunday_cb);
		initListener();
		initActionBar(false, "设置消息推送时间", true);
	}

	@Override
	public void onStart() {
		super.onStart();
		initData();
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
		timeRule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				startTime.setIs24HourView(isChecked);
				endTime.setIs24HourView(isChecked);
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
		startTime.setCurrentHour(startTimeStr);
		int endTimeStr = mSettings.getInt(ExampleUtil.PREFS_END_TIME, 23);
		endTime.setCurrentHour(endTimeStr);
	}

	/**
	 * 设置允许接收通知时间
	 */
	@SuppressWarnings("deprecation")
	private void setPushTime() {
		double starTime = startTime.getCurrentHour() + startTime.getCurrentMinute() * 0.01;
		double endTime = this.endTime.getCurrentHour() + this.endTime.getCurrentMinute() * 0.01;
		if (starTime > endTime) {
			Toast.makeText(SettingActivity.this, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
			return;
		}
		Set<Integer> days = new HashSet<>();
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
		SQLiteDatabase db = new DBInformation(SettingActivity.this).getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("weekend", days + "");
		values.put("startTime", starTime + "");
		values.put("endTime", endTime + "");
		Cursor cursor = db.rawQuery("select * from time", null);
		db.beginTransaction();
		int n = cursor.getCount();
		Log.v("n", n + "");
		if (n == 0) {
			db.insert("time", null, values);
		} else {
			db.update("time", values, "_id=?", new String[] { "1" });
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		cursor.close();
		db.close();
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
