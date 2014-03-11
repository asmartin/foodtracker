package com.example.first_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Item {
	public String item_name;
	public int totalCounter;
	public int max_servings = 2;
	public int day_counter;
	public long itemID = -1;

	// Database fields
	private DBHelper dbHelper;
	private String[] ITEM_TABLE_COLUMNS = { DBHelper.COLUMN_ID, DBHelper.COLUMN_ITEM_NAME, String.valueOf(DBHelper.COLUMN_VALUE), String.valueOf(DBHelper.COLUMN_MAX) };
	private SQLiteDatabase database;

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public Item(Context c, String name, int max_servings){
		dbHelper = new DBHelper(c);
		open();
		// uncomment to clear database
		//dbHelper.onUpgrade(database, 1, 2);

		this.item_name = name;
		this.max_servings = max_servings;
		
		// check if this item exists already in the database (check based on name)
		Cursor results = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_ITEMS + " WHERE " + DBHelper.COLUMN_ITEM_NAME + " = '" + name + "'", null);  		
		if (!results.moveToFirst()) {
			// no existing row found, add one for this item
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_ITEM_NAME, name);
            values.put(DBHelper.COLUMN_VALUE, 0);
            values.put(DBHelper.COLUMN_MAX, max_servings);
			itemID = database.insert(DBHelper.TABLE_ITEMS, null, values);
			String query = "SELECT * FROM " + DBHelper.TABLE_ITEMS + " WHERE " + DBHelper.COLUMN_ITEM_NAME + " = '" + name + "'";
	        results = database.rawQuery(query, null);
	        if (results.moveToFirst()) {
	        	// read in newly-inserted data
	        	updateVariablesFromDB(results);
	        }
		} else {
			// row already exists
			updateVariablesFromDB(results);
		}
	}
	
	// assumes results.moveToFirst() already done
	private void updateVariablesFromDB(Cursor results) {
		// populate values from database
		itemID = results.getInt(results.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
		totalCounter = results.getInt(results.getColumnIndexOrThrow(DBHelper.COLUMN_VALUE));
		max_servings = results.getInt(results.getColumnIndexOrThrow(DBHelper.COLUMN_MAX));
	}
	
	private void update(String col, int newValue) {
	    ContentValues args = new ContentValues();
	    args.put(col, newValue);
	    database.update(DBHelper.TABLE_ITEMS, args, DBHelper.COLUMN_ID + " = " + itemID, null);
	}

	public int increment() {
		totalCounter++;
		update(DBHelper.COLUMN_VALUE, totalCounter);
		return totalCounter;
	}
	
	public int decrement() {
		totalCounter--;
		update(DBHelper.COLUMN_VALUE, totalCounter);
		return totalCounter;
	}
	
	public int getCounterValue() {
		return totalCounter;
	}
	
	public String getId(){

		return item_name;
	}

	public int getMaxServings(){
		return max_servings;
	}

	@Override
	public String toString() {
		return item_name;
	}
}
