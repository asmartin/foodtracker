package com.avidandrew.habittracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Item {
	private final String SQL_GET_ALL_ROWS = "SELECT * FROM " + DBHelper.TABLE_ITEMS + " WHERE 1";
	private final String SQL_GET_ROW_BY_NAME = "SELECT * FROM " + DBHelper.TABLE_ITEMS + " WHERE " + DBHelper.COLUMN_ITEM_NAME + " = '%s'";
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

	/** 
	 * Creates a new item
	 * @param c
	 * @param name
	 * @param max_servings
	 */
	public Item(Context c, String name, int max_servings){
		dbHelper = new DBHelper(c);
		open();
		// uncomment to clear database
		//dbHelper.onUpgrade(database, 1, 2);

		this.item_name = name;
		this.max_servings = max_servings;
		
		// check if this item exists already in the database (check based on name)
		Cursor results = database.rawQuery(String.format(SQL_GET_ROW_BY_NAME,  name), null);  		
		if (!results.moveToFirst()) {
			// no existing row found, add one for this item
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_ITEM_NAME, name);
            values.put(DBHelper.COLUMN_VALUE, 0);
            values.put(DBHelper.COLUMN_MAX, max_servings);
			long ret = database.insert(DBHelper.TABLE_ITEMS, null, values);
			if (ret < 0) {
				// error inserting row
				throw new SQLException();
			}
			
			// load the newly-inserted data from the database
	        results = database.rawQuery(String.format(SQL_GET_ROW_BY_NAME,  name), null);
	        if (results.moveToFirst()) {
	        	updateVariablesFromDB(results, false);
	        }
		} else {
			// row already exists, update local variables
			updateVariablesFromDB(results, false);
		}
	}

	/** 
	 * Updates local variables from the results pulled from the database
	 * @param results the Cursor object with the results
	 * @param movedToFirst set to false if you've already called moveToFirst() on this Cursor object
	 */
	private void updateVariablesFromDB(Cursor results, boolean movedToFirst) {
		if (!movedToFirst) {
			// if not already done, move to the first row of results
			results.moveToFirst();
		}
		itemID = results.getInt(results.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
		totalCounter = results.getInt(results.getColumnIndexOrThrow(DBHelper.COLUMN_VALUE));
		max_servings = results.getInt(results.getColumnIndexOrThrow(DBHelper.COLUMN_MAX));
	}
	
	/** 
	 * Updates an existing row in the database
	 * @param col the name of the column to update
	 * @param newValue the new value
	 */
	private void update(String col, int newValue) {
	    ContentValues args = new ContentValues();
	    args.put(col, newValue);
	    database.update(DBHelper.TABLE_ITEMS, args, DBHelper.COLUMN_ID + " = " + itemID, null);
	}

	/**
	 * Increments the counter; updates the data in the database
	 * @return the new value of the counter
	 */
	public int increment() {
		totalCounter++;
		update(DBHelper.COLUMN_VALUE, totalCounter);
		return totalCounter;
	}
	
	/**
	 * Decrements the counter; updates the data in the database
	 * @return the new value of the counter
	 */
	public int decrement() {
		totalCounter--;
		update(DBHelper.COLUMN_VALUE, totalCounter);
		return totalCounter;
	}
	
	/** 
	 * Returns the current counter value
	 * @return the current value of the counter
	 */
	public int getCounterValue() {
		return totalCounter;
	}
	
	/** 
	 * Returns the name of the item
	 * @return the name of the item
	 */
	public String getName(){

		return item_name;
	}

	/**
	 * Returns the maximum number of servings
	 * @return the maximum number of servings
	 */
	public int getMaxServings(){
		return max_servings;
	}

	@Override
	public String toString() {
		return item_name;
	}
}
