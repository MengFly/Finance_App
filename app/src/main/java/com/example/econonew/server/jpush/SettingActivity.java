package com.example.econonew.server.jpush;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import com.example.econonew.R;
import com.example.econonew.db.DBHelperFactory;
import com.example.econonew.entity.PushTime;
import com.example.econonew.view.activity.BaseActivity;


public class SettingActivity extends BaseActivity {

    private TimePicker startTime;
    private TimePicker endTime;
    private CheckBox timeRule;
    private CheckBox actSetTimeMondayCb, actSetTimeTuesdayCb, actSetTimeWednesdayCb, actSetTimeThursdayCb, actSetTimeFridayCb, actSetTimeSaturdayCb, actSetTimeSundayCb;
    private Button buSetTime;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_set_push_time);
        startTime = (TimePicker) findViewById(R.id.start_time);
        endTime = (TimePicker) findViewById(R.id.end_time);
        timeRule = (CheckBox) findViewById(R.id.timeRule);
        actSetTimeMondayCb = (CheckBox) findViewById(R.id.act_set_time_monday_cb);
        actSetTimeTuesdayCb = (CheckBox) findViewById(R.id.act_set_time_tuesday_cb);
        actSetTimeWednesdayCb = (CheckBox) findViewById(R.id.act_set_time_wednesday_cb);
        actSetTimeThursdayCb = (CheckBox) findViewById(R.id.act_set_time_thursday_cb);
        actSetTimeFridayCb = (CheckBox) findViewById(R.id.act_set_time_friday_cb);
        actSetTimeSaturdayCb = (CheckBox) findViewById(R.id.act_set_time_saturday_cb);
        actSetTimeSundayCb = (CheckBox) findViewById(R.id.act_set_time_sunday_cb);
        buSetTime = (Button) findViewById(R.id.bu_setTime);
        initActionBar(false, "设置消息推送时间", true);
        initListener();
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
        startTime.setCurrentHour(timer.getStartHour());
        startTime.setCurrentMinute(timer.getStartMinus());
        endTime.setCurrentHour(timer.getEndHour());
        endTime.setCurrentMinute(timer.getEndMinus());
    }

    private void initListener() {
        buSetTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
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

    /**
     * 设置允许接收通知时间
     */
    private void setPushTime() {

        int startHour = startTime.getCurrentHour();
        int startMinute = startTime.getCurrentMinute();
        int endHour = endTime.getCurrentHour();
        int endMinute = endTime.getCurrentMinute();
        if (endHour < startHour || (endHour == startHour && endMinute < startMinute)) {
            showToast("开始时间不能大于结束时间");
            return;
        }
        PushTime time = new PushTime();
        time.setStartHour(startHour);
        time.setStartMinus(startMinute);
        time.setEndHour(endHour);
        time.setEndMinus(endMinute);

        if (actSetTimeMondayCb.isChecked()) {
            time.addWeekDay(PushTime.WeekDay.MONDAY);
        }
        if (actSetTimeTuesdayCb.isChecked()) {
            time.addWeekDay(PushTime.WeekDay.TUESDAY);
        }
        if (actSetTimeWednesdayCb.isChecked()) {
            time.addWeekDay(PushTime.WeekDay.WEDNESDAY);
        }
        if (actSetTimeThursdayCb.isChecked()) {
            time.addWeekDay(PushTime.WeekDay.THURSDAY);
        }
        if (actSetTimeFridayCb.isChecked()) {
            time.addWeekDay(PushTime.WeekDay.FRIDAY);
        }
        if (actSetTimeSaturdayCb.isChecked()) {
            time.addWeekDay(PushTime.WeekDay.SATURDAY);
        }
        if (actSetTimeSundayCb.isChecked()) {
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
                actSetTimeMondayCb.setChecked(true);
                break;
            case TUESDAY:
                actSetTimeTuesdayCb.setChecked(true);
                break;
            case WEDNESDAY:
                actSetTimeWednesdayCb.setChecked(true);
                break;
            case THURSDAY:
                actSetTimeThursdayCb.setChecked(true);
                break;
            case FRIDAY:
                actSetTimeFridayCb.setChecked(true);
                break;
            case SATURDAY:
                actSetTimeSaturdayCb.setChecked(true);
                break;
            case SUNDAY:
                actSetTimeSundayCb.setChecked(true);
                break;
        }
    }
}
