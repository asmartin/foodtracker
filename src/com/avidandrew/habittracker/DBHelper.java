package com.avidandrew.habittracker;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public static final String TABLE_ITEMS = "Items";				// name of the items table
	public static final String COLUMN_ID = "_id";					// name of the ID column (primary key)
	public static final String COLUMN_ITEM_NAME = "Name";			// name of the "name" column
	public static final String COLUMN_VALUE = "Value";				// name of the counter column
	public static final String COLUMN_MAX = "Max";					// name of the max recommended column
	
	public static final String TABLE_TIMESTAMPS = "Timestamps";		// name of the timestamp table
	public static final String COLUMN_TIME_ID = "_id";				// name of the timestamp table's ID column
	public static final String COLUMN_TIME_ITEM_ID = "itemid";			// name of the ID of the item associated with this timestamp (a foreign key)
	public static final String COLUMN_TIME_STAMP = "timestamp";		// name of the timestamp column
	
	public static final String DATABASE_NAME = "foodtracker.db";	// name of the database
	public static final int DATABASE_VERSION = 1;
	private SQLiteDatabase database = null;

	// Database creation SQL statement
	private static final String TABLE_CREATE_ITEMS = "Create table " + TABLE_ITEMS
			  + "(" + COLUMN_ID + " integer primary key autoincrement, " 
			  + COLUMN_ITEM_NAME + " text not null, " 
			  + COLUMN_VALUE + " integer, " 
			  + COLUMN_MAX + " integer); ";

	private static final String TABLE_CREATE_TIMESTAMPS = "Create table " + TABLE_TIMESTAMPS
			  + "(" + COLUMN_TIME_ID + " integer primary key autoincrement, "
			  + COLUMN_TIME_ITEM_ID + " integer, "
			  + COLUMN_TIME_STAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP);";	
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/** 
	 * initialize the database connector
	 */
	private void dbInit() {
		if (database == null) {
			database = this.getWritableDatabase();
		}
	}
	/** 
	 * Gets all of the items currently in the database
	 * @param c the current context
	 * @return a list of the items in the database, or an empty list if the database is empty
	 */
	public ArrayList<Item> getItems(Context c) {
		dbInit();
		ArrayList<Item> items = new ArrayList<Item>();
		Cursor results = database.rawQuery(Item.SQL_GET_ALL_ROWS, null);
		
		if (results.moveToFirst()) {
			do {
				items.add(new Item(c, results));
			} while (results.moveToNext());
        }
        
        return items;
	}
	
	
	public boolean deleteItem(int item_id){
		dbInit();
		// remove the last timestamp row
		database.execSQL(String.format(Item.SQL_DELETE_ALL_ITEM_TIMESTAMPS, item_id));
		database.execSQL(String.format(Item.SQL_DELETE_ITEM, item_id));
		
		int matchingItems = getNumRows(String.format(Item.SQL_GET_ROW_BY_ID, item_id));
		int matchingTimestamps = getNumRows(String.format(Item.SQL_GET_TIMESTAMP_ROWS_MATCHING_ITEMID, item_id));
		
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
	public Item getItem(Context c, String name) {
		Item thisItem = null;
		dbInit();
		Cursor results = database.rawQuery(String.format(Item.SQL_GET_ROW_BY_NAME, name), null);
		
		if (results.moveToFirst()) {
			thisItem = new Item(c, results);
        }
        
        return thisItem;
	}
	
	
	
	public boolean itemNameExists(Context c, String name) {
		ArrayList<Item> items = getItems(c);
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
	
	public static void emptyDB(Context c) {
		DBHelper dbh = new DBHelper(c);
		SQLiteDatabase d = dbh.getWritableDatabase();
		d.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		d.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMESTAMPS);
		d.execSQL(TABLE_CREATE_ITEMS);
		d.execSQL(TABLE_CREATE_TIMESTAMPS);
	}
}
