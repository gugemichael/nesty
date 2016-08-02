package org.nesty.commons.utils;

import java.util.Calendar;

public class Time {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int microSecond;

    private Time() {
    }

    public int getYear() {
        return year;
    }

    public Time setYear(int year) {
        this.year = year;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public Time setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getDay() {
        return day;
    }

    public Time setDay(int day) {
        this.day = day;
        return this;
    }

    public int getHour() {
        return hour;
    }

    public Time setHour(int hour) {
        this.hour = hour;
        return this;
    }

    public int getMinute() {
        return minute;
    }

    public Time setMinute(int minute) {
        this.minute = minute;
        return this;
    }

    public int getSecond() {
        return second;
    }

    public Time setSecond(int second) {
        this.second = second;
        return this;
    }

    public int getMicroSecond() {
        return microSecond;
    }

    public Time setMicroSecond(int microSecond) {
        this.microSecond = microSecond;
        return this;
    }

    public boolean diffDay(Time t) {
        return t == null || t.year != year || t.month != month || t.day != day;
    }

    public boolean diffMiniute(Time t) {
        return t == null || t.year != year || t.month != month || t.day != day || t.hour != hour || t.minute != minute;
    }

    public boolean diffSecond(Time t) {
        return t == null || t.year != year || t.month != month || t.day != day || t.hour != hour || t.minute != minute || t.second != second;
    }

    @Override
    public String toString() {
        return String.format("%d-%d-%d %d:%d:%d.%d", year, month, day, hour, minute, second, microSecond);
    }

    public static Time fetch() {
        Calendar calender = Calendar.getInstance();
        return new Time().setYear(calender.get(Calendar.YEAR)).setMonth(calender.get(Calendar.MONTH) + 1)
                .setDay(calender.get(Calendar.DAY_OF_MONTH)).setHour(calender.get(Calendar.HOUR_OF_DAY))
                .setMinute(calender.get(Calendar.MINUTE)).setSecond(calender.get(Calendar.SECOND))
                .setMicroSecond(calender.get(Calendar.MILLISECOND));
    }

    /**
     * return current unix timestamp seconds
     *
     * @return unix timestamp
     */
    public static long getUnixStamp() {
        return System.currentTimeMillis() / 1000L;
    }
}
