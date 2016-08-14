package com.zhuoxin.videonews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Able on 2016/8/14.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentAll;

    public MainViewPagerAdapter(FragmentManager fm) {

        super(fm);

    }

    public MainViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentAll) {

        super(fm);

        this.fragmentAll = fragmentAll;

    }

    @Override
    public Fragment getItem(int position) {

        return fragmentAll.get(position);

    }

    @Override
    public int getCount() {

        return fragmentAll.size();

    }

}
