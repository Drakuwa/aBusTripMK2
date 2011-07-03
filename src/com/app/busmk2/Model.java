package com.app.busmk2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class Model {

	private Context ctx;

	public Model(Context context) {
		ctx = context;
	}

	public void email_toast() {
		Toast
				.makeText(
						ctx,
						"Android верзија "
								+ Build.VERSION.RELEASE
								+ " не подржува директно повикување "
								+ "на Email клиент. Испратете mail на drakuwa@gmail.com",
						Toast.LENGTH_LONG).show();
	}

	public void first_run() {
		boolean exists = (new File("/data/data/com.app.busmk2/notwelcomefirst"))
				.exists();

		if (!exists) {
			// Welcome note...
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage(
					"Добредојдовте во aBusTripMK2, "
							+ "за повеќе информации и начин на користење, "
							+ "погледнете во 'More...' делот ").setIcon(
					R.drawable.icon).setTitle(R.string.app_name).setCancelable(
					false).setPositiveButton("OK..",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
			try {
				new File("/data/data/com.app.busmk2/notwelcomefirst")
						.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void first_run_map(MapController mc) {
		boolean exists = (new File("/data/data/com.app.busmk2/notfirst")).exists();
		if (!exists) {
			GeoPoint initial = new GeoPoint(41995912, 21431454);
			mc.setZoom(15);
			mc.setCenter(initial);
			try {
				new File("/data/data/com.app.busmk2/notfirst").createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("xxxx", "First RUN!!! :D");
		}
	}

	public void final_dialog(final String txt) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(txt).setIcon(R.drawable.busmarker).setTitle(
				R.string.app_name).setCancelable(false).setPositiveButton(
				"OK..", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void create_list(final String[] items) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
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
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
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
								Db = new DataBaseHelper(ctx);
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

}