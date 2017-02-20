package com.softwareproject.chandy.teller.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chandy on 2016/12/5.
 */

public class AppInfo {
    private static AppInfo appInfo;
    public ArrayList<String> packageName;
    private AppInfo(){
        packageName=new ArrayList<>();
    }
    public static AppInfo getInstance(){
        if(null==appInfo){
            appInfo=new AppInfo();
            return appInfo;
        }else{
            return appInfo;
        }
    }

    //获取非系统程序
    public ArrayList<String> getPackageName(Context context){
        PackageManager pm=context.getPackageManager();
        List<PackageInfo> packageInfoList=pm.getInstalledPackages(0);
        for(PackageInfo p:packageInfoList){
            if((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <=0){
                packageName.add(p.packageName);
                System.out.println(p.packageName);
            }
        }
        return packageName;
    }

    public boolean isRunningOther(Context context){
        ArrayList<String> names=getPackageName(context);
        ActivityManager activityManager=(ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list=activityManager.getRunningAppProcesses();
        for(String s:names){
            if(s.equals(list.get(0).processName)&&s.contains("com.softwareproject.chandy.teller")){
                return false;
            }
        }
        return true;
    }
}
