package com.softwareproject.chandy.teller.Fragment;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwareproject.chandy.circleseekbar.CircleSeekBar;
import com.softwareproject.chandy.teller.AIDL.IAlertService;
import com.softwareproject.chandy.teller.AIDL.IDetectOther;
import com.softwareproject.chandy.teller.Activity.SettingActivity;
import com.softwareproject.chandy.teller.Application.MyApplication;
import com.softwareproject.chandy.teller.Bean.History;
import com.softwareproject.chandy.teller.Bean.HttpResult;
import com.softwareproject.chandy.teller.Bean.Mission;
import com.softwareproject.chandy.teller.Bean.TimerStatus;
import com.softwareproject.chandy.teller.Bean.Weather;
import com.softwareproject.chandy.teller.DB.DBManager;
import com.softwareproject.chandy.teller.HttpMethod.HttpManager;
import com.softwareproject.chandy.teller.R;
import com.softwareproject.chandy.teller.Service.DetectorService;
import com.softwareproject.chandy.teller.Service.TimerService;
import com.softwareproject.chandy.teller.Utils.AppInfo;
import com.softwareproject.chandy.teller.databinding.FragmentSubmitBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;


/**
 * Created by Chandy on 2016/12/4.
 */

public class SubmitFragment extends Fragment {
    FragmentSubmitBinding binding;
    //这个是任务的实体类
    Mission mission;
    //这个是任务时钟的状态实体类
    public TimerStatus timerStatus;
    static Handler handler;
    History todayRecord;
    Subscriber<HttpResult> subscriber;
    public final ObservableField<String> todayTime = new ObservableField<>();
    Weather weather;
    boolean stubInit = false;
    IDetectOther detectorService;
    IAlertService timerService;
    boolean startService = false;
    float waveHeight = 0;

    Timer t;
    Intent timerIntent = new Intent();
    Intent detectorIntent = new Intent();


    //两个ServiceConnection
    ServiceConnection TimerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            timerService = TimerService.MyBinder.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    ServiceConnection DetectorServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            detectorService = DetectorService.Detector.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_submit, container, false);
        binding.setFrag(this);
        mission = new Mission(20);
        binding.setMission(mission);
        timerStatus = new TimerStatus(TimerStatus.Status.IDEL);
        binding.setStatus(timerStatus);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (null != todayRecord) {
                        todayTime.set("你今天集中注意力" + todayRecord.getTotalTime() + "分钟");
                    } else {
                        todayTime.set("你今天集中注意力" + 0 + "分钟");
                    }
                }
                if (msg.what == 2) {
                    if (0 == mission.second) {
                        timerStatus.setStatus(TimerStatus.Status.IDEL.toString());
                        PropertyValuesHolder pl1 = PropertyValuesHolder.ofFloat("Alpha", 0f, 1f);
                        PropertyValuesHolder pl2 = PropertyValuesHolder.ofFloat("ScaleX", 0f, 1f);
                        PropertyValuesHolder pl3 = PropertyValuesHolder.ofFloat("ScaleY", 0f, 1f);
                        ObjectAnimator.ofPropertyValuesHolder(binding.circle, pl1, pl2, pl3).setDuration(600).start();
                        binding.circle.setEnable(true);
                        binding.wave.setAccelerate(false);
                        cancelClock();
                        startService = false;
                        binding.wave.setWaveHeightDelta((waveHeight + binding.wave.getHeight() * (float) 2 / 9));
                    } else {
                        mission.second--;
                        if ((mission.second % 60 + "").length() == 1) {
                            mission.setTimeNows(mission.second / 60 + ":0" + (mission.second % 60));
                        } else {
                            mission.setTimeNows(mission.second / 60 + ":" + (mission.second % 60));
                        }
                        binding.wave.setWaveHeightDelta((waveHeight + binding.wave.getHeight() * (float) 2 / 9)
                                * ((float) mission.second / mission.getTimeTarget() / 60));
                    }
                }
            }
        };

        //订阅者
        subscriber = new Subscriber<HttpResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult httpResult) {
                if (null != weather) {
                    stubInit = true;
                } else {
                    weather = new Weather();
                }
                weather.setHigh(httpResult.getData().getForecast().get(0).getHigh().substring(2));
                weather.setLow(httpResult.getData().getForecast().get(0).getLow().substring(2));
                weather.setTemp(httpResult.getData().getWendu() + "℃");
                weather.setType(httpResult.getData().getForecast().get(0).getType());
                Drawable drawable = getResources().getDrawable(R.mipmap.sunny);
                if (weather.getType().contains("阴") || weather.getType().contains("多云")) {
                    drawable = getResources().getDrawable(R.mipmap.cloud);
                }
                if (weather.getType().contains("雪")) {
                    drawable = getResources().getDrawable(R.mipmap.snow);
                }
                if (weather.getType().contains("雨")) {
                    drawable = getResources().getDrawable(R.mipmap.rain);
                }
                if (null != weather.getTemp()) {
                    if (!stubInit) {
                        ((ViewStub) binding.getRoot().findViewById(R.id.weather)).inflate();
                    }
                    ((ImageView) binding.getRoot().findViewById(R.id.weather_type)).setImageDrawable(drawable);
                    ((TextView) binding.getRoot().findViewById(R.id.temp)).setText(weather.getTemp());
                    ((TextView) binding.getRoot().findViewById(R.id.temps)).setText(weather.getLow() + "——" + weather.getHigh());

                    PropertyValuesHolder pl1 = PropertyValuesHolder.ofFloat("ScaleX", 0f, 1f);
                    PropertyValuesHolder pl2 = PropertyValuesHolder.ofFloat("ScaleY", 0f, 1f);
                    PropertyValuesHolder pl3 = PropertyValuesHolder.ofFloat("Alpha", 0f, 1f);
                    ObjectAnimator.ofPropertyValuesHolder(binding.weathertaker, pl1, pl2, pl3).setDuration(600).start();
                }

            }
        };
        //HttpManager.getInstance().getWeather(subscriber);
        initView();


        AppInfo.getInstance().getPackageName(SubmitFragment.this.getContext());
        return binding.getRoot();
    }

    public void imitateClock(int time) {
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessage(message);
                try {
                    detectorService.isRunningOther(mission.second);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 1000);
    }

    public void cancelClock() {
        if (null != t) {
            t.cancel();
        }
        mission.setTimeNows(mission.getTimeTarget() + ":00");
    }

    public void submit(View view) {
        //这个是点击了确定
        if (timerStatus.getStatus().equals(TimerStatus.Status.IDEL.toString())) {
            timerStatus.setStatus(TimerStatus.Status.DOING.toString());
            PropertyValuesHolder pl1 = PropertyValuesHolder.ofFloat("Alpha", 1f, 0f);
            PropertyValuesHolder pl2 = PropertyValuesHolder.ofFloat("ScaleX", 1f, 0.8f);
            PropertyValuesHolder pl3 = PropertyValuesHolder.ofFloat("ScaleY", 1f, 0.8f);
            ObjectAnimator.ofPropertyValuesHolder(binding.circle, pl1, pl2, pl3).setDuration(600).start();
            binding.circle.setEnable(false);
            binding.wave.setAccelerate(true);
            
            try {
                timerService.startAlert(mission.second);
                startService = true;
                imitateClock(mission.getTimeTarget());

            } catch (RemoteException e) {
                e.printStackTrace();
            }
            todayTime.set("打起十二分精神来吧！");


            //修改
        } else {
            //点击了取消
            timerStatus.setStatus(TimerStatus.Status.IDEL.toString());
            PropertyValuesHolder pl1 = PropertyValuesHolder.ofFloat("Alpha", 0f, 1f);
            PropertyValuesHolder pl2 = PropertyValuesHolder.ofFloat("ScaleX", 0.8f, 1f);
            PropertyValuesHolder pl3 = PropertyValuesHolder.ofFloat("ScaleY", 0.8f, 1f);
            ObjectAnimator.ofPropertyValuesHolder(binding.circle, pl1, pl2, pl3).setDuration(600).start();
            binding.circle.setEnable(true);
            binding.wave.setAccelerate(false);

            if (startService) {
                //获取运行了多少秒
                try {
                    startService = false;
                    timerService.stop(mission.second);
                    detectorService.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
            mission.seekTo(mission.getTimeTarget());
            cancelClock();
            queryToday();

            binding.wave.setWaveHeightDelta((waveHeight + binding.wave.getHeight() * (float) 2 / 9));
        }
    }

    private void initView() {
        timerIntent.setClass(getContext(), TimerService.class);
        detectorIntent.setClass(getContext(), DetectorService.class);
        binding.circle.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(int progress) {
                int delta = progress / 5;
                if (delta % 5 == 0) {
                    mission.seekTo(Mission.init + delta);
                }
                waveHeight = (float) 7 / 9 * binding.wave.getHeight() * ((float) binding.circle.getProgress() / binding.circle.getMaxProgress());
                binding.wave.setWaveHeightDelta(binding.wave.getHeight() * (float) 2 / 9 + waveHeight);
                binding.wave.setStable(false);
            }

            @Override
            public void onStartTrackingTouch() {

            }

            @Override
            public void onStopTrackingTouch() {
                binding.wave.setStable(true);

            }
        });

        binding.setting.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AnimationSet animationSet = new AnimationSet(true);
                RotateAnimation rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f
                        , Animation.RELATIVE_TO_SELF, 0.5f);
                animationSet.addAnimation(rotateAnimation);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.35f, 1f, 1.35f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animationSet.addAnimation(scaleAnimation);
                animationSet.setDuration(1500);
                binding.setting.startAnimation(animationSet);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent();
                        intent.setClass(SubmitFragment.this.getContext(), SettingActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                return false;
            }
        });

        getContext().startService(timerIntent);
        getContext().startService(detectorIntent);
        getContext().bindService(timerIntent, TimerServiceConnection, Context.BIND_AUTO_CREATE);
        getContext().bindService(detectorIntent, DetectorServiceConnection, Context.BIND_AUTO_CREATE);
        queryToday();


    }

    public void queryToday() {
        /**
         * 异步
         */
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date();
                DBManager dbManager = MyApplication.getDbManager();
                todayRecord = dbManager.queryToday(df.format(date));

                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
            }
        }.start();
    }


    public void setting(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        animationSet.addAnimation(rotateAnimation);
        animationSet.setDuration(500);
        binding.setting.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent();
                intent.setClass(SubmitFragment.this.getContext(), SettingActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        HttpManager.getInstance().getWeather(subscriber);
        mission.seekTo(mission.getTimeTarget());
        timerStatus.setStatus(TimerStatus.Status.IDEL.toString());
        PropertyValuesHolder pl1 = PropertyValuesHolder.ofFloat("Alpha", 0f, 1f);
        PropertyValuesHolder pl2 = PropertyValuesHolder.ofFloat("ScaleX", 0f, 1f);
        PropertyValuesHolder pl3 = PropertyValuesHolder.ofFloat("ScaleY", 0f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(binding.circle, pl1, pl2, pl3).setDuration(10).start();
        binding.circle.setEnable(true);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PropertyValuesHolder pl1 = PropertyValuesHolder.ofFloat("Alpha", 0f, 1f);
                PropertyValuesHolder pl2 = PropertyValuesHolder.ofFloat("ScaleX", 0f, 1f);
                PropertyValuesHolder pl3 = PropertyValuesHolder.ofFloat("ScaleY", 0f, 1f);
                ObjectAnimator.ofPropertyValuesHolder(binding.wave, pl1, pl2, pl3).setDuration(500).start();
                binding.wave.setWaveHeightDelta(binding.wave.getHeight() * 2 / 9 + waveHeight);
                queryToday();

            }
        }, 200);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (startService) {
            //获取运行了多少秒
            try {
                detectorService.isRunningOther(-1);
                startService = false;
                timerService.stop(-1);
                detectorService.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        cancelClock();
        System.out.println("onStop");

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (startService) {
            startService = false;
            try {
                timerService.stop(mission.second);
                detectorService.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }
}
