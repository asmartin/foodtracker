package com.avidandrew.habittracker;

import java.util.ArrayList;

import com.example.first_app.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import com.avidandrew.habittracker.TabsPagerAdapter;

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

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), this);

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
				
		//set Shared preferences
		setSharedPreferences();

		// empty DB (for debugging)
		//DBHelper.emptyDB(this);
		
		// ASM: uncomment to show tabs
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

		// Adding Tabs
		actionBar.addTab(actionBar.newTab().setText(
				getResources().getString(R.string.PERIOD_NONE_LABEL)).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(
				getResources().getString(R.string.PERIOD_DAILY_LABEL)).setTabListener(this));	
		actionBar.addTab(actionBar.newTab().setText(
				getResources().getString(R.string.PERIOD_WEEKLY_LABEL)).setTabListener(this));	
		actionBar.addTab(actionBar.newTab().setText(
				getResources().getString(R.string.PERIOD_MONTHLY_LABEL)).setTabListener(this));
		
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
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
		
		viewPager.setCurrentItem(DEFAULT_TAB_INDEX);

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
	
	private void setSharedPreferences(){
		SharedPreferences sharedPref = this.getPreferences(this.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		
		//Start Shared Preferences
		if(sharedPref.getBoolean("firstrun", true)){
		
		//First Launch, set values	
			sharedPref.edit().putBoolean("firstrun", false).commit();
	
			//spinner sort order
			// 0 = Alphabetical
			// 1 = Order Added
			editor.putInt("sort_order", 0).commit();
		
			//spinner starting table
			editor.putInt("start_table", 0).commit();
			
		editor.commit();
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
}
