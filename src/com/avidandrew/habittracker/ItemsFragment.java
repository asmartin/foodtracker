package com.avidandrew.habittracker;

import static com.avidandrew.habittracker.Constants.*;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import com.avidandrew.habittracker.R;

public class ItemsFragment extends Fragment {
	private TableLayout layout = null;
	private int period = 0;
	
	public ItemsFragment() {
		super();
		
		period = this.getId();
	}

	public void setPeriod(int period) {
		this.period = period;
		
	}

	/**
	 * determines if there is data in the Items database; if so, display it; else, display sample data
	 */
	private void loadItems() {
		DBHelper db = new DBHelper(this.getActivity());
		ArrayList<Item> items = db.getItemsByPeriod(period);
		if (items == null || items.size() == 0) {
			// load sample data
			//loadSampleData();
			
			//TextView and params
			TextView noDataMessage = new TextView(this.getActivity());
			noDataMessage.setGravity(Gravity.CENTER_HORIZONTAL);
			noDataMessage.setPadding(10, 10, 10, 10);
			noDataMessage.setText(R.string.MSG_NO_ITEMS);
			
			layout.addView(noDataMessage);

		} else {
			// load data from database
			loadItemsView(items);


		}
	}


	/**
	 * Loads a list of items into the activity
	 * @param items
	 */
	private void loadItemsView(ArrayList<Item> items) {
		
		
		if (items != null) {			
			//Margins
			TableRow.LayoutParams rowMargins = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, 6.0f);
			rowMargins.setMargins(5, 5, 5, 5);

			//Create First row for Goal label
			TableRow title_row = new TableRow(getActivity());
			//Create and Set params
			TableRow.LayoutParams title_row_params = new LayoutParams();	
			title_row_params.gravity = Gravity.CENTER_HORIZONTAL;
			title_row_params.setMargins(5, 5, 5, 5);
			title_row.setLayoutParams(title_row_params);
			
			//Params variable for individual buttons
			TableRow.LayoutParams button_params = new LayoutParams();
			button_params.gravity = Gravity.CENTER_HORIZONTAL;
			button_params.setMargins(5, 0, 5, 0);
			
			// Minus Button Label 
			TextView title_minus = new TextView(getActivity());
			button_params.weight = 1;
			title_minus.setLayoutParams(button_params);
			title_minus.setText("Minus");
			title_row.addView(title_minus);
			
			//Item Button
			TextView title_item = new TextView(getActivity());
			title_item.setText("Item");
			button_params.weight = 3;
			title_item.setLayoutParams(button_params);
			title_row.addView(title_item);
			
			//Plus Button
			TextView title_plus = new TextView(getActivity());
			button_params.weight = 1;
			title_plus.setLayoutParams(button_params);
			title_plus.setText("Plus");
			title_row.addView(title_plus);
			
			//Add Goal Title
			button_params.weight = 1;
			TextView goal = new TextView(getActivity());
			goal.setText("Goal");
			goal.setLayoutParams(button_params);
			title_row.addView(goal);
			
			
			//Add First Row to View
			layout.addView(title_row);
			
			
			//Add items to period
			for (final Item item : items) {
				TableRow row = new ItemView(this.getActivity(), item);
				row.setLayoutParams(rowMargins);
				//add row to view

				layout.addView(row);
			}
		}
	}

	/**
	 * Loads sample data into the activity
	 */
	private void loadSampleData() {
		ArrayList<Item> sampleData = new ArrayList<Item>();
		for (String[] sample : SAMPLE_DATA) {
			if (sample.length > 1) {
				int max = Integer.parseInt(sample[1]);
				int period = Integer.parseInt(sample[2]);
				if(period == this.period){
					sampleData.add(new Item(this.getActivity(), sample[0], period, max));
				}
			}
		}

		loadItemsView(sampleData);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.itemsfragment_activity, container, false);
		//this.getActivity().setContentView(R.layout.itemsfragment_activity);
		layout = (TableLayout) rootView.findViewById(R.id.main_table);
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//Remove all views
		layout.removeAllViews();
		loadItems();
	}

}