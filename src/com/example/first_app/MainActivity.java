package com.example.first_app;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		List<Item> items = new Items_Data().getItems();
		TableLayout layout = (TableLayout) findViewById(R.id.main_table);
		
		
		//This creates the buttons 
		for (final Item item : items) {
			
			TableRow row = new TableRow(this);
			
			//Create Minus button
			Button minus = new Button(this);
			minus.setText("-");
			row.addView(minus);
			minus.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					subtractServing();
					
				}
			});
			
			//Create Item Button
			Button button = new Button(this);
			button.setText(item.item_name);
			row.addView(button);
			
			//create the plus button
			Button plus = new Button(this);
			plus.setText("+");
			row.addView(plus);
			
			//Breakfast
			TextView breakfast_counter = new TextView(this);
			//CHANGE INT TO STRING
			breakfast_counter.setText(Integer.toString(item.breakfast_counter));
			row.addView(breakfast_counter);
			
			//Lunch Counter
			TextView lunch_counter = new TextView(this);
			lunch_counter.setText(Integer.toString(item.lunch_counter));
			row.addView(lunch_counter);
			
			//Dinner Counter
			TextView dinner_counter = new TextView(this);
			dinner_counter.setText(Integer.toString(item.dinner_counter));
			row.addView(dinner_counter);
			
			//Set Total
			TextView total_counter = new TextView(this);
			String out_of = "/11";
			total_counter.setText(item.total_counter + out_of);
			row.addView(total_counter);
	
			
			//add row to view
			layout.addView(row);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void addServing(Item item) {
		
		
		
	}
	
	public void subtractServing() { }
	
	public void view_Details(View v){

		Intent intent = new Intent(this, vegetables_detail_activity.class);
		startActivity(intent);
	}
	
}
