package com.avidandrew.habittracker;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.avidandrew.habittracker.Constants.*;

public class DBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase database = null;
	
	private Context c = null;		// the Context passed to the constructor (may be needed by other methods)

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.c = context;	// save the context for use in methods		
	}
	
	/** 
	 * initialize the SQLiteDatabase object
	 */
	private void dbInit() {
		if (database == null) {
			database = this.getWritableDatabase();
			onCreate(database);	// create the tables if they don't exist
		}
	}

	/** 
	 * Gets all of the items currently in the database
	 * @return a list of the items in the database, or an empty list if the database is empty
	 */
	public ArrayList<Item> getItems() {
		dbInit();
		ArrayList<Item> items = new ArrayList<Item>();
		Cursor results = database.rawQuery(SQL_GET_ALL_ROWS, null);
		
		if (results.moveToFirst()) {
			do {
				items.add(new Item(c, results));
			} while (results.moveToNext());
		}
        
		results.close();
		
		return items;
	}
	
	/** 
	 * Gets all of the results of the specified query and return as an arraylist of strings
	 * @return a list of the results from the database, or an empty list if the database is empty
	 */
	public ArrayList<ArrayList<String>> getResultsAsStrings(String query) {
		dbInit();
		ArrayList<ArrayList<String>> items = new ArrayList<ArrayList<String>>();
		Cursor results = database.rawQuery(query, null);
		
		if (results.moveToFirst()) {
			do {
				ArrayList<String> thisItem = new ArrayList<String>();
				String[] columns = results.getColumnNames();
				for (String col : columns) {
					int thisIndex = results.getColumnIndex(col);
					thisItem.add(results.getString(thisIndex));
				}
				items.add(thisItem);
			} while (results.moveToNext());
		}
		
		results.close();
        
		return items;
	}
	
	public boolean insertFromCSV(String table, String header[], String data[]) {
		dbInit();
		// read in the contents of the file
		ContentValues cv = new ContentValues();
		for (int i = 0; i < header.length; i++) {
			if (data.length < i) {
				// data and header mismatch, fail
				return false;
			}
			
			cv.put(header[i], data[i]);
		}
		
		return (database.insert(table, null, cv) >= 0);
	}
	
	/** 
	 * Gets all of the items currently in the database for a specific period
	 * @param int the period
	 * @return a list of the items in the database, or an empty list if the database is empty
	 */
	public ArrayList<Item> getItemsByPeriod(int period) {
		dbInit();
		ArrayList<Item> items = new ArrayList<Item>();
		Cursor results = database.rawQuery(String.format(SQL_GET_ROWS_MATCHING_PERIOD, period), null);
		
		if (results.moveToFirst()) {
			do {
				
				if (period == PERIOD_NONE) {
					items.add(new Item(c, results));
				} else {
					Item thisItem = new Item(c, results);
					
					String periodFormat = null;
					if (period == PERIOD_DAILY) {
						periodFormat = "%j-%Y";
					} else if (period == PERIOD_WEEKLY) {
						// the manpage for POSIX strftime lists Monday as the first day of the week
						periodFormat = "%W-%Y";
					} else if (period == PERIOD_MONTHLY) {
						periodFormat = "%m-%Y";
					}
					
					if (periodFormat == null) {
						// no period could be determined, just use all of the items
						items.add(thisItem);
					} else {
						// we have found a valid period, find out how many of the timestamps are within the current one
						int numTimestampsInPeriod = getNumRows(String.format(SQL_GET_TIMESTAMPS_IN_PERIOD, thisItem.getID(), periodFormat, periodFormat));
						if (numTimestampsInPeriod < 0) {
							numTimestampsInPeriod = 0;
						}
						
						// now add the item, with the adjusted number of timestamps
						items.add(new Item(c, results, numTimestampsInPeriod));
					}
				}
				
			} while (results.moveToNext());
        }
		
		results.close();
        
        return items;
	}
	
	
	public boolean deleteItem(int item_id){
		dbInit();
		// remove the last timestamp row
		database.execSQL(String.format(SQL_DELETE_ALL_ITEM_TIMESTAMPS, item_id));
		database.execSQL(String.format(SQL_DELETE_ITEM, item_id));
		
		int matchingItems = getNumRows(String.format(SQL_GET_ROW_BY_ID, item_id));
		int matchingTimestamps = getNumRows(String.format(SQL_GET_TIMESTAMP_ROWS_MATCHING_ITEMID, item_id));
		
		if(matchingItems > 0 || matchingTimestamps > 0){
			return false;
		}
		return true;
	}
	
	/**
	 * Returns the number of rows matching the query
	 * @param sql the SQL query to use to query the rows
	 * @return returns the number of rows matching the query, or -1 if none found
	 */
	public int getNumRows(String sql) {
		dbInit();
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
	 * Gets an item object from a row in the database
	 * @param c the current context
	 * @param name the name of the item to get
	 * @return null if item not found in the database, else an object representing the item
	 */
	public Item getItem(String name) {
		Item thisItem = null;
		dbInit();
		Cursor results = database.rawQuery(String.format(SQL_GET_ROW_BY_NAME, name), null);
		
		if (results.moveToFirst()) {
			thisItem = new Item(c, results);
        }
		
		results.close();
        
        return thisItem;
	}
	
	/** 
	 * Checks if an item with the specified name already exits
	 * @param c the current context
	 * @param name the name to search for
	 * @return true if the name is already used by an item, false otherwise
	 */
	public boolean itemNameExists(String name) {
		ArrayList<Item> items = getItems();
		if (items == null) {
			return false;
		} else {
			for (Item i : items) {
				if (i.getName().equals(name)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE_ITEMS);
		db.execSQL(TABLE_CREATE_TIMESTAMPS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMESTAMPS);
		db.execSQL(TABLE_CREATE_ITEMS);
		db.execSQL(TABLE_CREATE_TIMESTAMPS);
	}
	
	/**
	 * deletes all content from the database
	 * @param c the current context
	 */
	public void emptyDB() {
		dbInit();
		this.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		this.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMESTAMPS);
	}
	
	/**
	 * executes SQL on the database
	 * @param c the current context
	 */
	public void execSQL(String sql) {
		SQLiteDatabase d = this.getWritableDatabase();
		d.execSQL(sql);
	}
	
	/**
	 * deletes all content from the database
	 * @param c the current context
	 */
	public static void recreateDB(Context c, String sql) {
		DBHelper dbh = new DBHelper(c);
		SQLiteDatabase d = dbh.getWritableDatabase();
		d.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		d.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMESTAMPS);
		d.execSQL(TABLE_CREATE_ITEMS);
		d.execSQL(TABLE_CREATE_TIMESTAMPS);
	}
}
