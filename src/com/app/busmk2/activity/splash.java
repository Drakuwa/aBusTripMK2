package com.app.busmk2.activity;

import com.app.busmk2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * First activity shown in the application, and a splash window. This class
 * shows a splash screen and dismisses it after a while
 * 
 * @author drakuwa
 */
public class splash extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 2000;

	/**
	 * Override of the onCreate method that calls a new runnable after a while.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		/**
		 * Starts a new activity given with the novIntent variable.
		 */
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent novIntent = new Intent(splash.this, main.class);
				startActivity(novIntent);
				finish();

			}
		}, 2300);
	}

}