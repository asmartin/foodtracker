package com.avidandrew.habittracker;


import com.avidandrew.habittracker.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class About_Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}




}
