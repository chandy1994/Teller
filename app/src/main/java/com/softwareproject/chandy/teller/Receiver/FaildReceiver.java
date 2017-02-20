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

//用来监听失败的讯号
public class FaildReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_FLAG = 1;
    Bundle bundle;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        bundle=intent.getExtras();
        System.out.println(bundle.getString("r"));
        if(bundle.getBoolean("user")==true){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DBManager dbManager = MyApplication.getDbManager();
                    String date = bundle.getString("date");
                    int time = bundle.getInt("totaltime");
                    dbManager.handle(new History(date, time));
                }
            }).start();
            System.out.println("用户取消了");
        }else {
            SharedPreferences sharedPreferences = context.getSharedPreferences("setting", context.MODE_PRIVATE);
            boolean alert = sharedPreferences.getBoolean("alert", true);
            boolean voice = sharedPreferences.getBoolean("voice", true);
            boolean shake = sharedPreferences.getBoolean("shake", true);
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Intent openintent = new Intent();
            openintent.setClass(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openintent, PendingIntent.FLAG_CANCEL_CURRENT);
            Notification notification = new Notification.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setContentText("你刚才的任务失败了！")
                    .setContentTitle("失败")
                    .setTicker("任务失败")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            manager.notify(NOTIFICATION_FLAG, notification);
            if (alert) {
                if (shake) {
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(1000);
                }
                if (voice) {
                    //播放音乐
                }


            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DBManager dbManager = MyApplication.getDbManager();
                    String date = bundle.getString("date");
                    int time = bundle.getInt("totaltime");
                    dbManager.handle(new History(date, time));
                }
            }).start();
            System.out.println("不是用户取消了");
        }

    }
}
