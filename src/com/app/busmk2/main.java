package com.app.busmk2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * The main dashboard class which connects the ImageView objects with their
 * functionalities.
 * 
 * @author drakuwa
 */
public class main extends Activity {

	// Initialization of the model
	Model model = new Model(this);

	/**
	 * An override of the onCreate method that initializes the ImageView objects
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/**
		 * Calls the model First run method to check if the application is ran
		 * for the first time
		 */
		model.first_run();

		/**
		 * The next 3 ImageView variables start new activities with calling a
		 * new intent of the given classes.
		 */
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

		/**
		 * An ImageView variable that redirects the application to an Internet
		 * location given with its URL.
		 */
		ImageView raspored = (ImageView) findViewById(R.id.raspored);
		raspored.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://jsp.com.mk/VozenRed.aspx"));
				startActivity(myIntent);
			}
		});

		/**
		 * An ImageView variable that calls the exit_dialog() on click.
		 */
		ImageView logo_exit = (ImageView) findViewById(R.id.upper);
		logo_exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				exit_dialog();
			}
		});

		/**
		 * An ImageView variable that starts the More activity on click
		 */
		ImageView more = (ImageView) findViewById(R.id.more);
		more.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk2", "com.app.busmk2.More");
				startActivity(myIntent);
			}
		});
	}

	/**
	 * Private method that creates and shows an exit dialog window, and exits the
	 * application on positive answer.
	 */
	private void exit_dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Дали сте сигурни дека саката да ја напуштите апликацијата?")
				.setIcon(R.drawable.icon).setTitle(R.string.app_name)
				.setCancelable(false).setPositiveButton("OK",
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
