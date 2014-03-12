package com.avidandrew.habittracker;

import android.content.Context;
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
