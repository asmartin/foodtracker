package com.example.first_app;

import java.util.ArrayList;
import java.util.List;

import android.R.style;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
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
		
		
		/**
		 * This for loop creates the buttons on the screen, one for each item 
		 */
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
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
			
			//Create the plus button
			Button plus = new Button(this);
			plus.setText("+");
			row.addView(plus);
			
			
			//Set Total
			TextView total_counter = new TextView(this);
			
			String out_of = "/" + getMaxQuantity();
			total_counter.setText(item.total_counter + out_of);
			row.addView(total_counter);
			/**
			 * Update Text if item added
			 */
			
			total_counter.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					
				}
			});
			
	
			
			//add row to view
			layout.addView(row);
		}
	}
	
	
	/**		END CREATING BUTTONS	 **/



	/**
	 * This method should return the max number of an item
	 * @return
	 */
	public int getMaxQuantity(){
		
		return 1;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * This method adds an item to the db, checks if it already exists
	 * @param item
	 */
	public void addServing(Item item) {
		
		
	}
	
	/**
	 * This method subtracts an item from the db
	 */
	public void subtractServing() { 
		
		
	}
	
	
	/**
	 * This method opens an activty to show servings for that category
	 * @param v
	 */
	public void view_Details(View v){

		Intent intent = new Intent(this, vegetables_detail_activity.class);
		startActivity(intent);
	}
	
}
