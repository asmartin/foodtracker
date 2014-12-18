package com.avidandrew.habittracker;

public interface Constants {
	public static final String APP_NAME = "Habit Tracker";
	public static final String APP_EXTENSION = "htr";
	public static final String DATABASE_NAME = "habittracker.db";	// name of the database
	public static final String VERSION = "0.9.0pre";					// name of the current version
	public static final String SEP = ", ";							// separator between items in SQL SELECT queries
	public static final String MANIFEST_FILENAME = "manifest.xml";	// filename of the manifest
	public static final String ITEMS_FILENAME = "items.csv";	// filename of the items csv file
	public static final String TIMESTAMPS_FILENAME = "timestamps.csv";	// filename of the timestamps csv file
	
	// periods - these values MUST correspond to the indexes of the ActionBar tabs
	public static final int PERIOD_NONE = 0;
	public static final int PERIOD_DAILY = 1;
	public static final int PERIOD_WEEKLY = 2;
	public static final int PERIOD_MONTHLY = 3;
	public static final int DEFAULT_TAB_INDEX = PERIOD_DAILY;		// by default start at the daily tab
	
	
	//Bundles for passing data between fragments
	
	public static final String EXTRA_FRAGMENT_ID = "com.avidandrew.habittracker.fragment_id";
	
	
	
	// Items table
	public static final String TABLE_ITEMS = "Items";				// name of the items table
	public static final String COLUMN_ID = "_id";					// name of the ID column (primary key)
	public static final String COLUMN_ITEM_NAME = "Name";			// name of the "name" column
	public static final String COLUMN_ITEM_PERIOD = "Period";		// name of the period column
	public static final String COLUMN_VALUE = "Value";				// name of the counter column
	public static final String COLUMN_MAX = "Max";					// name of the max recommended column
	public static final String ITEMS_COLUMNS_ORDER = COLUMN_ID + SEP + COLUMN_ITEM_NAME + SEP + COLUMN_ITEM_PERIOD + SEP + COLUMN_VALUE + SEP + COLUMN_MAX;
	
	// Timestamps table
	public static final String TABLE_TIMESTAMPS = "Timestamps";		// name of the timestamp table
	public static final String COLUMN_TIME_ID = "_id";				// name of the timestamp table's ID column
	public static final String COLUMN_TIME_ITEM_ID = "itemid";		// name of the ID of the item associated with this timestamp (a foreign key)
	public static final String COLUMN_TIME_STAMP = "timestamp";		// name of the timestamp column
	public static final String TIMESTAMPS_COLUMNS_ORDER = COLUMN_TIME_ID + SEP + COLUMN_TIME_ITEM_ID + SEP + COLUMN_TIME_STAMP;

	// Database creation
	public static final String TABLE_CREATE_ITEMS = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS
			  + "(" + COLUMN_ID + " integer primary key autoincrement, " 
			  + COLUMN_ITEM_NAME + " text not null, " 
			  + COLUMN_ITEM_PERIOD + " integer, " 
			  + COLUMN_VALUE + " integer, " 
			  + COLUMN_MAX + " integer); ";

	public static final String TABLE_CREATE_TIMESTAMPS = "CREATE TABLE IF NOT EXISTS " + TABLE_TIMESTAMPS
			  + "(" + COLUMN_TIME_ID + " integer primary key autoincrement, "
			  + COLUMN_TIME_ITEM_ID + " integer, "
			  + COLUMN_TIME_STAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP);";	
	
	// SQL Queries
	public static final String SQL_GET_ALL_ROWS = "SELECT " + ITEMS_COLUMNS_ORDER + " FROM " + TABLE_ITEMS + " WHERE 1";
	public static final String SQL_GET_ALL_TIMESTAMPS_ROWS = "SELECT " + TIMESTAMPS_COLUMNS_ORDER + " FROM " + TABLE_TIMESTAMPS + " WHERE 1";
	public static final String SQL_GET_ROWS_MATCHING_PERIOD = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEM_PERIOD + " = '%s'";
	public static final String SQL_GET_TIMESTAMPS_IN_PERIOD = "SELECT * FROM " + TABLE_TIMESTAMPS + " WHERE " + COLUMN_TIME_ITEM_ID + " = '%s' AND strftime('%s', " + COLUMN_TIME_STAMP + ") = strftime('%s', 'now')";
	public static final String SQL_GET_ROW_BY_NAME = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEM_NAME + " = '%s'";
	public static final String SQL_GET_ROW_BY_ID = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ID + " = '%d'";
	public static final String SQL_GET_TIMESTAMP_ROWS_MATCHING_ITEMID = "SELECT " + COLUMN_TIME_ID + " FROM " + TABLE_TIMESTAMPS + " WHERE " + COLUMN_TIME_ITEM_ID + "='%s'";
	public static final String SQL_DELETE_ALL_ITEM_TIMESTAMPS = "DELETE FROM " + TABLE_TIMESTAMPS + " WHERE " + COLUMN_TIME_ID + " IN (SELECT " + COLUMN_TIME_ID + " FROM "
			+ TABLE_TIMESTAMPS + " WHERE " + COLUMN_TIME_ITEM_ID + "='%d')";
	public static final String SQL_DELETE_LAST_TIMESTAMP = "DELETE FROM " + TABLE_TIMESTAMPS + " WHERE " + COLUMN_TIME_ID + " IN (SELECT " + COLUMN_TIME_ID + " FROM "
			+ TABLE_TIMESTAMPS + " WHERE " + COLUMN_TIME_ITEM_ID + "='%d' ORDER BY " + COLUMN_TIME_ID + " DESC LIMIT 1)";
	public static final String SQL_DELETE_ITEM = "DELETE FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ID + "='%d'";
	
	// Manifest file
	public static final String MANIFEST_TAG_ROOT = "habittracker";
	public static final String MANIFEST_TAG_VERSION = "version";
	public static final String MANIFEST_TAG_SQL_ITEMS = "SQL_Items";
	public static final String MANIFEST_TAG_SQL_TIMESTAMPS = "SQL_Timestamps";
	
	// Extras
	public static final String EXTRA_NAME = "extra_name";
	public static final String EXTRA_MAX = "extra_max";
	public static final String EXTRA_PERIOD	= "extra_period";
	
	// Sample Data
	public static final String[][] SAMPLE_DATA = { {"Fats, Oils, Sweets", "1", "0"}, {"Meat", "2", "0"}, {"Eggs", "2", "1"}, {"Dairy", "3", "2"}, {"Fruits", "4", "3"}, {"Vegetables", "5", "3"}, {"Breads, Carbs", "3", "2"}, {"Nuts", "4", "1"} };


	
	
}
