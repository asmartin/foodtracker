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

public class Edit_Item extends Activity{
	private DBHelper dbHelper = new DBHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_item_activity);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		//Update text for those fields
		String item_name = getIntent().getStringExtra("name");
		int max_quant = getIntent().getIntExtra("max",  0);
		EditText item_name_field2 = (EditText) findViewById(R.id.edit_item_name);
		EditText max_quantity_field2 = (EditText) findViewById(R.id.edit_max_quantity);
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
				Item thisItem = dbHelper.getItem(getBaseContext(), nameText);
				if (thisItem == null) {
					Toast.makeText(getBaseContext(), "Item cannot be updated - not found in database", Toast.LENGTH_SHORT).show();
				} else {
					// update the name and max quantity
					// TODO: check return values
					String name = item_name_field.getText().toString();
					if (dbHelper.itemNameExists(getBaseContext(), name)) {
						Toast.makeText(getBaseContext(), "An item with the same name already exists", Toast.LENGTH_SHORT).show();
					}

					else if (!thisItem.getName().equals(name)) {
						if (!thisItem.updateName(name)) {
							Toast.makeText(getBaseContext(), "Error updating name", Toast.LENGTH_SHORT).show();
						}
						Toast.makeText(getBaseContext(), "Item name set to " + name, Toast.LENGTH_SHORT).show();
					}


					try {
						int max = Integer.parseInt(max_quantity_field.getText().toString());
						if (thisItem.getMaxServings() != max) {
							if (!thisItem.updateMax(max)) {
								Toast.makeText(getBaseContext(), "Error updating max", Toast.LENGTH_SHORT).show();
							}
							Toast.makeText(getBaseContext(), "Max set to " + max, Toast.LENGTH_SHORT).show();
						}
					} catch (NumberFormatException nfe) {
						Toast.makeText(getBaseContext(), "Error: Max must be a number", Toast.LENGTH_SHORT).show();
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
			    builder.setTitle("Confirm");
			    builder.setMessage("Are you sure?");
			    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			        public void onClick(DialogInterface dialog, int which) {
			          
			        }

			    });

			    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

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
