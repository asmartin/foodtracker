package com.avidandrew.habittracker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableRow;
import android.widget.TextView;

public class Timestamp_View extends TableRow {

	private int itemID;
	//Creates a row of a timestamp
	public Timestamp_View(Context context, int itemID) {
		super(context);
		this.itemID = itemID;
		
		TextView timestamp = new TextView(context);
		
		addView(timestamp);
	}

	//Timestamp for object with itemName
	public Timestamp_View(Context context, String itemName) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	//timestamp of all items
	public Timestamp_View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Timestamp_View(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	

}
