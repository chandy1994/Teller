package com.softwareproject.chandy.teller.Activity;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.softwareproject.chandy.teller.Adapter.VPAdapter;
import com.softwareproject.chandy.teller.Adapter.VPTransformer;
import com.softwareproject.chandy.teller.Application.MyApplication;
import com.softwareproject.chandy.teller.Fragment.HistoryFragment;
import com.softwareproject.chandy.teller.Fragment.SubmitFragment;
import com.softwareproject.chandy.teller.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Fragment> fragments;
    ViewPager viewPager;
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,ADActivity.class);
        startActivity(intent);
    }

    private void initView(){
        powerManager = (PowerManager) this.getSystemService(Service.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Lock");
        sharedPreferences=getSharedPreferences("setting",MODE_PRIVATE);
        wakeLock.setReferenceCounted(false);
        viewPager=(ViewPager)findViewById(R.id.vp);
        fragments=new ArrayList<>();
        SubmitFragment submitFragment=new SubmitFragment();
        final HistoryFragment historyFragment=new HistoryFragment();
        fragments.add(submitFragment);
        fragments.add(historyFragment);
        viewPager.setAdapter(new VPAdapter(getSupportFragmentManager(),fragments));
        viewPager.setCurrentItem(0);
        viewPager.setPageTransformer(true,new VPTransformer());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
            }

            @Override
            public void onPageSelected(int position) {
                if(1==position){
                    historyFragment.reFresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sharedPreferences.getBoolean("alwayson",true)){
            wakeLock.acquire();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(sharedPreferences.getBoolean("alwayson",true)){
            wakeLock.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getDbManager().closeDB();
    }
}
