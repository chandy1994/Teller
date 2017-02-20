package com.softwareproject.chandy.teller.Bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.softwareproject.chandy.teller.BR;


/**
 * Created by Chandy on 2016/12/3.
 */

public class Mission extends BaseObservable {
    private int timeTarget;

    private String timeNows;
    private int timeNow;
    public static final int init = 20;
    public int second;

    public Mission(int timeTarget) {
        this.timeNow = timeTarget;
        this.timeNows = timeTarget + ":00";
        this.timeTarget = timeTarget;
        second = timeTarget * 60;
    }

    public int getTimeNow() {
        return timeNow;
    }

    public int getTimeTarget() {
        return timeTarget;
    }

    @Bindable
    public String getTimeNows() {
        return timeNows;
    }

    public void setTimeNow(int timeNow) {
        this.timeNow = timeNow;
    }

    public void setTimeNows(String timeNows) {
        this.timeNows = timeNows;
        notifyPropertyChanged(BR.timeNows);
    }

    public void setTimeTarget(int timeTarget) {
        this.timeTarget = timeTarget;
    }

    public void seekTo(int target) {
        setTimeNow(target);
        setTimeNows(target + ":00");
        setTimeTarget(target);
        second = target * 60;
    }

    
}
