package com.avidandrew.habittracker;


import com.example.first_app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Settings_Activity extends Activity implements OnItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Set Layout File
		setContentView(R.layout.settings_activity);
		//Set back button
		getActionBar().setDisplayHomeAsUpEnabled(true);


		//				Sort Order Spinner Code

		Spinner sort_order_spinner = (Spinner) findViewById(R.id.sort_order_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> sort_order_adapter = ArrayAdapter.createFromResource(this,
				R.array.sort_order  , android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		sort_order_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sort_order_spinner.setAdapter(sort_order_adapter);


		// 				Starting Table Spinner Code

		Spinner starting_table_spinner = (Spinner) findViewById(R.id.starting_table_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> starting_table_adapter = ArrayAdapter.createFromResource(this,
				R.array.starting_table_spinner , android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		starting_table_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		starting_table_spinner.setAdapter(starting_table_adapter);


	}

	//This method returns to previews activity if back button pressed
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) { finish();}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
