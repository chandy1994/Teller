package com.softwareproject.chandy.teller.Fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softwareproject.chandy.teller.Application.MyApplication;
import com.softwareproject.chandy.teller.Bean.History;
import com.softwareproject.chandy.teller.DB.DBManager;
import com.softwareproject.chandy.teller.R;
import com.softwareproject.chandy.teller.Utils.DayOfWeek;
import com.softwareproject.chandy.teller.databinding.FragmentHistoryBinding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Chandy on 2016/12/4.
 */

public class HistoryFragment extends Fragment {
    ArrayList<History> histories;
    FragmentHistoryBinding binding;
    int initLeft = 1;
    public static int offsetDays = 0;
    ArrayList<ProgressBar> progressBars;
    ArrayList<TextView> textViews;
    static Handler handler;
    public final ObservableField<String> dateField = new ObservableField<>();
    static Date clearDate;
    ArrayList<Integer> timearray;
    Date cDate;

    @Override
    public void onStop() {
        super.onStop();
        offsetDays = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        binding.setFrag(this);
        timearray = new ArrayList<>();
        progressBars = new ArrayList<>();
        textViews = new ArrayList<>();
        initView();
        try {
            reLoadData(-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    public void initView() {
        handler = new Handler();
        progressBars.add(binding.day1pb);
        progressBars.add(binding.day2pb);
        progressBars.add(binding.day3pb);
        progressBars.add(binding.day4pb);
        progressBars.add(binding.day5pb);
        progressBars.add(binding.day6pb);
        progressBars.add(binding.day7pb);
        textViews.add(binding.time1tv);
        textViews.add(binding.time2tv);
        textViews.add(binding.time3tv);
        textViews.add(binding.time4tv);
        textViews.add(binding.time5tv);
        textViews.add(binding.time6tv);
        textViews.add(binding.time7tv);
    }

    public void toPrevious(View view) {
        try {
            reLoadData(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void toNext(View view) {
        try {
            reLoadData(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void reLoadData(int direction) throws ParseException {
        //今天是周几
        int dow = -1;
        Date date = new Date();
        Date index;
        dow = DayOfWeek.dayOfWeek(date);
        clearDate = DayOfWeek.getDayBefore(date, DayOfWeek.dayOfWeek(date));
        if (dow != -1) {
            if (-1 == direction) {
                //初始化
                index = DayOfWeek.getDayBefore(clearDate, 0);
                loadData(index);
                offsetDays=0;
            }
            if (0 == direction) {
                //previous
                offsetDays--;
                index = DayOfWeek.getDayBefore(clearDate, Math.abs(offsetDays) * 7);
                loadData(index);
            } else {
                //next
                if (offsetDays == 0) {
                    return;
                } else {
                    offsetDays++;
                    index = DayOfWeek.getDayBefore(clearDate, Math.abs(offsetDays) * 7);
                    loadData(index);

                }
            }
        }


    }

    public void reFresh(){
        try {
            reLoadData(-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void loadData(final Date firstday) {
        //TODO 异步
        handler.post(new Runnable() {
            @Override
            public void run() {
                timearray.clear();
                ArrayList<History> histories;
                DBManager dbManager = MyApplication.getDbManager();
                histories = dbManager.queryAWeek(firstday);
                for (int i = 0; i < 7; i++) {
                    timearray.add(histories.get(i).getTotalTime());
                    textViews.get(i).setText(timearray.get(i) + "分");
                }
                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
                String another = histories.get(6).getDate().toString().substring(0, 4);
                String toUser;
                if (another.equals(histories.get(0).getDate().toString().substring(0, 4))) {
                    toUser = histories.get(0).getDate().toString().substring(0, 4) + "." +
                            histories.get(0).getDate().toString().substring(4, 6) + "." + histories.get(0).getDate().toString().substring(6, 8) +
                            "——" +
                            histories.get(6).getDate().toString().substring(4, 6) + "." + histories.get(6).getDate().toString().substring(6, 8);
                } else {
                    toUser = histories.get(0).getDate().toString().substring(0, 4) + "." +
                            histories.get(0).getDate().toString().substring(4, 6) + "." + histories.get(0).getDate().toString().substring(6, 8) +
                            "——" + another + "." +
                            histories.get(6).getDate().toString().substring(4, 6) + "." + histories.get(6).getDate().toString().substring(6, 8);
                }
                dateField.set(toUser);
                for (int i = 0; i < 7; i++) {
                    progressBars.get(i).setProgress(timearray.get(i) + 20);
                    AnimationSet set = new AnimationSet(true);
                    Animation animation = new ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
                    set.addAnimation(animation);
                    set.setDuration(600);
                    progressBars.get(i).setAnimation(set);
                    set.start();
                    PropertyValuesHolder pl1 = PropertyValuesHolder.ofFloat("TranslationY",
                            0f,
                            progressBars.get(i).getHeight() * (1 - ((float) progressBars.get(i).getProgress() / (float) progressBars.get(i).getMax())));
                    ObjectAnimator.ofPropertyValuesHolder(textViews.get(i), pl1).setDuration(600).start();
                }
            }
        });
    }

    public void sunAnimation(View view) {
        int max = binding.getRoot().getDisplay().getWidth();

        if (1 == initLeft) {
            initLeft = -1;
        } else {
            initLeft = 1;
        }
        PropertyValuesHolder pl1;
        PropertyValuesHolder pl2;
        if (-1 == initLeft) {
            pl1 = PropertyValuesHolder.ofFloat("TranslationX", 0f, max / 5 * 3);
            pl2 = PropertyValuesHolder.ofFloat("rotation", 0f, 270f);
        } else {
            pl1 = PropertyValuesHolder.ofFloat("TranslationX", max / 5 * 3, 0f);
            pl2 = PropertyValuesHolder.ofFloat("rotation", 270f, 0f);
        }
        ObjectAnimator.ofPropertyValuesHolder(view, pl1, pl2).setDuration(750).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            reLoadData(-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
