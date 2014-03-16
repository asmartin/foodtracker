package com.avidandrew.habittracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.first_app.R;
import static com.avidandrew.habittracker.Constants.*;

public class Add_Item_Activity extends Activity{
	private DBHelper dbHelper = new DBHelper(this);

	public Add_Item_Activity(){
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_item_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		final Button button = (Button) findViewById(R.id.add_item_button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText item_name_field = (EditText) findViewById(R.id.add_item_field);
				EditText quantity_field =(EditText) findViewById(R.id.max_quant_field);
				String item_name = item_name_field.getText().toString();
				
				
				//Check that fields aren't empty
				if(item_name.equals("")){
					Toast.makeText(getBaseContext(), R.string.MSG_ERROR_NAME_EMPTY, Toast.LENGTH_SHORT).show();
				}
				else if (quantity_field.getText().toString().equals("")){
					Toast.makeText(getBaseContext(), R.string.MSG_ERROR_GOAL_EMPTY, Toast.LENGTH_SHORT).show();
				}
				else if (dbHelper.itemNameExists(item_name)) {
					Toast.makeText(getBaseContext(), R.string.MSG_ERROR_NAME_DUPLICATE, Toast.LENGTH_SHORT).show();
				}
				else{
					//If not empty then add to DB
					int max_quantity = Integer.parseInt(quantity_field.getText().toString());
					// TODO: refactor to not use exception, check for good and handle appropriately
					new Item(getBaseContext(), item_name, max_quantity);
					//Clear Fields
					item_name_field.setText("");
					quantity_field.setText(null);
					Toast.makeText(getBaseContext(), R.string.MSG_INFO_ADD_SUCCESS, Toast.LENGTH_SHORT).show();

				}	

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
