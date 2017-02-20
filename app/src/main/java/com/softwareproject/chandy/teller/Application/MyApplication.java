package com.softwareproject.chandy.teller.Application;

import android.app.Application;
import android.content.SharedPreferences;

import com.softwareproject.chandy.teller.Bean.History;
import com.softwareproject.chandy.teller.Bean.Setting;
import com.softwareproject.chandy.teller.DB.DBManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chandy on 2016/12/activity_main.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;
    private static Setting setting;
    private static DBManager dbManager;

    @Override
    public void onCreate() {
        super.onCreate();
        setting = new Setting();
        myApplication = this;
        dbManager = new DBManager(this);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        dbManager.handle(new History(df.format(date), 0));

        reLoadSetting();
    }

    public static DBManager getDbManager() {
        return dbManager;
    }

    public static Setting getSetting() {
        return setting;
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }

    public void reLoadSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences("setting", getApplicationContext().MODE_PRIVATE);
        setting.setAlert(sharedPreferences.getBoolean("alert", true));
        setting.setVoice(sharedPreferences.getBoolean("voice", true));
        setting.setShake(sharedPreferences.getBoolean("shake", true));
        setting.setAlwaysOn(sharedPreferences.getBoolean("alwaysOn", true));
    }


}
