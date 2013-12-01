package com.avidandrew.foodtracker;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FTSectionsPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;

    public FTSectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        fragmentList = fragments;
    }

@Override
public int getCount() {
    return fragmentList.size();
}

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentList.get(position);        
        return fragment;
    }
    
    
}