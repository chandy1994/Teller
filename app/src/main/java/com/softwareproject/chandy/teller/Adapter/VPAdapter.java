package com.softwareproject.chandy.teller.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Chandy on 2016/12/4.
 */

public class VPAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> lists;
    public VPAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.lists=list;
    }

    @Override
    public Fragment getItem(int position) {
        return lists.get(position);
    }

    @Override
    public int getCount() {
        return lists.size();
    }
}
