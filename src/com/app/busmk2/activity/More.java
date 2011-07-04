package com.app.busmk2.activity;

import com.app.busmk2.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * A TabActivity class that connects the given tabs with different activities
 * 
 * @author drakuwa
 * 
 */
public class More extends TabActivity {

	/**
	 * An override of the onCreate method that initializes the tab objects
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_tab);

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, about.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("about").setIndicator("За Апликацијата",
				res.getDrawable(R.drawable.ic_tab_about)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, help.class);
		spec = tabHost.newTabSpec("help").setIndicator("Помош",
				res.getDrawable(R.drawable.ic_tab_help)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, contact.class);
		spec = tabHost.newTabSpec("contact").setIndicator("Контакт",
				res.getDrawable(R.drawable.ic_tab_contact)).setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}

}
