package com.softwareproject.chandy.teller.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Chandy on 2016/12/6.
 */

public class DayOfWeek {
    public static DayOfWeek dayOfWeek;

    private DayOfWeek() {
        dayOfWeek = new DayOfWeek();
    }

    public DayOfWeek getInstance() {
        if (null != dayOfWeek) {
            return dayOfWeek;
        } else {
            dayOfWeek = new DayOfWeek();
            return dayOfWeek;
        }
    }

    public static int dayOfWeek(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(format.format(date)));
        int dayForWeek = 0;
        dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        return dayForWeek;
    }

    public static String getDateBefore(Date d, int day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return format.format(now.getTime());
    }

    public static Date getDayBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    public static String getDateAfter(Date d, int day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return format.format(now.getTime());
    }

    public static Date getDayAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }
    public static ArrayList<String> getAWeekDate(Date firstdate){
        ArrayList<String> strings=new ArrayList<>();
        for(int i=0;i<7;i++){
            strings.add(getDateAfter(firstdate,i));
        }
        return strings;
    }
}
