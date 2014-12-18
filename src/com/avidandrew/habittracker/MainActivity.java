package com.avidandrew.habittracker;


import java.util.Locale;

import com.avidandrew.habittracker.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static com.avidandrew.habittracker.Constants.*;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



		// INITIALIZE ADAPTER AND VIEWPAGER
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), this);
		viewPager.setAdapter(mAdapter);

		//LOAD SHARED PREFERENCES
		setSharedPreferences();

		//Set current fragment to display
		viewPager.setCurrentItem(sharedPref.getInt("start_table", actionBar.getSelectedNavigationIndex()));
		
		//SETUP ACTIONBAR
		actionBar.setHomeButtonEnabled(false);

		// empty DB (for debugging)
		//DBHelper.emptyDB(this);

		// ASM: uncomment to show tabs
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
		//Toast.makeText(this, String.valueOf(sharedPref.getInt("start_table", DEFAULT_TAB_INDEX)) , Toast.LENGTH_SHORT).show();
		// Adding Tabs

		/*
		actionBar.addTab(actionBar.newTab().setText(
				getResources().getString(R.string.PERIOD_NONE_LABEL)).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(
				getResources().getString(R.string.PERIOD_DAILY_LABEL)).setTabListener(this));	
		actionBar.addTab(actionBar.newTab().setText(
				getResources().getString(R.string.PERIOD_WEEKLY_LABEL)).setTabListener(this));	
		actionBar.addTab(actionBar.newTab().setText(
				getResources().getString(R.string.PERIOD_MONTHLY_LABEL)).setTabListener(this));
		 */


		//POSSIBLE UNNECESSSARY CODE BELOW
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		/*
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				//actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		 */


	}


	@Override
	protected void onResume() {
		super.onResume();
		//Refresh fragments in case new items added
		mAdapter.notifyDataSetChanged();


	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Global intent variable
		Intent intent;

		switch (item.getItemId()) {
		case R.id.action_settings:

			intent = new Intent(this, Settings_Activity.class);
			startActivity(intent);
			return true;

		case R.id.import_db:
			ImportHelper ih = new ImportHelper(this);
			ih.selectFile();
			return true;			

		case R.id.export_db:
			ExportHelper eh = new ExportHelper(this);
			eh.export();
			return true;

		case R.id.add_item:
			intent = new Intent(this, Add_Item_Activity.class);
			startActivity(intent);
			return true;

		case R.id.about_actvity:
			intent = new Intent(this, About_Activity.class);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	
	/*
	 * This method sets up initial settings of the application
	 */
	private void setSharedPreferences(){


		//sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		editor = sharedPref.edit();

		//Start Shared Preferences
		if(sharedPref.getBoolean("firstrun", true)){

			Toast.makeText(this, "First Launch", Toast.LENGTH_SHORT).show();

			//First Launch, set values	
			editor.putBoolean("firstrun", false).commit();

			//spinner sort order
			// 0 = Alphabetical
			// 1 = Order Added
			editor.putInt("sort_order", 0).commit();

			//Set Starting Table
			editor.putInt("start_table", DEFAULT_TAB_INDEX).commit();
		}

	}

	//This creates the menu items
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}


	/**
	 * Tabs Pager Adapter - Extends the adapter 											View Pager Adapter
	 * @author emmanuel
	 *
	 */
	public class TabsPagerAdapter extends FragmentPagerAdapter {
		private Activity activity;

		public TabsPagerAdapter(FragmentManager fm, Activity activity) {
			super(fm);

			this.activity = activity;
		}

		/**
		 * This method is used to make sure the notifydatasetchanged actually updates the
		 * viewpager after adding an item
		 */
		public int getItemPosition(Object object) {  
			return POSITION_NONE;  
		} 

		@Override
		public Fragment getItem(int index) {

			ItemsFragment fragment = new ItemsFragment();
			Bundle args = new Bundle();
			args.putInt(EXTRA_FRAGMENT_ID, index);
			fragment.setArguments(args);

			//f.setPeriod(index);
			return fragment;

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
}
