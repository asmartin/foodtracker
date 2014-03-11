package com.avidandrew.habittracker;

import com.example.first_app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class vegetables_detail_activity extends Activity {

	public vegetables_detail_activity() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.vegetables_detail_layout);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	if (item.getItemId() == android.R.id.home) {
		finish();
	}
	return super.onOptionsItemSelected(item);
}
}
 