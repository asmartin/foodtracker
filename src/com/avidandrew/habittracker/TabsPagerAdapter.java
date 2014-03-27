package com.avidandrew.habittracker;

import java.util.Locale;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import static com.avidandrew.habittracker.Constants.*;

public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return new ItemsFragment(PERIOD_NONE);
        case 1:
            // Games fragment activity
            return new ItemsFragment(PERIOD_DAILY);
        case 2:
            // Movies fragment activity
            return new ItemsFragment(PERIOD_WEEKLY);
        case 3:
            // Movies fragment activity
            return new ItemsFragment(PERIOD_MONTHLY);
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
 
    @Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return PERIOD_NONE.toUpperCase(l);
		case 1:
			return PERIOD_DAILY.toUpperCase(l);
		case 2:
			return PERIOD_WEEKLY.toUpperCase(l);
		case 3:
			return PERIOD_MONTHLY.toUpperCase(l);			
		}
		return null;
	}
}