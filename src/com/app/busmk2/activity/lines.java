package com.app.busmk2.activity;

import com.app.busmk2.R;
import com.app.busmk2.lines.a12;
import com.app.busmk2.lines.a15;
import com.app.busmk2.lines.a19;
import com.app.busmk2.lines.a2;
import com.app.busmk2.lines.a22;
import com.app.busmk2.lines.a24;
import com.app.busmk2.lines.a3;
import com.app.busmk2.lines.a41;
import com.app.busmk2.lines.a5;
import com.app.busmk2.lines.a65b;
import com.app.busmk2.lines.a7;
import com.app.busmk2.lines.a8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * An activity class that connects the ImageView objects of the lines layout
 * with the corresponding line activity classes
 * 
 * @author drakuwa
 * 
 */
public class lines extends Activity {

	/**
	 * An override of the onCreate method that initializes the ImageView objects
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lines);

		/**
		 * The next 12 ImageView variables start new activities with calling a
		 * new intent of the given corresponding classes.
		 */
		ImageView l2 = (ImageView) findViewById(R.id.line2);
		l2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a2.class);
				startActivity(myIntent);
			}
		});

		ImageView l3 = (ImageView) findViewById(R.id.line3);
		l3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a3.class);
				startActivity(myIntent);
			}
		});

		ImageView l5 = (ImageView) findViewById(R.id.line5);
		l5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a5.class);
				startActivity(myIntent);
			}
		});

		ImageView l7 = (ImageView) findViewById(R.id.line7);
		l7.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a7.class);
				startActivity(myIntent);
			}
		});

		ImageView l8 = (ImageView) findViewById(R.id.line8);
		l8.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a8.class);
				startActivity(myIntent);
			}
		});

		ImageView l12 = (ImageView) findViewById(R.id.line12);
		l12.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a12.class);
				startActivity(myIntent);
			}
		});

		ImageView l15 = (ImageView) findViewById(R.id.line15);
		l15.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a15.class);
				startActivity(myIntent);
			}
		});

		ImageView l19 = (ImageView) findViewById(R.id.line19);
		l19.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a19.class);
				startActivity(myIntent);
			}
		});

		ImageView l22 = (ImageView) findViewById(R.id.line22);
		l22.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a22.class);
				startActivity(myIntent);
			}
		});

		ImageView l24 = (ImageView) findViewById(R.id.line24);
		l24.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a24.class);
				startActivity(myIntent);
			}
		});

		ImageView l41 = (ImageView) findViewById(R.id.line41);
		l41.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a41.class);
				startActivity(myIntent);
			}
		});

		ImageView l65 = (ImageView) findViewById(R.id.line65);
		l65.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(lines.this, a65b.class);
				startActivity(myIntent);
			}
		});

	}
}