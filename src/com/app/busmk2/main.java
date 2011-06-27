package com.app.busmk2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class main extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ImageView mapa = (ImageView) findViewById(R.id.map);
		mapa.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.temp");
				startActivity(myIntent);
			}
		});

		ImageView lista = (ImageView) findViewById(R.id.list);
		lista.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.temp");
				startActivity(myIntent);
			}
		});

		ImageView linii = (ImageView) findViewById(R.id.lines);
		linii.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.temp");
				startActivity(myIntent);
			}
		});

		ImageView raspored = (ImageView) findViewById(R.id.raspored);
		raspored.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.temp");
				startActivity(myIntent);
			}
		});

		ImageView logo_exit = (ImageView) findViewById(R.id.upper);
		logo_exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				finish();
			}
		});

		ImageView more = (ImageView) findViewById(R.id.more);
		more.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.More");
				startActivity(myIntent);
			}
		});
	}
}
