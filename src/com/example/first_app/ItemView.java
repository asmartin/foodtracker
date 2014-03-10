package com.example.first_app;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class ItemView extends TableRow {
	private Item item = null;
	private TextView counterView = null;
	private int totalCounter = 0;
	public ItemView(Context c, Item i) {
		super(c);
		
		item = i;
		counterView = new TextView(c);

		//Create Minus button
		Button minus = new Button(c);
		minus.setText("-");
		addView(minus);
		minus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				decrement();
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
		plus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				increment();
			}
		});
		addView(plus);
		
		
		//Set Total Counter
		setCounterValue(totalCounter);
		addView(counterView);
		/**
		 * Update Text if item added
		 */
		
		counterView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	
	}
	
	public void increment() {
		totalCounter++;
		setCounterValue(totalCounter);
	}
	
	public void decrement() {
		totalCounter--;
		setCounterValue(totalCounter);
	}
	
	public int getCounterValue() {
		return totalCounter;
	}
	
	private void setCounterValue(int newValue) {
		counterView.setText(newValue + "/" + item.getMaxQuantity());
	}
}