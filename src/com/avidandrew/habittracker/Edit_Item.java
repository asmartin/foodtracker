package com.avidandrew.habittracker;

import com.example.first_app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class Edit_Item extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.edit_item_activity);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		String item_name = getIntent().getStringExtra("name");
		int max_quant = getIntent().getIntExtra("max", 0);
		
		 
		
		EditText item_name_field = (EditText) findViewById(R.id.edit_item_name);
		EditText max_quantity_field = (EditText) findViewById(R.id.edit_max_quantity);
		
		item_name_field.setText(item_name);
		max_quantity_field.setText("" + max_quant);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
		}

	
}
