package com.app.busmk2.activity;

import java.io.IOException;
import java.util.ArrayList;

import com.app.busmk2.model.DataBaseHelper;
import com.app.busmk2.R;
import com.app.busmk2.model.Model;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * An activity class that shows a list of ImageViews, with each one of them
 * showing a dialog of selectable items.
 * 
 * @author drakuwa
 * 
 */
public class list extends Activity {

	/**
	 * Set initial variables that will be used.
	 */
	ArrayList<String> gjorce = new ArrayList<String>();
	ArrayList<String> karpos = new ArrayList<String>();
	ArrayList<String> centar = new ArrayList<String>();
	ArrayList<String> cair = new ArrayList<String>();
	ArrayList<String> gazibaba = new ArrayList<String>();
	ArrayList<String> aerodrom = new ArrayList<String>();
	ArrayList<String> kvoda = new ArrayList<String>();
	ArrayList<String> butel = new ArrayList<String>();
	ArrayList<String> shutka = new ArrayList<String>();

	// Initialize the model.
	Model model = new Model(this);

	/**
	 * An override of the onCreate method that initializes the ImageView objects
	 * and initializes the array lists with values from the database.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		/**
		 * Open the database and fill in the array lists with values read from
		 * the corresponding database tables.
		 */
		DataBaseHelper myDb = new DataBaseHelper(null);
		myDb = new DataBaseHelper(this);

		{
			try {

				myDb.createDataBase();

			} catch (IOException ioe) {

				throw new Error("Unable to create database");

			}

			try {

				myDb.openDataBase();

			} catch (SQLException sqle) {

				throw sqle;

			}

			myDb.getReadableDatabase();

			Cursor c = myDb.getStanici();

			if (c.moveToFirst()) {
				do {
					String ime = c.getString(1);
					String naselba = c.getString(4);
					if (naselba.equalsIgnoreCase("Ѓ.Петров")
							&& ime.endsWith("А")) {
						gjorce.add(ime);
					}
					if (naselba.equalsIgnoreCase("Карпош") && ime.endsWith("А")) {
						karpos.add(ime);
					}
					if (naselba.equalsIgnoreCase("Центар") && ime.endsWith("А")) {
						centar.add(ime);
					}
					if (naselba.equalsIgnoreCase("Чаир") && ime.endsWith("А")) {
						cair.add(ime);
					}
					if (naselba.equalsIgnoreCase("Гази Баба")
							&& ime.endsWith("А")) {
						gazibaba.add(ime);
					}
					if (naselba.equalsIgnoreCase("Аеродром")
							&& ime.endsWith("А")) {
						aerodrom.add(ime);
					}
					if (naselba.equalsIgnoreCase("К.Вода") && ime.endsWith("А")) {
						kvoda.add(ime);
					}
					if (naselba.equalsIgnoreCase("Бутел") && ime.endsWith("А")) {
						butel.add(ime);
					}
					if (naselba.equalsIgnoreCase("Ш.Оризари")
							&& ime.endsWith("А")) {
						shutka.add(ime);
					}
				} while (c.moveToNext());
			}

			myDb.close();
		}

		/**
		 * Create string arrays from the array lists.
		 */
		final String gjorcelist[] = (String[]) gjorce.toArray(new String[gjorce
				.size()]);
		final String karposlist[] = (String[]) karpos.toArray(new String[karpos
				.size()]);
		final String centarlist[] = (String[]) centar.toArray(new String[centar
				.size()]);
		final String cairlist[] = (String[]) cair.toArray(new String[cair
				.size()]);
		final String gazibabalist[] = (String[]) gazibaba
				.toArray(new String[gazibaba.size()]);
		final String aerodromlist[] = (String[]) aerodrom
				.toArray(new String[aerodrom.size()]);
		final String kvodalist[] = (String[]) kvoda.toArray(new String[kvoda
				.size()]);
		final String butellist[] = (String[]) butel.toArray(new String[butel
				.size()]);
		final String shutkalist[] = (String[]) shutka.toArray(new String[shutka
				.size()]);

		/**
		 * The next 9 ImageView variables call the create_list() method from the
		 * model, passing an array of strings to it.
		 */
		ImageView aer = (ImageView) findViewById(R.id.op_aerodrom);
		aer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				model.create_list(aerodromlist);
			}
		});

		ImageView but = (ImageView) findViewById(R.id.op_butel);
		but.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				model.create_list(butellist);
			}
		});

		ImageView gzb = (ImageView) findViewById(R.id.op_gazibaba);
		gzb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				model.create_list(gazibabalist);
			}
		});

		ImageView gpt = (ImageView) findViewById(R.id.op_gpetrov);
		gpt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				model.create_list(gjorcelist);
			}
		});

		ImageView krp = (ImageView) findViewById(R.id.op_karpos);
		krp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				model.create_list(karposlist);
			}
		});

		ImageView kvd = (ImageView) findViewById(R.id.op_kvoda);
		kvd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				model.create_list(kvodalist);
			}
		});

		ImageView cnt = (ImageView) findViewById(R.id.op_centar);
		cnt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				model.create_list(centarlist);
			}
		});

		ImageView cai = (ImageView) findViewById(R.id.op_cair);
		cai.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				model.create_list(cairlist);
			}
		});

		ImageView stk = (ImageView) findViewById(R.id.op_shutka);
		stk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				model.create_list(shutkalist);
			}
		});
	}

}