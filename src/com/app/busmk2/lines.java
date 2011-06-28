package com.app.busmk2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class lines extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lines);
		
		ImageView l2 = (ImageView) findViewById(R.id.line2);
		l2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a2");
				startActivity(myIntent);
			}
		});
		
		ImageView l3 = (ImageView) findViewById(R.id.line3);
		l3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a3");
				startActivity(myIntent);
			}
		});
		
		ImageView l5 = (ImageView) findViewById(R.id.line5);
		l5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a5");
				startActivity(myIntent);
			}
		});
		
		ImageView l7 = (ImageView) findViewById(R.id.line7);
		l7.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a7");
				startActivity(myIntent);
			}
		});
		
		ImageView l8 = (ImageView) findViewById(R.id.line8);
		l8.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a8");
				startActivity(myIntent);
			}
		});
		
		ImageView l12 = (ImageView) findViewById(R.id.line12);
		l12.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a12");
				startActivity(myIntent);
			}
		});
		
		ImageView l15 = (ImageView) findViewById(R.id.line15);
		l15.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a15");
				startActivity(myIntent);
			}
		});
		
		ImageView l19 = (ImageView) findViewById(R.id.line19);
		l19.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a19");
				startActivity(myIntent);
			}
		});
		
		ImageView l22 = (ImageView) findViewById(R.id.line22);
		l22.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a22");
				startActivity(myIntent);
			}
		});
		
		ImageView l24 = (ImageView) findViewById(R.id.line24);
		l24.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a24");
				startActivity(myIntent);
			}
		});
		
		ImageView l41 = (ImageView) findViewById(R.id.line41);
		l41.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a41");
				startActivity(myIntent);
			}
		});
		
		ImageView l65 = (ImageView) findViewById(R.id.line65);
		l65.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.a65b");
				startActivity(myIntent);
			}
		});
		
	}
}