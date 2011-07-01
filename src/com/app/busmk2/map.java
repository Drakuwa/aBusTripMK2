package com.app.busmk2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class map extends MapActivity {

	private LocationManager lm;
	private LocationListener ll;
	private MapController mc;
	private MyItemizedOverlay person;
	private MyItemizedOverlay stations;
	private List<Overlay> mapOverlays;
	private GeoPoint current;

	private static final int MENU_MY_LOCATION = Menu.FIRST;
	private static final int MENU_CLEAR_MAP = MENU_MY_LOCATION + 1;
	private static final int MENU_PLACE = MENU_CLEAR_MAP + 1;
	private static final int MENU_ABOUT = MENU_PLACE + 1;

	private static final int DIALOG_ABOUT_ID = 1;
	private MapView mapView;

	private boolean offlineVar = false;

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			createGpsDisabledAlert();
		}

		ll = new MyLocationListener();

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, ll);

		{// TODO some initial map overlays, ovakoj treba da se dodadev i crtanje
			// na putishta (funkcija u MyItemizedOverlay...)
			// u MyItemizedOverlay...

		}

		mapOverlays = mapView.getOverlays();

		Drawable person_drawable = this.getResources().getDrawable(
				R.drawable.person);
		person = new MyItemizedOverlay(person_drawable, this);

		Drawable station_drawable = this.getResources().getDrawable(
				R.drawable.busmarker);
		stations = new MyItemizedOverlay(station_drawable, this);

		mc = mapView.getController();

		DataBaseHelper myDb = new DataBaseHelper(null);
		myDb = new DataBaseHelper(this);

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

		{
			myDb.getReadableDatabase();
		}
		/* Itemized Overlay */
		{
			final ArrayList<OverlayItem> busstops = new ArrayList<OverlayItem>();

			// accessing the database
			{
				Cursor c = myDb.getStanici();
				if (c.moveToFirst()) {
					do {
						// String id = c.getString(0);
						String ime = c.getString(1);

						double flon = c.getDouble(3);
						double flat = c.getDouble(2);

						int lat = (int) (flat * 1000000);
						int lon = (int) (flon * 1000000);

						GeoPoint stanica = new GeoPoint(lat, lon);

						busstops.add(new OverlayItem(stanica, ime, " "));

						// Log.d("xxx", "ID: " + id + "  ime=" + ime +
						// " lat/lon=" + lon + " / " + lat);

					} while (c.moveToNext());
				}
				myDb.close();
			}

			stations.addOverlay(busstops);
			mapOverlays.add(stations);
		}

		/*
		 * //TODO ova za first run kontrolerot da ne pozicionira nekade vo
		 * skopje :D
		 * 
		 * mc.setZoom(14); mc.setCenter(anchor_Europe);
		 * 
		 * Projection pr; pr = mapView.getProjection();
		 */

	}

	@Override
	protected void onPause() {
		lm.removeUpdates(ll);
		super.onPause();
		};
	
	private void createGpsDisabledAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
				.setMessage(
						"Вашиот GPS е оневозможен! Потребно е да биде активиран, за нормално функционирање на апликацијата (пронаоѓање на Вашата локација). Дали би сакале да го овозможите?")
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

	private void showGpsOptions() {
		Intent gpsOptionsIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(gpsOptionsIntent);
	}

	//TODO moze da se izbrishe Clear Map, poshto valjda nema da treba...
	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(0, MENU_MY_LOCATION, Menu.NONE, "My Location").setIcon(
				android.R.drawable.ic_menu_mylocation);
		pMenu.add(0, MENU_CLEAR_MAP, Menu.NONE, "Clear Map").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		pMenu.add(0, MENU_PLACE, Menu.NONE, "Place Marker").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		pMenu.add(0, MENU_ABOUT, Menu.NONE, "About").setIcon(
				android.R.drawable.ic_menu_info_details);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_MY_LOCATION:
			if (current != null)
				mc.animateTo(current);
			else
				Toast
						.makeText(
								getApplicationContext(),
								"You're location is unknown, place a marker or wait for GPS...",
								Toast.LENGTH_SHORT).show();
			return true;

		case MENU_CLEAR_MAP:

			// for (Iterator<OpenStreetMapViewOverlay> iter =
			// mOsmv.getOverlays()
			// .iterator(); iter.hasNext();) {
			// Object o = iter.next();
			// if (MyOverLay.class.getName().equals(o.getClass().getName())) {
			// iter.remove();
			// }
			// }
			return true;

		case MENU_PLACE:
			offlineVar = true;
			return true;

		case MENU_ABOUT:
			showDialog(DIALOG_ABOUT_ID);
			return true;

		default: // Map mode submenu items
			// this.mOsmv.setRenderer(OpenStreetMapRendererInfo.values()[item
			// .getItemId() - 1000]);
		}
		return false;
	}

	// TODO smeni poraka!
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;

		switch (id) {
		case DIALOG_ABOUT_ID:
			return new AlertDialog.Builder(map.this).setIcon(R.drawable.icon)
					.setTitle(R.string.app_name).setMessage(
							"Ова е тест порака...") 
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();

		default:
			dialog = null;
			break;
		}
		return dialog;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// demek da ima promenliva ako kliknesh na oflajn mod tga da ga povika
		// handlePress
		if (offlineVar) {
			handlePress(event);
			offlineVar = false;
		}
		return super.dispatchTouchEvent(event);
	}

	private void handlePress(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// A new touch has been detected
			GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(),
					(int) event.getY());
			current = p;
			OverlayItem myloc = new OverlayItem(p, "My Location", "");

			person.clear();
			person.addOverlay(myloc);
			mapOverlays.add(person);
		}
	}

	public class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {

			if (loc != null) {
				Toast.makeText(
						getBaseContext(),
						"Location changed : Lat: " + loc.getLatitude()
								+ " Lng: " + loc.getLongitude(),
						Toast.LENGTH_SHORT).show();

				// Location current_location =
				// lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				// if(isBetterLocation(loc,current_location) ){
				// ako e podobra novata lokacija od starata
				// };

				GeoPoint p = new GeoPoint((int) (loc.getLatitude() * 1000000),
						(int) (loc.getLongitude() * 1000000));

				current = p;
				OverlayItem overlayitem_me = new OverlayItem(p,
						"My Current Location...", "");

				person.clear();
				person.addOverlay(overlayitem_me);
				mapView.getOverlays().add(person);

				mc.animateTo(p);
				mapView.invalidate();
				lm.removeUpdates(ll);

			}
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

}
