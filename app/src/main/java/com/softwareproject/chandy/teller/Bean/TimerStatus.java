package com.softwareproject.chandy.teller.Bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by Chandy on 2016/12/3.
 */

public class TimerStatus extends BaseObservable{

    private String status;
    private Status s;
    public TimerStatus(Status s){
        this.s=s;
        setStatus(s.toString());
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(com.softwareproject.chandy.teller.BR.status);
    }

    @Bindable
    public String getStatus() {
        return status;
    }
    public enum Status{
        IDEL,DOING;
        @Override
        public String toString() {
            if(this.equals(IDEL)){
                return "确  定";
            }else {
                return "取  消";
            }
        }
    }
}