package com.softwareproject.chandy.teller.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.softwareproject.chandy.teller.Application.MyApplication;
import com.softwareproject.chandy.teller.Bean.History;
import com.softwareproject.chandy.teller.Utils.DayOfWeek;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Chandy on 2016/11/26.
 */

public class DBManager {
    private Context mcontext;
    private DBHelper helper;
    private SQLiteDatabase db;
    private History history;
    private ArrayList<History> list;
    private int oldtime = 0;

    public DBManager(Context context) {
        mcontext = context;
        helper = new DBHelper(mcontext);
        list = new ArrayList<>();
        db = helper.getWritableDatabase();
    }

    private void add(History history) {
        ContentValues cv = new ContentValues();
        cv.put("date", history.getDate());
        cv.put("totaltime", history.getTotalTime());
        System.out.println("new time is " + history.getTotalTime());
        db.replace("History", null, cv);


    }

    private void update(History history) {
        ContentValues cv = new ContentValues();
        cv.put("totaltime", oldtime + history.getTotalTime());
        System.out.println("old time is " + oldtime);
        System.out.println("new time is " + history.getTotalTime());
        db.update("History", cv, "date = ?", new String[]{history.getDate()});
        oldtime = 0;
    }

    private boolean isIn(History history) {
        query();
        for (History h : list) {
            if (h.getDate().equals(history.getDate())) {
                oldtime = h.getTotalTime();
                return true;
            }
        }
        return false;
    }

    public ArrayList<History> query() {
        synchronized (this) {
            list.clear();
            Cursor c = db.rawQuery("SELECT * FROM History", null);
            c.moveToFirst();
            while (c.moveToNext()) {
                History history = new History(c.getString(c.getColumnIndex("date")), c.getInt(c.getColumnIndex("totaltime")));
                list.add(history);
            }
            c.close();
            return list;
        }
    }

    public History queryToday(String today) {
        synchronized (this) {
            query();
            for (History h : list) {
                if (h.getDate().equals(today)) {
                    return h;
                }
            }
            return null;
        }
    }

    public ArrayList<History> queryAWeek(Date firstDay) {
        synchronized (this) {
            query();
            ArrayList<History> histories = new ArrayList<>();
            ArrayList<String> strings = DayOfWeek.getAWeekDate(firstDay);
            for (String s : strings) {
                History history = queryToday(s);
                if (null != history) {
                    histories.add(history);
                } else {
                    histories.add(new History(s, 0));
                }
            }
            return histories;
        }
    }


    public void closeDB() {
        db.close();
    }

    public void handle(History history) {
        synchronized (this) {
            if (isIn(history)) {
                update(history);
            } else {
                add(history);
            }
        }
    }

    public void dropTable() {
        db.execSQL("delete from History");
    }


}


