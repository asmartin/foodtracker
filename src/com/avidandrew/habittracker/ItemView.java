package com.avidandrew.habittracker;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class ItemView extends TableRow {
	private Item item = null;
	private TextView counterView = null;
	public ItemView(Context c, Item i) {
		super(c);
		
		item = i;
		counterView = new TextView(c);

		//Create Minus button
		Button minus = new Button(c);
		minus.setText("-");
		addView(minus);
		minus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		minus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				item.decrement();
				setCounterValue();
			}
		});
		
		//Create Item Button
		Button button = new Button(c);
		button.setText(item.item_name);
		addView(button);
		button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		
		
		//Create the plus button
		Button plus = new Button(c);
		plus.setText("+");
		plus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		plus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				item.increment();
				setCounterValue();
			}
		});
		addView(plus);
		
		//Set Total Counter
		setCounterValue();
		addView(counterView);
		
	}
	
	private void setCounterValue() {
		counterView.setText(item.getCounterValue() + "/" + item.getMaxServings());
	}
}
