package com.avidandrew.habittracker;

import java.util.ArrayList;

import com.example.first_app.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import static com.avidandrew.habittracker.Constants.*;

public class MainActivity extends Activity {
	
	private TableLayout layout = null;
	
	/**
	 * determines if there is data in the Items database; if so, display it; else, display sample data
	 */
	private void loadItems() {
		DBHelper db = new DBHelper(this);
		ArrayList<Item> items = db.getItems();
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
			
			//Margins
			TableRow.LayoutParams rowMargins = new TableRow.LayoutParams();
			rowMargins.setMargins(5, 5, 5, 5);

			for (final Item item : items) {
				TableRow row = new ItemView(this, item);
				row.setLayoutParams(rowMargins);
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
		for (String[] sample : SAMPLE_DATA) {
			if (sample.length > 1) {
				int max = Integer.parseInt(sample[1]);
				sampleData.add(new Item(this, sample[0], max));
			}
		}
		
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

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// re-generate list of items
		layout.removeAllViews();
		loadItems();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		
		case R.id.add_item:
			add_item();
			return true;
			
		case R.id.about_actvity:
			Intent intent = new Intent(this, About_Activity.class);
			startActivity(intent);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	public void add_item(){
		Intent intent = new Intent(this, Add_Item_Activity.class);

		startActivity(intent);
	}
}
