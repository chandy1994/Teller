package com.softwareproject.chandy.teller.Bean;

import android.databinding.BaseObservable;

/**
 * Created by Chandy on 2016/12/5.
 */

public class Setting extends BaseObservable{
    private boolean alert=true;
    private boolean voice=true;
    private boolean shake=true;
    private boolean alwaysOn=true;

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isAlwaysOn() {
        return alwaysOn;
    }

    public void setAlwaysOn(boolean alwaysOn) {
        this.alwaysOn = alwaysOn;
    }

    public boolean isShake() {
        return shake;
    }

    public void setShake(boolean shake) {
        this.shake = shake;
    }

    public boolean isVoice() {
        return voice;
    }
    public void setVoice(boolean voice) {
        this.voice = voice;

    }
}
