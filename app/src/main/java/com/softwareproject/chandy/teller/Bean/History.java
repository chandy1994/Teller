package com.softwareproject.chandy.teller.Bean;

import java.sql.Date;

/**
 * Created by Chandy on 2016/11/26.
 */

public class History {
    private String date;
    private int totalTime;
    public History(String date,int time){
        this.date=date;
        totalTime=time;
    }
    public void setDate(String date){
        this.date=date;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public String getDate(){
        return date;
    }
}
