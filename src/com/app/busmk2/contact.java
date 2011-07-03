package com.app.busmk2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class contact extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		
		ImageView twitter = (ImageView) findViewById(R.id.twitter);
		twitter.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://twitter.com/aBusTripMK"));
				startActivity(myIntent);
			}
		});
		
		ImageView facebook = (ImageView) findViewById(R.id.facebook);
		facebook.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://www.facebook.com/aBusTripMK"));
				startActivity(myIntent);
			}
		});
		
		ImageView email = (ImageView) findViewById(R.id.email);
		email.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("mailto:drakuwa@gmail.com"));
				startActivity(myIntent);
			}
		});
		
	}
}
