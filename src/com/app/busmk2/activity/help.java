package com.app.busmk2.activity;

import com.app.busmk2.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * Simple activity class that shows the help layout
 * @author drakuwa
 *
 */
public class help extends Activity {

	/**
	 * On create of the activity, show the help layout
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
	}
}
