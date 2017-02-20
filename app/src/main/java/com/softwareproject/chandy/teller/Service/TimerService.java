package com.softwareproject.chandy.teller.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.softwareproject.chandy.teller.AIDL.IAlertService;
import com.softwareproject.chandy.teller.Bean.Mission;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chandy on 2016/12/4.
 */


//这个service是用来计算耗时的
public class TimerService extends Service{
    private final String TAG="TimerService";
    MyBinder myBinder=new MyBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MyBinder extends IAlertService.Stub{
        AlarmManager alarmManager;
        Intent intent;
        int totalTime;
        @Override
        public void startAlert(int time) throws RemoteException {
            totalTime=time;
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            Bundle bundle=new Bundle();
            bundle.putString("date",df.format(date));
            bundle.putInt("totaltime",time/60);
            intent=new Intent("android.intent.action.TELLER_SUCCESS");
            intent.putExtras(bundle);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager=(AlarmManager)getApplicationContext().getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,totalTime*1000+System.currentTimeMillis(),pendingIntent);
        }

        @Override
        public void stop(int curTime) throws RemoteException {
            if(-1!=curTime){
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.cancel(pendingIntent);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date();
                Bundle bundle=new Bundle();
                bundle.putString("date",df.format(date));
                bundle.putInt("totaltime",(totalTime-curTime)/60);
                bundle.putBoolean("user",true);
                Intent intent=new Intent("android.intent.action.TELLER_FAILURE");
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        }



    }
}
