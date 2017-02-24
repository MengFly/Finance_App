package com.example.econonew.server.jpush;

import android.content.Context;

import com.example.econonew.db.DBHelperFactory;
import com.example.econonew.entity.PushTime;

import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;

public class PushSwitch {

    private static final String TAG = "timer";
    private Context context; // getApplicationContext()

    public PushSwitch(Context context) {
        this.context = context;
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
        //获取到当前的时间
        Calendar nowTimer = Calendar.getInstance();
        if (DBHelperFactory.getDBHelper().queryAllItems(PushTime.class).size()>0) {
            PushTime settingTime = DBHelperFactory.getDBHelper().queryAllItems(PushTime.class).get(0);
            if (settingTime.isContainWeekDay(nowTimer.get(Calendar.DAY_OF_WEEK))) {
                int nowHour = nowTimer.get(Calendar.HOUR_OF_DAY);
                int nowMinute = nowTimer.get(Calendar.MINUTE);
                if (nowHour > settingTime.getStartHour() && nowHour < settingTime.getEndHour()) {
                    return true;
                } else if (nowHour == settingTime.getStartHour() && nowMinute > settingTime.getStartMinus()) {
                    return true;
                } else if (nowHour == settingTime.getEndHour() && nowMinute < settingTime.getEndMinus()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }


    private void stopPush() {
        JPushInterface.stopPush(context);
    }

    private void startPush() {
        JPushInterface.resumePush(context);
    }
}
