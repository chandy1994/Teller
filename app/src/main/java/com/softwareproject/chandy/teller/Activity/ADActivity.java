package com.softwareproject.chandy.teller.Activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.softwareproject.chandy.teller.R;


/**
 * Created by Chandy on 2016/12/4.
 */

public class ADActivity extends Activity {
    //使用静态的handler防止对activity的引用
    static Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ADActivity.this.finish();
                handler=null;
            }
        },2500);
    }
}
