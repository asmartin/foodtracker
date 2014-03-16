package com.avidandrew.habittracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Item {
	public final static String SQL_GET_ALL_ROWS = "SELECT * FROM " + DBHelper.TABLE_ITEMS + " WHERE 1";	// needs to be public because is used in DBHelper
	public final static String SQL_GET_ROW_BY_NAME = "SELECT * FROM " + DBHelper.TABLE_ITEMS + " WHERE " + DBHelper.COLUMN_ITEM_NAME + " = '%s'";
	public final static String SQL_GET_ROW_BY_ID = "SELECT * FROM " + DBHelper.TABLE_ITEMS + " WHERE " + DBHelper.COLUMN_ID + " = '%d'";

	
	public static final String SQL_GET_TIMESTAMP_ROWS_MATCHING_ITEMID = "SELECT " + DBHelper.COLUMN_TIME_ID + " FROM " + DBHelper.TABLE_TIMESTAMPS + " WHERE " + DBHelper.COLUMN_TIME_ITEM_ID + "='%s'";
	public static final String SQL_DELETE_ALL_ITEM_TIMESTAMPS = "DELETE FROM " + DBHelper.TABLE_TIMESTAMPS + " WHERE " + DBHelper.COLUMN_TIME_ID + " IN (SELECT " + DBHelper.COLUMN_TIME_ID + " FROM "
			+ DBHelper.TABLE_TIMESTAMPS + " WHERE " + DBHelper.COLUMN_TIME_ITEM_ID + "='%d')";
	private final String SQL_DELETE_LAST_TIMESTAMP = "DELETE FROM " + DBHelper.TABLE_TIMESTAMPS + " WHERE " + DBHelper.COLUMN_TIME_ID + " IN (SELECT " + DBHelper.COLUMN_TIME_ID + " FROM "
			+ DBHelper.TABLE_TIMESTAMPS + " WHERE " + DBHelper.COLUMN_TIME_ITEM_ID + "='%d' ORDER BY " + DBHelper.COLUMN_TIME_ID + " DESC LIMIT 1)";
	public static final String SQL_DELETE_ITEM = "DELETE FROM " + DBHelper.TABLE_ITEMS + " WHERE " + DBHelper.COLUMN_ID + "='%d'";
	public String item_name;
	public int totalCounter;
	public int max_servings = 2;
	public int day_counter;
	public long itemID = -1;
	private Context c = null;		// the Context passed to the constructor (may be needed by other methods)

	// Database fields
	private DBHelper dbHelper;
	private String[] ITEM_TABLE_COLUMNS = {DBHelper.COLUMN_ID, DBHelper.COLUMN_ITEM_NAME, String.valueOf(DBHelper.COLUMN_VALUE), String.valueOf(DBHelper.COLUMN_MAX) };
	private SQLiteDatabase database;

	/** 
	 * opens the database connection
	 * @param c
	 * @throws SQLException
	 */
	public void open(Context c) throws SQLException {
		this.c = c;		// save the context for use later in methods
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
	 * @return true if update succeeded, false otherwise
	 */
	private boolean update(String col, String newValue) {
	    ContentValues args = new ContentValues();
	    args.put(col, newValue);
	    int results = database.update(DBHelper.TABLE_ITEMS, args, DBHelper.COLUMN_ID + " = " + itemID, null);
	    if (results < 1) {
	    	// no rows affected; this didn't work as expected
	    	return false;
	    }
	    return true;
	}
	
	/**
	 * Updates the max value of the item
	 * @param new_max the new max value
	 * @return true if the update succeeded, false otherwise
	 */
	public boolean updateMax(int new_max){
		return update(DBHelper.COLUMN_MAX, String.valueOf(new_max));
	}
	
	/**
	 * Updates the name of the item; does not check if a different item already 
	 * @param new_name the new name to give the item
	 * @return true if the update succeeded, false otherwise
	 */
	public boolean updateName(String new_name){
		if (new_name.equals(item_name)) {
			// the new name is the same as the old name, no need to do anything
			return true;
		}

		if (dbHelper.itemNameExists(new_name)) {
			// another item in the database has the same name, refuse to update
			return false;
		}
		
		return update(DBHelper.COLUMN_ITEM_NAME, new_name);
	}


	
	/**
	 * Increments the counter; updates the data in the database
	 * @return the new value of the counter
	 */
	public int increment() {
		totalCounter++;
		update(DBHelper.COLUMN_VALUE, String.valueOf(totalCounter));
		
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
		int numRowsBefore = dbHelper.getNumRows(String.format(SQL_GET_TIMESTAMP_ROWS_MATCHING_ITEMID, itemID));
		
		// remove the last timestamp row
		database.execSQL(String.format(SQL_DELETE_LAST_TIMESTAMP, itemID));
		
		// check how many timestamp rows there are after the removal (should be one less)
		int numRowsAfter = dbHelper.getNumRows(String.format(SQL_GET_TIMESTAMP_ROWS_MATCHING_ITEMID, itemID));
		
		if (numRowsBefore == -1 || numRowsAfter + 1 != numRowsBefore) {
			// this method was supposed to remove one row from this table, but something else happened
			throw new SQLException();
		}
		
		update(DBHelper.COLUMN_VALUE, String.valueOf(totalCounter));
		
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
	
	public int getID(){
		return (int) itemID;
		
	}
}
