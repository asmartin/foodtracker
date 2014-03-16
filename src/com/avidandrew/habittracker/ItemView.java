package com.avidandrew.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import static com.avidandrew.habittracker.Constants.*;

@SuppressLint("ViewConstructor")
public class ItemView extends TableRow {
	private Item item = null;
	private TextView counterView = null;
	
	public ItemView(Context c, Item i) {
		super(c);
		
		item = i;
		counterView = new TextView(c);
		
		TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT);
		params.weight = 1;
		params.gravity =  Gravity.CENTER_VERTICAL;
		counterView.setLayoutParams(params);
		
		//Create Minus button
		Button minus = new Button(c);
		minus.setText(BUTTON_MINUS);
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
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent edit_item = new Intent(getContext(), Edit_Item.class);
				
				// make the name and max variables available in the new activity
				edit_item.putExtra(EXTRA_NAME, item.getName());
				edit_item.putExtra(EXTRA_MAX, item.getMaxInPeriod());
				
				// launch the new activity
				getContext().startActivity(edit_item);
			}
		});
		
		
		//Create the plus button
		Button plus = new Button(c);
		plus.setText(BUTTON_PLUS);
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
		counterView.setText(item.getCounterValue() + "/" + item.getMaxInPeriod());
	}
}
