package com.avidandrew.habittracker;

public class Constants {
	public static final String APP_NAME = "Habit Tracker";
	public static final String DATABASE_NAME = "habittracker.db";	// name of the database
	
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

	// Buttons
	public static final String BUTTON_YES = "YES";
	public static final String BUTTON_NO = "NO";
	public static final String BUTTON_MINUS = "-";
	public static final String BUTTON_PLUS = "+";	
	
	// Message Strings
	public static final String MSG_INFO_UPDATE_NAME = "Item name set to %s";
	public static final String MSG_INFO_UPDATE_MAX = "Max set to %d";
	public static final String MSG_INFO_ADD_SUCCESS = "Item Added. Hit back to return to the list.";
	public static final String MSG_INFO_CONFIRM_DELETE_TITLE = "Confirm Delete";
	public static final String MSG_INFO_CONFIRM_DELETE_TEXT = "Are you sure you want to delete this item?";
	public static final String MSG_INFO_DELETE_SUCCESS = "Item deleted successfully";
	
	public static final String MSG_ERROR_INCREMENT = "Error increasing count of %s";
	public static final String MSG_ERROR_DECREMENT = "Error decreasing count of %s";
	public static final String MSG_ERROR_NAME_EMPTY = "Item Name cannot be empty";
	public static final String MSG_ERROR_MAX_EMPTY = "Max Quantity cannot be empty";
	public static final String MSG_ERROR_MAX_NAN = "Error: Max must be a number";
	public static final String MSG_ERROR_NAME_DUPLICATE = "An item with the same name already exists";
	public static final String MSG_ERROR_NOT_IN_DATABASE = "Item cannot be updated - not found in database";
	public static final String MSG_ERROR_UPDATE_NAME = "Error updating name";
	public static final String MSG_ERROR_UPDATE_MAX = "Error updating max";
	public static final String MSG_ERROR_DELETE_ITEM = "Error deleting Item";
	
}