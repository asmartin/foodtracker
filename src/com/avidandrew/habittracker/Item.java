package com.avidandrew.habittracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Item {
	public final static String SQL_GET_ALL_ROWS = "SELECT * FROM " + DBHelper.TABLE_ITEMS + " WHERE 1";	// needs to be public because is used in DBHelper
	private final String SQL_GET_ROW_BY_NAME = "SELECT * FROM " + DBHelper.TABLE_ITEMS + " WHERE " + DBHelper.COLUMN_ITEM_NAME + " = '%s'";
	private final String SQL_GET_TIMESTAMP_ROWS_MATCHING_ITEMID = "SELECT " + DBHelper.COLUMN_TIME_ID + " FROM " + DBHelper.TABLE_TIMESTAMPS + " WHERE " + DBHelper.COLUMN_TIME_ITEM_ID + "='%s'";
	private final String SQL_DELETE_LAST_TIMESTAMP = "DELETE FROM " + DBHelper.TABLE_TIMESTAMPS + " WHERE " + DBHelper.COLUMN_TIME_ID + " IN (SELECT " + DBHelper.COLUMN_TIME_ID + " FROM "
			+ DBHelper.TABLE_TIMESTAMPS + " WHERE " + DBHelper.COLUMN_TIME_ITEM_ID + "='%d' ORDER BY " + DBHelper.COLUMN_TIME_ID + " DESC LIMIT 1)";
	public String item_name;
	public int totalCounter;
	public int max_servings = 2;
	public int day_counter;
	public long itemID = -1;

	// Database fields
	private DBHelper dbHelper;
	private String[] ITEM_TABLE_COLUMNS = {DBHelper.COLUMN_ID, DBHelper.COLUMN_ITEM_NAME, String.valueOf(DBHelper.COLUMN_VALUE), String.valueOf(DBHelper.COLUMN_MAX) };
	private SQLiteDatabase database;

	public void open(Context c) throws SQLException {
		dbHelper = new DBHelper(c);
		database = dbHelper.getWritableDatabase();
	}

	/** 
	 * Creates a new item
	 * @param c
	 * @param name
	 * @param max_servings
	 */
	public Item(Context c, String name, int max_servings){
		open(c);

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
	        } else {
	        	// cannot load any data from the database, error
	        	throw new SQLException();
	        }
		} else {
			// row already exists, update local variables
			updateVariablesFromDB(results, false);
		}
	}
	
	/**
	 * Create a new item from data pulled from the database
	 * @param c the context (view)
	 * @param data the Cursor object, moveToFirst already run
	 */
	public Item(Context c, Cursor data) {
		open(c);
		updateVariablesFromDB(data, true);
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
		item_name = results.getString(results.getColumnIndexOrThrow(DBHelper.COLUMN_ITEM_NAME));
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
	    int results = database.update(DBHelper.TABLE_ITEMS, args, DBHelper.COLUMN_ID + " = " + itemID, null);
	    if (results < 1) {
	    	// no rows affected; this didn't work as expected
	    	throw new SQLException();
	    }
	}
	
	public void updateMax(int new_max){
		
		
	}
	
	public void updateName(String new_name){
		
		
	}

	/**
	 * Returns the number of rows matching the query
	 * @param sql the SQL query to use to query the rows
	 * @return returns the number of rows matching the query, or -1 if none found
	 */
	private int getNumRows(String sql) {
		Cursor results = database.rawQuery(sql, null);
		if (results == null || results.getCount() < 0) {
			// can't get any existing rows
			return -1;
		}
		int numRows = results.getCount();
		results.close();
		return numRows;
	}
	
	/**
	 * Increments the counter; updates the data in the database
	 * @return the new value of the counter
	 */
	public int increment() {
		totalCounter++;
		update(DBHelper.COLUMN_VALUE, totalCounter);
		
		// record the timestamp
		ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TIME_ITEM_ID, itemID);
		long ret = database.insert(DBHelper.TABLE_TIMESTAMPS, null, values);
		if (ret < 0) {
			// error inserting row
			throw new SQLException();
		}
			
		return totalCounter;
	}
	
	/**
	 * Decrements the counter; updates the data in the database
	 * @return the new value of the counter
	 */
	public int decrement() {
		if (totalCounter <= 0) {
			// don't allow negative values
			totalCounter = 0;
			return totalCounter;
		}
		totalCounter--;
		
		// check how many timestamp rows there are before the removal
		int numRowsBefore = getNumRows(String.format(SQL_GET_TIMESTAMP_ROWS_MATCHING_ITEMID, itemID));
		
		// remove the last timestamp row
		database.execSQL(String.format(SQL_DELETE_LAST_TIMESTAMP, itemID));
		
		// check how many timestamp rows there are after the removal (should be one less)
		int numRowsAfter = getNumRows(String.format(SQL_GET_TIMESTAMP_ROWS_MATCHING_ITEMID, itemID));
		
		if (numRowsBefore == -1 || numRowsAfter + 1 != numRowsBefore) {
			// this method was supposed to remove one row from this table, but something else happened
			throw new SQLException();
		}
		
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
