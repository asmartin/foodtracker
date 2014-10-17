package com.avidandrew.habittracker;

import static com.avidandrew.habittracker.Constants.EXTRA_MAX;
import static com.avidandrew.habittracker.Constants.EXTRA_NAME;
import static com.avidandrew.habittracker.Constants.EXTRA_PERIOD;
import static com.avidandrew.habittracker.Constants.PERIOD_DAILY;
import static com.avidandrew.habittracker.Constants.PERIOD_MONTHLY;
import static com.avidandrew.habittracker.Constants.PERIOD_NONE;
import static com.avidandrew.habittracker.Constants.PERIOD_WEEKLY;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Edit_Item extends Activity{
	private DBHelper dbHelper = new DBHelper(this);
	private RadioGroup periods;
	Item thisItem;

	//Views
	EditText item_name_field;
	EditText max_quantity_field;
	ImageButton save_button;
	ImageButton delete_item;
	ImageButton view_history;
	ImageButton reminders;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_item_activity);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		//Update text for those fields
		final String item_name = getIntent().getStringExtra(EXTRA_NAME);
		final int max_quant = getIntent().getIntExtra(EXTRA_MAX,  0);
		final int item_period = getIntent().getIntExtra(EXTRA_PERIOD, 0);

		//LINK VIEWS
		item_name_field = (EditText) findViewById(R.id.edit_item_name);
		max_quantity_field = (EditText) findViewById(R.id.edit_max_quantity);
		periods = (RadioGroup) findViewById(R.id.radiogroup_period_edit_item);
		save_button = (ImageButton) findViewById(R.id.imageButton_save);
		delete_item = (ImageButton) findViewById(R.id.imageButton_delete_item);
		view_history = (ImageButton) findViewById(R.id.imageButton_view_graph);
		reminders = (ImageButton) findViewById(R.id.imageButton_reminders);
		periods.check(getPeriodId(item_period));

		//Set fields to name of button and goal
		item_name_field.setText(item_name);
		max_quantity_field.setText(String.valueOf(max_quant));

		//SAVE CHANGES IMAGEBUTTON
		save_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				thisItem = dbHelper.getItem(item_name_field.getText().toString());

				//Check if item name is null
				if (thisItem == null) {
					Toast.makeText(getBaseContext(), R.string.MSG_ERROR_NOT_IN_DATABASE, Toast.LENGTH_SHORT).show();
				} else {
					// update the name and max quantity
					boolean error = false;
					String name = item_name_field.getText().toString();

					//if name exists in db show error
					if (dbHelper.itemNameExists(name) && !item_name.equals(name) ) {
						Toast.makeText(getBaseContext(), R.string.MSG_ERROR_NAME_DUPLICATE, Toast.LENGTH_SHORT).show();
						error = true;
					}
					//if the name is the same 
					else if (!thisItem.getName().equals(name)) {
						if (!thisItem.updateName(name)) {
							Toast.makeText(getBaseContext(), R.string.MSG_ERROR_UPDATE_NAME, Toast.LENGTH_SHORT).show();
							error = true;
						}
						String updated_name_msg = getResources().getString(R.string.MSG_INFO_UPDATE_NAME) + " "+ name;
						Toast.makeText(getBaseContext(), updated_name_msg, Toast.LENGTH_SHORT).show();
					}

					try {
						int max = Integer.parseInt(max_quantity_field.getText().toString());
						if (thisItem.getMaxInPeriod() != max) {
							if (!thisItem.updateMax(max)) {

								Toast.makeText(getBaseContext(), R.string.MSG_ERROR_UPDATE_GOAL, Toast.LENGTH_SHORT).show();
								error = true;
							}
							//Toast if updated max
							String update_max_msg = getResources().getString(R.string.MSG_INFO_UPDATE_GOAL) + " " + max;
							Toast.makeText(getBaseContext(), update_max_msg, Toast.LENGTH_SHORT).show();
						}
					} catch (NumberFormatException nfe) {
						Toast.makeText(getBaseContext(), R.string.MSG_ERROR_GOAL_NAN, Toast.LENGTH_SHORT).show();
						error = true;
					}


					///Update Period for Item
					error = updateItemsPeriod();

					if (!error) {
						// if no errors, return to the main activity
						finish();
					}
				}
			}
		});

		//DELETE IMAGEBUTTON
		delete_item.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Item.this);
				builder.setTitle(R.string.MSG_INFO_CONFIRM_DELETE_TITLE);
				builder.setMessage(R.string.MSG_INFO_CONFIRM_DELETE_TEXT);
				builder.setPositiveButton(R.string.BUTTON_YES, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						Item thisItem = dbHelper.getItem(item_name_field.getText().toString());
						if(thisItem == null){ Toast.makeText(getBaseContext(), R.string.MSG_ERROR_DELETE_ITEM, Toast.LENGTH_SHORT).show();}

						if(dbHelper.deleteItem(thisItem.getID()))
						{Toast.makeText(getBaseContext(), R.string.MSG_INFO_DELETE_SUCCESS, Toast.LENGTH_SHORT).show();
						finish();}

						else {Toast.makeText(getBaseContext(), R.string.MSG_ERROR_DELETE_ITEM, Toast.LENGTH_SHORT).show();}
					}

				});

				builder.setNegativeButton(R.string.BUTTON_NO, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {	}
				});

				AlertDialog alert = builder.create();
				alert.show();

			}
		});

		//VIEW HISTORY IMAGEBUTTON
		view_history.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent item_history = new Intent(getBaseContext(), Chart_Activity.class);
				startActivity(item_history);
			}
		});

		reminders.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent reminders = new Intent(getApplicationContext(), Reminders_Activity.class);
				startActivity(reminders);
				
			}
		});
		
		
	}

	private Boolean updateItemsPeriod(){

		try{
			RadioButton selectRadio = (RadioButton) findViewById(periods.getCheckedRadioButtonId());
			String period_label = selectRadio.getText().toString();
			int period_value = getPeriodByLabel(period_label);
			thisItem.updatePeriod(period_value);
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), R.string.MSG_NO_PERIOD_SELECTED, Toast.LENGTH_SHORT).show();
			return true;}

		return false;

	}

	private int getPeriodId(int period){
		switch (period) {
		case 0: return R.id.RadioButton_No_Period;
		case 1:	return R.id.RadioButton_Daily;
		case 2: return R.id.RadioButton_Weekly;
		case 3: return R.id.RadioButton_Monthly;
		default: return R.id.RadioButton_No_Period;

		}

	}

	private int getPeriodByLabel(String label) {
		if (label.equals(getResources().getString(R.string.PERIOD_NONE_LABEL))) {
			return PERIOD_NONE;
		} else if (label.equals(getResources().getString(R.string.PERIOD_DAILY_LABEL))) {
			return PERIOD_DAILY;
		} else if (label.equals(getResources().getString(R.string.PERIOD_WEEKLY_LABEL))) {
			return PERIOD_WEEKLY;
		} else if (label.equals(getResources().getString(R.string.PERIOD_MONTHLY_LABEL))) {
			return PERIOD_MONTHLY;
		}

		// if no period can be identified, return PERIOD_NONE
		return PERIOD_NONE;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
