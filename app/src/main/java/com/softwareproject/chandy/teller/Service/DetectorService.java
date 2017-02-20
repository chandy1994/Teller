package com.softwareproject.chandy.teller.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.softwareproject.chandy.teller.AIDL.IDetectOther;
import com.softwareproject.chandy.teller.Utils.AppInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Chandy on 2016/12/6.
 */
//这个service是用来检测是否有其他app在前台运行
public class DetectorService extends Service {
    Detector detector = new Detector();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return detector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public class Detector extends IDetectOther.Stub {
        public int count=-1;
        boolean flag = false;
        public int totalSecond=0;

        @Override
        public boolean isRunningOther(final int time) throws RemoteException {
            if (-1==count&&time!=count){
                count=time;
                totalSecond=time;
            }
            if (AppInfo.getInstance().isRunningOther(getApplicationContext())||-1==time) {
                //发送失败信息
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date();
                Bundle bundle = new Bundle();
                bundle.putString("date", df.format(date));
                bundle.putInt("totaltime", (totalSecond - count) / 60);
                bundle.putBoolean("user", false);
                bundle.putString("r","runing");
                Intent intent = new Intent("android.intent.action.TELLER_FAILURE");
                intent.putExtras(bundle);
                PendingIntent fail = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, 0, fail);
                flag = true;
                count=-1;
            }
            count--;
            return flag;
        }

        @Override
        public void stop() throws RemoteException {
            count=-1;
        }

        @Override
        public int getRunTimes() throws RemoteException {
            //返回运行的秒数
            return  0;
        }


    }

}
