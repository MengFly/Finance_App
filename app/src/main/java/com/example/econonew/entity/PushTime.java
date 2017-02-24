package com.example.econonew.entity;

import com.example.econonew.db.Saveable;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 有关时间推送的类，存储有关推送时间的设置
 * Created by mengfei on 2017/1/17.
 */
public class PushTime extends DataSupport implements Saveable{
    @Override
    public boolean saveSelf() {
        return save();
    }

    public enum WeekDay {
        MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

        int weekDay = 0;//星期对应的天数
        WeekDay(int weekDay) {
            this.weekDay = weekDay;
        }

        public int getWeekDay() {
            return weekDay;
        }
    }

    private int startHour;
    private int startMinus;

    private int endHour;
    private int endMinus;

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public String days;

    public int getStartMinus() {
        return startMinus;
    }

    public void setStartMinus(int startMinus) {
        this.startMinus = startMinus;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinus() {
        return endMinus;
    }

    public void setEndMinus(int endMinus) {
        this.endMinus = endMinus;
    }

    private List<WeekDay> dayList = new ArrayList<>(7);

    public void addWeekDay(WeekDay day) {
        if (!dayList.contains(day)) {
            dayList.add(day);
        }
        days = dayList.toString();
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void removeDay(WeekDay day) {
        if (dayList.contains(day)) {
            dayList.remove(day);
        }
        days = dayList.toString();
    }

    public List<WeekDay> getDayList() {
        if (!dayList.isEmpty()) {
            return dayList;
        }
        dayList.clear();
        if (days != null) {
            String[] dayArray = days.substring(1, days.length() - 1).split(",");
            for (String day : dayArray) {
                dayList.add(WeekDay.valueOf(day.trim()));
            }
        }
        return dayList;
    }

    /**
     * 是否包含这一天
     * @param weekDayInt
     */
    public boolean isContainWeekDay(int weekDayInt) {
        for (WeekDay weekDay : dayList) {
            if (weekDay.getWeekDay() == weekDayInt) {
                return true;
            }
        }
        return false;
    }

    public void setDayList(List<WeekDay> dayList) {
        this.dayList = dayList;
    }
}
