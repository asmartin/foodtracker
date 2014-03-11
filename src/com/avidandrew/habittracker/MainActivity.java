package com.avidandrew.habittracker;

import java.util.ArrayList;
import java.util.List;

import com.example.first_app.R;

import android.R.style;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
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
		
		List<Item> items = new Items_Data(this).getItems();
		TableLayout layout = (TableLayout) findViewById(R.id.main_table);
		
		
	//////////
		
		DBHelper items_database = new DBHelper(this);
		
		SQLiteDatabase db = items_database.getWritableDatabase();
		
	
		
		/** TODO
		 * read in item list from XML, csv, etc
		 * create new Item object for each; item object should include interface
		 */
		
		/**
		 * This for loop creates the buttons on the screen, one for each item 
		 */
		for (final Item item : items) {
			TableRow row = new ItemView(this, item);
			
			//add row to view
			layout.addView(row);
		}
	}
	
	
	//This creates the menu items
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * This method opens an activity to show servings for that category
	 * @param v
	 */
	public void view_Details(View v){

		Intent intent = new Intent(this, vegetables_detail_activity.class);
		startActivity(intent);
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_settings:
			
			
			break;
		
		case R.id.import_db:
			
			break;
			
		case R.id.export_db:
			
			break;
			
		case R.id.edit_items:
			
			break;
			
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	public void importDB(){
		
	}
	
	public void exportDB(){
		
	}
	
	public void edit_items(){
		
	}
	
	
	
}
