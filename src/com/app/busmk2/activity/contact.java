package com.app.busmk2.activity;

import com.app.busmk2.R;
import com.app.busmk2.model.Model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * Activity class that manages the given contact activity.
 * 
 * @author drakuwa
 * 
 */
public class contact extends Activity {

	// Initialization of the model.
	Model model = new Model(this);

	/**
	 * An override of the onCreate method that initializes the ImageView objects
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);

		/**
		 * The next 2 ImageView variables start new activities, opening the
		 * application profiles on Twitter and Facebook.
		 * 
		 */
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

		/**
		 * An ImageView variable that calls the default e-mail client of the
		 * Android device, if the release version of the OS is 2.2 or newer, and
		 * shows a Toast message if the release version is older.
		 */
		ImageView email = (ImageView) findViewById(R.id.email);
		email.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				if (!(Integer.parseInt(Build.VERSION.SDK) < 8)) {
					Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("mailto:drakuwa@gmail.com"));
					startActivity(myIntent);
				} else
					model.email_toast();
			}
		});

	}
}
