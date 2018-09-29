package com.example.chen.news.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by chen on 2018/9/16.
 */
public class NewsViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragments;

    public NewsViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "头条";
            case 1:
                return "社会";
            case 2:
                return "国内";
            case 3:
                return "国际";
            default:
                return null;
        }
    }
}
