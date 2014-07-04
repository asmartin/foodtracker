package com.avidandrew.habittracker;

import java.util.Locale;

import com.avidandrew.habittracker.R;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import static com.avidandrew.habittracker.Constants.*;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	private Activity activity = null;
	
    public TabsPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        
        this.activity = activity;
    }
 
    @Override
    public Fragment getItem(int index) {
    	ItemsFragment f = new ItemsFragment();
    	f.setPeriod(index);
    	return f;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
 
    public String getLabel(int period) {
		String label = "";

		switch (period) {
		case PERIOD_NONE:
			label = activity.getResources().getString(R.string.PERIOD_NONE_LABEL);
			break;
		case PERIOD_DAILY:
			label = activity.getResources().getString(R.string.PERIOD_DAILY_LABEL);
			break;
		case PERIOD_WEEKLY:
			label = activity.getResources().getString(R.string.PERIOD_WEEKLY_LABEL);
			break;
		case PERIOD_MONTHLY:
			label = activity.getResources().getString(R.string.PERIOD_MONTHLY_LABEL);
			break;
		}
		
		return label;
	}
    
    @Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
        case 0:
            // No Period
            return getLabel(PERIOD_NONE).toUpperCase(l);
        case 1:
            // Daily
            return getLabel(PERIOD_DAILY).toUpperCase(l);
        case 2:
            // Weekly
            return getLabel(PERIOD_WEEKLY).toUpperCase(l);
        case 3:
            // Monthly
            return getLabel(PERIOD_MONTHLY).toUpperCase(l);
        }
		return "";
	}
}