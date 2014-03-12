package com.avidandrew.habittracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.first_app.R;

public class Add_Item_Activity extends Activity{

	public Add_Item_Activity(){
		//TODO
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
				EditText quantiy_field =(EditText) findViewById(R.id.max_quant_field);

				//Check that fields aren't empty
				if(item_name_field.getText().toString().equals("")){
					Toast.makeText(getBaseContext(), "Item Name cannot be empty", Toast.LENGTH_SHORT).show();
				}
				
				else if (quantiy_field.getText().toString().equals("")){
					Toast.makeText(getBaseContext(), "Quantity cannot be empty", Toast.LENGTH_SHORT).show();
				}
				//If not empty then add to DB
				else{
					
					
					String item_name = item_name_field.getText().toString(); 
					int max_quantity = Integer.parseInt(quantiy_field.getText().toString());
					Item newItem = new Item(getBaseContext(), item_name, max_quantity);
					//Clear Fields
					item_name_field.setText("");
					quantiy_field.setText(null);
					Toast.makeText(getBaseContext(), "Item Added", Toast.LENGTH_SHORT).show();

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
