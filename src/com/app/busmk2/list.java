package com.app.busmk2;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class list extends Activity {

	ArrayList<String> gjorce = new ArrayList<String>();
	ArrayList<String> karpos = new ArrayList<String>();
	ArrayList<String> centar = new ArrayList<String>();
	ArrayList<String> cair = new ArrayList<String>();
	ArrayList<String> gazibaba = new ArrayList<String>();
	ArrayList<String> aerodrom = new ArrayList<String>();
	ArrayList<String> kvoda = new ArrayList<String>();
	ArrayList<String> butel = new ArrayList<String>();
	ArrayList<String> shutka = new ArrayList<String>();

	// Model model = new Model();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

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

		ImageView aer = (ImageView) findViewById(R.id.op_aerodrom);
		aer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				create_list(aerodromlist);
			}
		});

		ImageView but = (ImageView) findViewById(R.id.op_butel);
		but.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				create_list(butellist);
			}
		});

		ImageView gzb = (ImageView) findViewById(R.id.op_gazibaba);
		gzb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				create_list(gazibabalist);
			}
		});

		ImageView gpt = (ImageView) findViewById(R.id.op_gpetrov);
		gpt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				create_list(gjorcelist);
			}
		});

		ImageView krp = (ImageView) findViewById(R.id.op_karpos);
		krp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				create_list(karposlist);
			}
		});

		ImageView kvd = (ImageView) findViewById(R.id.op_kvoda);
		kvd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				create_list(kvodalist);
			}
		});

		ImageView cnt = (ImageView) findViewById(R.id.op_centar);
		cnt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				create_list(centarlist);
			}
		});

		ImageView cai = (ImageView) findViewById(R.id.op_cair);
		cai.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				create_list(cairlist);
			}
		});

		ImageView stk = (ImageView) findViewById(R.id.op_shutka);
		stk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				create_list(shutkalist);
			}
		});
	}

	public void create_list(final String[] items) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Избери станица:");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				String txt = items[item];
				createDialog(txt);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void createDialog(final String txt) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
				.setMessage(
						"Кликнавте на: "
								+ txt
								+ ", Дали сакате да ги погледнете линиите кои што поминуваат таму?")
				.setIcon(R.drawable.icon).setTitle(R.string.app_name)
				.setCancelable(true).setPositiveButton("Да..",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								ArrayList<String> korisni_linii = new ArrayList<String>();

								DataBaseHelper Db = new DataBaseHelper(null);
								Db = new DataBaseHelper(getApplicationContext());
								Db.openDataBase();

								Cursor c = Db.getStanica(txt);
								if (c.moveToFirst()) {
									String _id = c.getString(0); // id na
									// kliknata
									// stanica...
									Cursor c2 = Db.getUsefulLinii(_id);
									if (c2.moveToFirst()) {
										do {
											String id_korisna_linija = c2
													.getString(0);
											Cursor c3 = Db
													.getLinija(id_korisna_linija);
											String ime_linija = c3.getString(0);
											korisni_linii.add(ime_linija);
										} while (c2.moveToNext());
									}
								}

								String linii = "Следниве линии поминуваат на кликнатата станица: "
										+ korisni_linii;

								final_dialog(linii);

								Db.close();
							}

						});
		builder.setNegativeButton("Откажи",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void final_dialog(final String txt) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(txt).setIcon(R.drawable.icon).setTitle(
				R.string.app_name).setCancelable(false).setPositiveButton(
				"OK..", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

}