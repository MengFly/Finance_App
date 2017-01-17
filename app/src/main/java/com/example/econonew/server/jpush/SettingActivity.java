package com.example.econonew.server.jpush;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;

import com.example.econonew.R;
import com.example.econonew.databinding.ActSetPushTimeBinding;
import com.example.econonew.db.DBHelperFactory;
import com.example.econonew.entity.PushTime;
import com.example.econonew.view.activity.BaseActivity;

public class SettingActivity extends BaseActivity{
	private ActSetPushTimeBinding mBinding;

	@Override
	protected void initView(Bundle savedInstanceState) {
		mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_set_push_time);
		initListener();
		initActionBar(false, "设置消息推送时间", true);
		initDatas();
	}

	@Override
	protected void initDatas() {
		int timerCount = PushTime.count(PushTime.class);
		PushTime timer;
		if (timerCount > 0) {
			timer = DBHelperFactory.getDBHelper().queryAllItems(PushTime.class).get(0);
		} else {
			timer = new PushTime();
		}
		initUI(timer);
	}

	private void initUI(PushTime timer) {
		for (PushTime.WeekDay day : timer.getDayList()) {
			setWeek(day);
		}
		mBinding.startTime.setCurrentHour(timer.getStartHour());
		mBinding.startTime.setCurrentMinute(timer.getStartMinus());
		mBinding.endTime.setCurrentHour(timer.getEndHour());
		mBinding.endTime.setCurrentMinute(timer.getEndMinus());
	}

	private void initListener() {
		mBinding.buSetTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPushTime();
			}
		});
		mBinding.timeRule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				mBinding.startTime.setIs24HourView(isChecked);
				mBinding.endTime.setIs24HourView(isChecked);
			}
		});
	}
	/**
	 * 设置允许接收通知时间
	 */
	private void setPushTime() {

		int startHour = mBinding.startTime.getCurrentHour();
		int startMinute = mBinding.startTime.getCurrentMinute();
		int endHour = mBinding.endTime.getCurrentHour();
		int endMinute = mBinding.endTime.getCurrentMinute();
		if (endHour < startHour || (endHour == startHour && endMinute < startMinute)) {
			showToast("开始时间不能大于结束时间");
			return;
		}
		PushTime time = new PushTime();
		time.setStartHour(startHour);
		time.setStartMinus(startMinute);
		time.setEndHour(endHour);
		time.setEndMinus(endMinute);

		if (mBinding.actSetTimeMondayCb.isChecked()) {
			time.addWeekDay(PushTime.WeekDay.MONDAY);
		}
		if (mBinding.actSetTimeTuesdayCb.isChecked()) {
			time.addWeekDay(PushTime.WeekDay.TUESDAY);
		}
		if (mBinding.actSetTimeWednesdayCb.isChecked()) {
			time.addWeekDay(PushTime.WeekDay.WEDNESDAY);
		}
		if (mBinding.actSetTimeThursdayCb.isChecked()) {
			time.addWeekDay(PushTime.WeekDay.THURSDAY);
		}
		if (mBinding.actSetTimeFridayCb.isChecked()) {
			time.addWeekDay(PushTime.WeekDay.FRIDAY);
		}
		if (mBinding.actSetTimeSaturdayCb.isChecked()) {
			time.addWeekDay(PushTime.WeekDay.SATURDAY);
		}
		if (mBinding.actSetTimeSundayCb.isChecked()) {
			time.addWeekDay(PushTime.WeekDay.SUNDAY);
		}
		DBHelperFactory.getDBHelper().deleteAll(PushTime.class);
		DBHelperFactory.getDBHelper().insertItems(PushTime.class, time);
		// 调用JPush api设置Push时间
		showToast("设置成功");
	}


	private void setWeek(PushTime.WeekDay day) {
		switch (day) {
			case MONDAY:
				mBinding.actSetTimeMondayCb.setChecked(true);
				break;
			case TUESDAY:
				mBinding.actSetTimeTuesdayCb.setChecked(true);
				break;
			case WEDNESDAY:
				mBinding.actSetTimeWednesdayCb.setChecked(true);
				break;
			case THURSDAY:
				mBinding.actSetTimeThursdayCb.setChecked(true);
				break;
			case FRIDAY:
				mBinding.actSetTimeFridayCb.setChecked(true);
				break;
			case SATURDAY:
				mBinding.actSetTimeSaturdayCb.setChecked(true);
				break;
			case SUNDAY:
				mBinding.actSetTimeSundayCb.setChecked(true);
				break;
		}
	}
}
