package com.app.busmk2;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class main extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// First run
		boolean exists = (new File("/data/data/com.app.busmk2/notwelcomefirst"))
				.exists();

		if (!exists) {
			//Welcome note...
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Добредојдовте во aBusTripMK2, " +
					"за повеќе информации и начин на користење, " +
					"погледнете во 'More...' делот ")
				.setIcon(R.drawable.icon).setTitle(
					R.string.app_name).setCancelable(false).setPositiveButton(
					"OK..", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
			try {
				new File("/data/data/com.app.busmk2/notwelcomefirst").createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ImageView mapa = (ImageView) findViewById(R.id.map);
		mapa.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.map");
				startActivity(myIntent);
			}
		});

		ImageView lista = (ImageView) findViewById(R.id.list);
		lista.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.list");
				startActivity(myIntent);
			}
		});

		ImageView linii = (ImageView) findViewById(R.id.lines);
		linii.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.lines");
				startActivity(myIntent);
			}
		});

		ImageView raspored = (ImageView) findViewById(R.id.raspored);
		raspored.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://jsp.com.mk/VozenRed.aspx"));
				startActivity(myIntent);
			}
		});

		ImageView logo_exit = (ImageView) findViewById(R.id.upper);
		logo_exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				exit_dialog();
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

	private void exit_dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?").setIcon(
				R.drawable.icon).setTitle(R.string.app_name).setCancelable(
				false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
