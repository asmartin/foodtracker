package com.avidandrew.habittracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public static final String TABLE_ITEMS = "Items";				// name of the table
	public static final String COLUMN_ID = "_id";					// name of the ID column (primary key)
	public static final String COLUMN_ITEM_NAME = "Name";			// name of the "name" column
	public static final String COLUMN_VALUE = "Value";				// name of the counter column
	public static final String COLUMN_MAX = "Max";					// name of the max recommended column
	
	public static final String DATABASE_NAME = "foodtracker.db";	// name of the database
	public static final int DATABASE_VERSION = 1;

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "Create table " + TABLE_ITEMS
			  + "(" + COLUMN_ID + " integer primary key autoincrement, " 
			  + COLUMN_ITEM_NAME + " text not null, " 
			  + COLUMN_VALUE + " integer, " 
			  + COLUMN_MAX + " integer);";	   
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		db.execSQL(DATABASE_CREATE);
	}
}
