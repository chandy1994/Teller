package com.softwareproject.chandy.teller.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;

import com.softwareproject.chandy.teller.Activity.MainActivity;
import com.softwareproject.chandy.teller.Application.MyApplication;
import com.softwareproject.chandy.teller.Bean.History;
import com.softwareproject.chandy.teller.DB.DBManager;
import com.softwareproject.chandy.teller.R;


/**
 * Created by Chandy on 2016/12/6.
 */


//广播接受者 接受来自AlermManager的广播
//收到广播后我们需要根据我们的设置来提示Notification
public class SuccessReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_FLAG = 1;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("setting", context.MODE_PRIVATE);
        boolean alert=sharedPreferences.getBoolean("alert",true);
        boolean voice=sharedPreferences.getBoolean("voice",true);
        boolean shake=sharedPreferences.getBoolean("shake",true);
        NotificationManager manager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent openintent=new Intent();
        openintent.setClass(context, MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,openintent,PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification=new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setContentText("刚刚有个任务完成了！！")
                .setContentTitle("成功")
                .setTicker("完成任务")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(NOTIFICATION_FLAG, notification);
        if(alert){
            if(shake){
                Vibrator vibrator=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
            }
            if(voice){
                //播放音乐
            }


        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager dbManager= MyApplication.getDbManager();
                Bundle bundle=intent.getExtras();
                String date=bundle.getString("date");
                int time=bundle.getInt("totaltime");
                dbManager.handle(new History(date,time));
            }
        }).start();
    }
}
