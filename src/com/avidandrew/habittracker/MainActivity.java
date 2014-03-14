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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	private TableLayout layout = null;
	
	/**
	 * determines if there is data in the Items database; if so, display it; else, display sample data
	 */
	private void loadItems() {
		DBHelper db = new DBHelper(this);
		ArrayList<Item> items = db.getItems(this);
		if (items == null || items.size() == 0) {
			// load sample data
			loadSampleData();
		} else {
			// load data from database
			loadItemsView(items);
		}
	}
	
	/**
	 * Loads a list of items into the activity
	 * @param items
	 */
	private void loadItemsView(ArrayList<Item> items) {
		if (items != null) {
			layout = (TableLayout) findViewById(R.id.main_table);
			layout.removeAllViews();    // remove any existing rows
			for (final Item item : items) {
				TableRow row = new ItemView(this, item);

				//add row to view
				layout.addView(row);
			}
		}
	}
	
	/**
	 * Loads sample data into the activity
	 */
	private void loadSampleData() {
		ArrayList<Item> sampleData = new ArrayList<Item>();
		sampleData.add(new Item(this, "Fats, Oils, Sweets",1));
		sampleData.add(new Item(this, "Dairy",3));
		sampleData.add(new Item(this, "Meats, Eggs, Nuts", 3));
		sampleData.add(new Item(this, "Vegetables", 5));
		sampleData.add(new Item(this, "Fruits", 5));
		sampleData.add(new Item(this, "Breads, Carbs", 11));
		loadItemsView(sampleData);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// empty DB (for debugging)
		// DBHelper.emptyDB(this);
		
		loadItems();
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
		
		// re-generate list of items
		loadItems();
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
