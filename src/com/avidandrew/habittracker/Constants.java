package com.avidandrew.habittracker;

public class Constants {
	public static final String APP_NAME = "Habit Tracker";
	public static final String DATABASE_NAME = "habittracker.db";	// name of the database
	
	// periods
	public static final int PERIOD_NONE = 0;
	public static final int PERIOD_DAILY = 1;
	public static final int PERIOD_WEEKLY = 2;
	public static final int PERIOD_MONTHLY = 3;
	public static final int DEFAULT_TAB_INDEX = 1;					// by default start at the daily tab
	
	// Items table
	public static final String TABLE_ITEMS = "Items";				// name of the items table
	public static final String COLUMN_ID = "_id";					// name of the ID column (primary key)
	public static final String COLUMN_ITEM_NAME = "Name";			// name of the "name" column
	public static final String COLUMN_VALUE = "Value";				// name of the counter column
	public static final String COLUMN_MAX = "Max";					// name of the max recommended column
	
	// Timestamps table
	public static final String TABLE_TIMESTAMPS = "Timestamps";		// name of the timestamp table
	public static final String COLUMN_TIME_ID = "_id";				// name of the timestamp table's ID column
	public static final String COLUMN_TIME_ITEM_ID = "itemid";		// name of the ID of the item associated with this timestamp (a foreign key)
	public static final String COLUMN_TIME_STAMP = "timestamp";		// name of the timestamp column
	
	// SQL Queries
	public static final String SQL_GET_ALL_ROWS = "SELECT * FROM " + TABLE_ITEMS + " WHERE 1";
	public static final String SQL_GET_ROW_BY_NAME = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEM_NAME + " = '%s'";
	public static final String SQL_GET_ROW_BY_ID = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ID + " = '%d'";
	public static final String SQL_GET_TIMESTAMP_ROWS_MATCHING_ITEMID = "SELECT " + COLUMN_TIME_ID + " FROM " + TABLE_TIMESTAMPS + " WHERE " + COLUMN_TIME_ITEM_ID + "='%s'";
	public static final String SQL_DELETE_ALL_ITEM_TIMESTAMPS = "DELETE FROM " + TABLE_TIMESTAMPS + " WHERE " + COLUMN_TIME_ID + " IN (SELECT " + COLUMN_TIME_ID + " FROM "
			+ TABLE_TIMESTAMPS + " WHERE " + COLUMN_TIME_ITEM_ID + "='%d')";
	public static final String SQL_DELETE_LAST_TIMESTAMP = "DELETE FROM " + TABLE_TIMESTAMPS + " WHERE " + COLUMN_TIME_ID + " IN (SELECT " + COLUMN_TIME_ID + " FROM "
			+ TABLE_TIMESTAMPS + " WHERE " + COLUMN_TIME_ITEM_ID + "='%d' ORDER BY " + COLUMN_TIME_ID + " DESC LIMIT 1)";
	public static final String SQL_DELETE_ITEM = "DELETE FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ID + "='%d'";
	
	// Extras
	public static final String EXTRA_NAME = "extra_name";
	public static final String EXTRA_MAX = "extra_max";
	
	// Sample Data
	public static final String[][] SAMPLE_DATA = { {"Fats, Oils, Sweets", "1"}, {"Meat", "2"}, {"Eggs", "2"}, {"Dairy", "3"}, {"Fruits", "4"}, {"Vegetables", "5"}, {"Breads, Carbs", "3"}, {"Nuts", "4"} };

	
	
}