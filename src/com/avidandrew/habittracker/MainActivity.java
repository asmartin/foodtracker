package com.avidandrew.habittracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import com.example.first_app.R;

import android.R.style;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
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

		// empty DB (for debugging)
		// DBHelper.emptyDB(this);
		
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
			return true;
		
		case R.id.import_db:
			importDB();
			return true;
			
		case R.id.export_db:
			exportDB();
			return true;
			
		case R.id.edit_items:
			edit_items();
			return true;
			
		case R.id.add_item:
			add_item();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	
	public void importDB(){
		// TODO
		
	}
	
	public void exportDB(){
		
		try{
		
		  File sd = Environment.getExternalStorageDirectory();
          File data = Environment.getDataDirectory();

          if (sd.canWrite()) {
        	  String currentDBPath = "/data/com.example.first_app/databases/foodtracker.db";
              String backupDBPath = "foodtracker_backup.db";
              File currentDB = new File(data, currentDBPath);
              File backupDB = new File(sd, backupDBPath);

                  FileChannel src = new FileInputStream(currentDB).getChannel();
                  FileChannel dst = new FileOutputStream(backupDB).getChannel();
                  dst.transferFrom(src, 0, src.size());
                  src.close();
                  dst.close();
                  Toast.makeText(getBaseContext(), backupDB.toString(), Toast.LENGTH_LONG).show();

          }
      } catch (Exception e) {

          Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();


      }
}
	        	
	public void edit_items(){
		//TODO
	}
	
	public void add_item(){
		Intent intent = new Intent(this, Add_Item_Activity.class);
		startActivity(intent);
		Toast.makeText(getBaseContext(), "Starting Add Item Activity",Toast.LENGTH_SHORT);
		
	}
	
	
	
}
