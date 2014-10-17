package com.avidandrew.habittracker;

import com.avidandrew.habittracker.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import static com.avidandrew.habittracker.Constants.*;

public class Item extends Object{
	public long itemID = -1;		// the unique ID of the item
	public String item_name;		// the name of the item
	public int period = 0;			// the period of the item
	public int totalCounter;		// the current value of the counter
	public int max_in_period = 2;	// the maximum recommended/periodic counter value	
	private Context c = null;		// the Context passed to the constructor (may be needed by other methods)
	
	// Database fields
	private DBHelper dbHelper;
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
	 * @param max_in_period
	 */
	public Item(Context c, String name, int period, int max_in_period){
		open(c);

		this.item_name = name;
		this.period = period;
		this.max_in_period = max_in_period;
		
		// check if this item exists already in the database (check based on name)
		Cursor results = database.rawQuery(String.format(SQL_GET_ROW_BY_NAME,  name), null); 
		if (!results.moveToFirst()) {
			// no existing row found, add one for this item
            ContentValues values = new ContentValues();
            values.put(COLUMN_ITEM_NAME, name);
            values.put(COLUMN_ITEM_PERIOD, period);
            values.put(COLUMN_VALUE, 0);
            values.put(COLUMN_MAX, max_in_period);
			long ret = database.insert(TABLE_ITEMS, null, values);
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
	 * Create a new item from data pulled from the database
	 * @param c the context (view)
	 * @param data the Cursor object, moveToFirst already run
	 * @param numInPeriod the number of items in the current period (overrides the number of items in data)
	 */
	public Item(Context c, Cursor data, int numInPeriod) {
		open(c);
		updateVariablesFromDB(data, true);
		totalCounter = numInPeriod;
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
		
		itemID = results.getInt(results.getColumnIndexOrThrow(COLUMN_ID));
		item_name = results.getString(results.getColumnIndexOrThrow(COLUMN_ITEM_NAME));
		period = results.getInt(results.getColumnIndexOrThrow(COLUMN_ITEM_PERIOD));
		totalCounter = results.getInt(results.getColumnIndexOrThrow(COLUMN_VALUE));
		max_in_period = results.getInt(results.getColumnIndexOrThrow(COLUMN_MAX));
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
	    int results = database.update(TABLE_ITEMS, args, COLUMN_ID + " = " + itemID, null);
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
		return update(COLUMN_MAX, String.valueOf(new_max));
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
		
		return update(COLUMN_ITEM_NAME, new_name);
	}
	
	/**
	 * Updates the name of the item; does not check if a different item already 
	 * @param new_name the new name to give the item
	 * @return true if the update succeeded, false otherwise
	 */
	public boolean updatePeriod(int new_period){
		if (new_period == period) {
			// meet the new period, same as the old period; won't get fooled again
			return true;
		}
		
		return update(COLUMN_ITEM_PERIOD, String.valueOf(new_period));
	}
	
	public int getPeriod(){return this.period;}
	
	/**
	 * Increments the counter; updates the data in the database
	 * @return the new value of the counter
	 */
	public int increment() {
		
		totalCounter++;
		update(COLUMN_VALUE, String.valueOf(totalCounter));
		
		// record the timestamp
		ContentValues values = new ContentValues();
        values.put(COLUMN_TIME_ITEM_ID, itemID);
		long ret = database.insert(TABLE_TIMESTAMPS, null, values);
		if (ret < 0) {
			// error inserting row
										
			Toast.makeText(c, c.getApplicationContext().getString(R.string.MSG_ERROR_INCREMENT) + item_name, Toast.LENGTH_SHORT).show();
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
			
			Toast.makeText(c, c.getApplicationContext().getResources().getString(R.string.MSG_ERROR_DECREMENT) + item_name, Toast.LENGTH_SHORT).show();
		}
		
		update(COLUMN_VALUE, String.valueOf(totalCounter));
		
		return totalCounter;
	}

	/** 
	 * Returns the unique ID of the item
	 * @return the item ID
	 */
	public int getID(){
		return (int) itemID;
		
	}	

	/** 
	 * Returns the name of the item
	 * @return the name of the item
	 */
	public String getName(){
		return item_name;
	}
	
	/** 
	 * Returns the current counter value
	 * @return the current value of the counter
	 */
	public int getCounterValue() {
		return totalCounter;
	}
	
	/**
	 * Returns the maximum number of servings
	 * @return the maximum number of servings
	 */
	public int getMaxInPeriod(){
		return max_in_period;
	}

	@Override
	public String toString() {
		return item_name;
	}
}
