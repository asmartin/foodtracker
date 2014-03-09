package com.example.first_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class SQLiteHelper extends SQLiteOpenHelper{
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String ITEM_NAME = "name";
	
	public static final String DATABASE_NAME = "db";
	public static final String DATABASE_TABLE = "items_tracker";
	public static final int DATABASE_VERSION = 1;
	public static final int MAX_QUANTITY = 0;
	
	  private static final String DATABASE_CREATE = "Create Items Table"
		      + DATABASE_TABLE + "(" + KEY_ROWID
		      + "integer primary key autoincrement, " + ITEM_NAME + "item name" + MAX_QUANTITY
		      + "maximum quantity of serving);";
	
	   
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
