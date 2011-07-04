package com.app.busmk2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * A model class that resolves some of the business logic in the application.
 * Also as a part of the MVC(Model View Controller) programming pattern. It
 * presents changes to the UI thread through the Controller classes.
 * 
 * @author drakuwa
 */
public class Model {

	private Context ctx;

	/**
	 * Constructor of the Model class which initializes the activity context.
	 * 
	 * @param context
	 */
	public Model(Context context) {
		ctx = context;
	}

	/**
	 * Method that creates and shows a Toast message with the specified Android
	 * release version.
	 */
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

	/**
	 * Method that checks if the application is run for the first time. It
	 * checks for the existence of an empty file in the application folder, and
	 * if it doesn't it creates an AlertDialog with the welcome message, and it
	 * creates the file.
	 */
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

	/**
	 * Method that checks if the map is accessed for the first time. If it is
	 * the first time, the method positions the map in the center of Skopje.
	 * This function uses the same method of checking as the previous
	 * first_run(). The parameter passed is the MapController which is used to
	 * manipulate the state of the MapActivity
	 * 
	 * @param mc
	 */
	public void first_run_map(MapController mc) {
		boolean exists = (new File("/data/data/com.app.busmk2/notfirst"))
				.exists();
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

	/**
	 * Method that creates and shows an AlertDialog with a message passed with
	 * the txt parameter, and a PositiveButton "OK..."
	 * 
	 * @param txt
	 */
	public void final_dialog(final String txt) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(txt).setIcon(R.drawable.icon).setTitle(
				R.string.app_name).setCancelable(false).setPositiveButton(
				"OK..", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Method that creates a dialog with a list of selectable items passed with
	 * the items parameter (an array of strings), and calls createDialog()
	 * method on click.
	 * 
	 * @param items
	 */
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

	/**
	 * Method that creates a dialog window asking if the user wants to access
	 * the given functionality, and performs a database search on positive
	 * answer, for the given txt parameter. The algorithm searches for the
	 * appearance of the bus station with the name given in the txt parameter,
	 * in the bus lines database table, and displays the bus lines which pass
	 * through that station
	 * 
	 * @param txt
	 */
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
									// kliknata stanica...
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

	/**
	 * Method that creates and shows a GPS Disabled alert, and calls the
	 * showGpsOptions() method on positive click.
	 */
	public void createGpsDisabledAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder
				.setMessage(
						"Вашиот GPS е оневозможен! Потребно е да биде активен за  прецизно пронаоѓање на Вашата локација. Дали би сакале да го овозможите?")
				.setIcon(R.drawable.icon).setTitle(R.string.app_name)
				.setCancelable(false).setPositiveButton("Овозможи GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								showGpsOptions();
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

	/**
	 * Method that calls the Location settings and allows the user to enable or
	 * disable the GPS
	 */
	private void showGpsOptions() {
		Intent gpsOptionsIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		ctx.startActivity(gpsOptionsIntent);
	}
}