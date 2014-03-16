package com.avidandrew.habittracker;

import com.example.first_app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static com.avidandrew.habittracker.Constants.*;

public class Edit_Item extends Activity{
	private DBHelper dbHelper = new DBHelper(this);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_item_activity);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		//Update text for those fields
		final String item_name = getIntent().getStringExtra(EXTRA_NAME);
		final int max_quant = getIntent().getIntExtra(EXTRA_MAX,  0);
		
		//Get fields
		EditText item_name_field2 = (EditText) findViewById(R.id.edit_item_name);
		EditText max_quantity_field2 = (EditText) findViewById(R.id.edit_max_quantity);
		
		//Set fields to name of button and goal
		item_name_field2.setText(item_name);
		max_quantity_field2.setText(String.valueOf(max_quant));

		final String nameText = item_name_field2.getText().toString();

		//On Click listener for the save changes button
		Button save_button = (Button) findViewById(R.id.save_changes);
		save_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText item_name_field = (EditText) findViewById(R.id.edit_item_name);
				EditText max_quantity_field = (EditText) findViewById(R.id.edit_max_quantity);
				Item thisItem = dbHelper.getItem(nameText);

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

								Toast.makeText(getBaseContext(), R.string.MSG_ERROR_UPDATE_MAX, Toast.LENGTH_SHORT).show();
								error = true;
							}
							//Toast if updated max
							String update_max_msg = getResources().getString(R.string.MSG_INFO_UPDATE_MAX) + " " + max;
							Toast.makeText(getBaseContext(), update_max_msg, Toast.LENGTH_SHORT).show();
						}
					} catch (NumberFormatException nfe) {
						Toast.makeText(getBaseContext(), R.string.MSG_ERROR_MAX_NAN, Toast.LENGTH_SHORT).show();
						error = true;
					}

					if (!error) {
						// if no errors, return to the main activity
						finish();
					}
				}
			}
		});

		//On Click Listener for the Delete Item button

		Button delete_item = (Button) findViewById(R.id.delete_item);
		delete_item.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Item.this);
				builder.setTitle(R.string.MSG_INFO_CONFIRM_DELETE_TITLE);
				builder.setMessage(R.string.MSG_INFO_CONFIRM_DELETE_TEXT);
				builder.setPositiveButton(R.string.BUTTON_YES, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						Item thisItem = dbHelper.getItem(nameText);
						if(thisItem == null){

							Toast.makeText(getBaseContext(), R.string.MSG_ERROR_DELETE_ITEM, Toast.LENGTH_SHORT).show();
						}

						if(dbHelper.deleteItem(thisItem.getID())){
							Toast.makeText(getBaseContext(), R.string.MSG_INFO_DELETE_SUCCESS, Toast.LENGTH_SHORT).show();
							finish();
						}
						else {Toast.makeText(getBaseContext(), R.string.MSG_ERROR_DELETE_ITEM, Toast.LENGTH_SHORT).show();}

					}

				});

				builder.setNegativeButton(R.string.BUTTON_NO, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

				AlertDialog alert = builder.create();
				alert.show();

			}
		});

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
