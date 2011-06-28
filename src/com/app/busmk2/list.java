package com.app.busmk2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class list extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		ImageView aer = (ImageView) findViewById(R.id.op_aerodrom);
		aer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.temp");
				startActivity(myIntent);
			}
		});
		
	}
}