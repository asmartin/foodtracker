package com.avidandrew.habittracker;


import com.example.first_app.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Settings_Activity extends Activity implements OnItemSelectedListener {

	Spinner spinner_sort_order;
	//Shared preferences
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Set Layout File
		setContentView(R.layout.settings_activity);
		//Set back button
		getActionBar().setDisplayHomeAsUpEnabled(true);

		/// This code probably belongs on the first activity

		sharedPref = this.getPreferences(this.MODE_PRIVATE);
		editor = sharedPref.edit();



		//////////////////END 

		//				Sort Order Spinner Code

		spinner_sort_order = (Spinner) findViewById(R.id.spinner_sort_order);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> sort_order_adapter = ArrayAdapter.createFromResource(this,
				R.array.sort_order  , android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		sort_order_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner_sort_order.setAdapter(sort_order_adapter);
		spinner_sort_order.setOnItemSelectedListener(this);
		spinner_sort_order.setSelection(sharedPref.getInt("sort_order", 0));

		// 				Starting Table Spinner Code

		Spinner starting_table_spinner = (Spinner) findViewById(R.id.starting_table_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> starting_table_adapter = ArrayAdapter.createFromResource(this,
				R.array.starting_table_spinner , android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		starting_table_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		starting_table_spinner.setAdapter(starting_table_adapter);
		starting_table_spinner.setOnItemSelectedListener(this);
		starting_table_spinner.setSelection(sharedPref.getInt("start_table", 0));


	}

	//This method returns to previews activity if back button pressed
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) { finish();}
		return super.onOptionsItemSelected(item);
	}

	public void onItemSelected(AdapterView<?> parent, View view, 
			int pos, long id) 
	{
		// An item was selected. You can retrieve the selected item using
		// parent.getItemAtPosition(pos)

		switch(parent.getId()){
		case R.id.spinner_sort_order:

			editor.putInt("sort_order", pos).commit();
			break;

		case R.id.starting_table_spinner:
			editor.putInt("start_table", pos).commit();

		default:
			break;
		}

	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback

		Toast.makeText(this, "no item selected", Toast.LENGTH_SHORT).show();
	}

}
