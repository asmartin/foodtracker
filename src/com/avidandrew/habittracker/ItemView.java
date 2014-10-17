package com.avidandrew.habittracker;

import com.avidandrew.habittracker.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
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

		//CREATE MINUS BUTTON
		Button minus = new Button(c);
		minus.setText(R.string.BUTTON_MINUS);
		
		//Minus Styling Parameters 
		TableRow.LayoutParams params_minus_button = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT);
		params_minus_button.weight = 1;
		params_minus_button.setMargins(5, 5, 5, 5);
		
	    int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
	    params_minus_button.height = minHeight;
	    params_minus_button.width = minHeight;
		
		minus.setLayoutParams(params_minus_button);
		
		minus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		minus.setBackgroundResource(R.drawable.minus_button_style);
		minus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				item.decrement();
				setCounterValue();
			}
		});
		//Add Minus Button to Table Row
		addView(minus);

		//CREATE ITEM BUTTON
		Button button = new Button(c);
		button.setText(item.item_name);
		// Add Params for Item Button
		TableRow.LayoutParams item_params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT);
		item_params.weight = 3;
		item_params.setMargins(5, 5, 5, 5);
		button.setLayoutParams(item_params);
		
		button.setBackgroundResource(R.drawable.button_style);
		
		button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent edit_item = new Intent(getContext(), Edit_Item.class);

				// make the name and max variables available in the new activity
				edit_item.putExtra(EXTRA_NAME, item.getName());
				edit_item.putExtra(EXTRA_MAX, item.getMaxInPeriod());
				edit_item.putExtra(EXTRA_PERIOD, item.getPeriod());

				// launch the new activity
				getContext().startActivity(edit_item);
			}
		});
		addView(button);


		//CREATE PLUS BUTTON
		Button plus = new Button(c);
		plus.setText(R.string.BUTTON_PLUS);
		//Button Parameters 
		TableRow.LayoutParams params_plus_button = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT);
		params_plus_button.weight = 1;
		params_plus_button.setMargins(5, 5, 5, 5);
		plus.setLayoutParams(params_plus_button);
	    int minPlusHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
	    params_plus_button.height = minPlusHeight;
	    params_plus_button.width = minPlusHeight;
		
		
		plus.setBackgroundResource(R.drawable.plus_button_style);
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
		
		//Button Parameters 
		TableRow.LayoutParams text_view_params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT);
		text_view_params.weight = 1;
	    int minTextViewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics());
		text_view_params.width = minTextViewSize;
		counterView.setLayoutParams(text_view_params);
		counterView.setText(item.getCounterValue() + "/" + item.getMaxInPeriod());
	}
}
