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
	public ItemView(Context c, Item i) {
		super(c);
		
		item = i;

		//Create Minus button
		Button minus = new Button(c);
		minus.setText("-");
		addView(minus);
		minus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO
				System.out.println("Clicked");
				
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
		addView(plus);
		
		
		//Set Total
		TextView total_counter = new TextView(c);
		
		String out_of = "/" + item.getMaxQuantity();
		total_counter.setText(item.total_counter + out_of);
		addView(total_counter);
		/**
		 * Update Text if item added
		 */
		
		total_counter.addTextChangedListener(new TextWatcher() {
			
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
	
	public boolean increment() {
		return true;
	}
	
	public boolean decrement() {
		return true;
	}
	
	public int getValue() {
		return 0;
	}
	
	public boolean setValue(int newValue) {
		return true;
	}
}