package com.avidandrew.habittracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;

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
		
		
		final Button button = (Button) findViewById(R.id.add_item);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO
				
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
