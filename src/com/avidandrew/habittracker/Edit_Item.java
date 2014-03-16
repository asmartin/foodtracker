package com.avidandrew.habittracker;

import com.example.first_app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Edit_Item extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.edit_item_activity);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Get Items from previous Activity
		String item_name = getIntent().getStringExtra("name");
		int max_quant = getIntent().getIntExtra("max", 0);
		
		//Get Fields to edit
		EditText item_name_field = (EditText) findViewById(R.id.edit_item_name);
		EditText max_quantity_field = (EditText) findViewById(R.id.edit_max_quantity);
		
		//Update text for those fields
		item_name_field.setText(item_name);
		max_quantity_field.setText(String.valueOf(max_quant));
		
		//On Click listener for the save changes button
		 Button save_button = (Button) findViewById(R.id.save_changes);
		 save_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				
				
			}
		});
		 
		//On Click Listener for the Delete Item button
		 
		 Button delete_item = (Button) findViewById(R.id.delete_item);
		 delete_item.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		 
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
		}

	
}
