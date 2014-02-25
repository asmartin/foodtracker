package com.example.first_app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_main);
		
		Toast message = Toast.makeText(this, "Toasting", Toast.LENGTH_SHORT);
		message.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public void goToDetailActivity(View v ){
		
		Intent intent = new Intent(this, vegetables_detail_activity.class);
		startActivity(intent);
	}
	
	
}
