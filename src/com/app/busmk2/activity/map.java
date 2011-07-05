package com.app.busmk2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.app.busmk2.R;
import com.app.busmk2.model.DataBaseHelper;
import com.app.busmk2.model.Model;
import com.app.busmk2.model.MyItemizedOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * Map activity class which handles the main functionality of this application.
 * This class shows the bus stops and enables the user to choose between them,
 * so he can get the closest to him bus stop, and the useful bus line numbers
 * that will take him to his wanted destination.
 * 
 * @author drakuwa
 * 
 */
public class map extends MapActivity {

	/**
	 * Set initial variables that will be used
	 */
	private LocationManager lm;
	private LocationListener ll;
	private MapController mc;
	private MyItemizedOverlay person;
	private MyItemizedOverlay stations;
	private MyItemizedOverlay chosen_station;
	private MyItemizedOverlay dest_station;
	private List<Overlay> mapOverlays;
	private GeoPoint current;

	/**
	 * Set initial menu variables
	 */
	private static final int MENU_MY_LOCATION = Menu.FIRST;
	private static final int MENU_REFRESH = MENU_MY_LOCATION + 1;
	private static final int MENU_CLEAR_MAP = MENU_REFRESH + 1;
	private static final int MENU_PLACE = MENU_CLEAR_MAP + 1;
	private static final int MENU_ABOUT = MENU_PLACE + 1;

	private MapView mapView;

	private boolean offlineVar = false;

	// Initialize the model with the activities context.
	private Model model = new Model(this);

	// default needed override of the isRouteDiplayed() method
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * an override of the onCreate method that initializes the map and it
	 * overlays.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		// connect the map with its layout and set the default zoom controls.
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		// initialize the location manager variable and call the model method if
		// the GPS is disabled
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			model.createGpsDisabledAlert();
		}

		// create a new location listener and request a location update.
		ll = new MyLocationListener();

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, ll);

		// initialize the map overlays.
		mapOverlays = mapView.getOverlays();

		// initialize the map controller.
		mc = mapView.getController();

		/**
		 * Initialize the map marker drawables and the MyItemizedOverlay lists.
		 */
		Drawable final_drawable = this.getResources().getDrawable(
				R.drawable.greenmarker);
		chosen_station = new MyItemizedOverlay(final_drawable, this);
		Drawable dest_drawable = this.getResources().getDrawable(
				R.drawable.redmarker);
		dest_station = new MyItemizedOverlay(dest_drawable, this);

		Drawable person_drawable = this.getResources().getDrawable(
				R.drawable.person);
		person = new MyItemizedOverlay(person_drawable, this);

		Drawable station_drawable = this.getResources().getDrawable(
				R.drawable.busmarker);
		stations = new MyItemizedOverlay(station_drawable, this) {

			/**
			 * When initializing the "stations" list of overlay items, override
			 * the onTap method just for station overlay items, so that we can
			 * call a method only on bus stop tap.
			 * 
			 * When clicked on a bus stop, create an AlertDialog asking the user
			 * if he wants to get the closest bus stop/line number that will
			 * take him to this location, and call an AsyncTask that will
			 * calculate the result while showing a progress dialog, if the
			 * current location is known (placed marker or GPS...).
			 */
			@Override
			public boolean onTap(int index) {
				final OverlayItem item = stations.get(index);
				if (item.getTitle() != "My Location") {
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							map.this);
					dialog.setTitle(R.string.app_name).setIcon(R.drawable.icon);
					dialog.setMessage("Кликнавте на: " + item.getTitle()
							+ ", Дали сакате да стигнете до таму?");
					dialog.setPositiveButton("Да!",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									if (current != null) {
										int lon = item.getPoint()
												.getLongitudeE6();
										int lat = item.getPoint()
												.getLatitudeE6();
										String lons = Integer.toString(lon);
										String lats = Integer.toString(lat);

										new calc_stanica().execute(item
												.getTitle(), lons, lats);
									} else {
										model
												.final_dialog("Вашата моментална локација не е позната, Ве молиме почекајте, или поставете маркер");
									}
								}
							});
					dialog.setNegativeButton("Откажи",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
					AlertDialog alert = dialog.create();
					alert.show();
				}
				return true;
			}
		};

		/**
		 * Open the database and read the location of all the bus stops, and
		 * place them on the map in the "stations" overlay item list.
		 */
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

		/**
		 * Call to the model method which checks if the map is ran for the first
		 * time, and if it is, it positions the map in the center of Skopje.
		 */
		model.first_run_map(mc);

	}

	/**
	 * On pause, stop the GPS location search.
	 */
	@Override
	protected void onPause() {
		lm.removeUpdates(ll);
		super.onPause();
	};

	/**
	 * Initialize the options menu with the given labels and icons.
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(0, MENU_MY_LOCATION, Menu.NONE, "My Location").setIcon(
				android.R.drawable.ic_menu_mylocation);
		pMenu.add(0, MENU_REFRESH, Menu.NONE, "Refresh Location").setIcon(
				android.R.drawable.ic_menu_revert);
		pMenu.add(0, MENU_CLEAR_MAP, Menu.NONE, "Clear Map").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		pMenu.add(0, MENU_PLACE, Menu.NONE, "Place Marker").setIcon(
				android.R.drawable.ic_menu_myplaces);
		pMenu.add(0, MENU_ABOUT, Menu.NONE, "About").setIcon(
				android.R.drawable.ic_menu_info_details);

		return true;
	}

	/**
	 * An override of the menu item select method that adds some useful
	 * functionalities to this part of the application.
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_MY_LOCATION:
			// if the current location is known, centar the map there, else show
			// a toast message.
			if (current != null)
				mc.animateTo(current);
			else
				Toast
						.makeText(
								getApplicationContext(),
								"Вашата локација е непозната, поставете маркер или почекајте GPS...",
								Toast.LENGTH_SHORT).show();
			return true;

		case MENU_REFRESH:
			// if the GPS is enabled, refresh the location, else give gps
			// disabled alert.
			if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				model.createGpsDisabledAlert();
			} else
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
						10, ll);
			return true;

		case MENU_CLEAR_MAP:
			// clear the destination and closest station markings (green and red
			// transparent circles).
			dest_station.clear();
			chosen_station.clear();
			mapView.invalidate();
			return true;

		case MENU_PLACE:
			// boolean variable that enables the user to place a marker of its
			// location on the map.
			offlineVar = true;
			return true;

		case MENU_ABOUT:
			// calls a model method showing a dialog window with the given text.
			model
					.final_dialog("Ова апликација користи мапи обезбедени од Google Inc.");
			// showDialog(DIALOG_ABOUT_ID);
			return true;

		default:
		}
		return false;
	}

	/**
	 * An override of the touch event listener. If the offlineVar is true, call
	 * a method to handle the touch event, and set the variable to false, so
	 * that the next touch event wont be considered, unless the variable is
	 * again set true.
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (offlineVar) {
			handlePress(event);
			offlineVar = false;
		}
		return super.dispatchTouchEvent(event);
	}

	/**
	 * If this method is called, the user wanted to set its location rather then
	 * waiting for GPS to find it. The method gets the map pixel projection of
	 * the touch event and places a marker there, removing previously placed
	 * location markers.
	 * 
	 * @param event
	 */
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

	/**
	 * A small override and change of the LocationListener class which extends
	 * the method called on changed location.
	 * 
	 * @author drakuwa
	 * 
	 */
	public class MyLocationListener implements LocationListener {

		/**
		 * If the GPS finds the users location it shows a toast message of the
		 * location, and it places a marker on the found location, deleting any
		 * previously placed markers on the map, and removing the location
		 * listener for battery save.
		 */
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
				OverlayItem overlayitem_me = new OverlayItem(p, "My Location",
						"");

				person.clear();
				person.addOverlay(overlayitem_me);
				mapView.getOverlays().add(person);

				mc.animateTo(p);
				mapView.invalidate();
				lm.removeUpdates(ll);

			}
		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

	}

	/**
	 * Method that places markers on the final (clicked) bus stop, and the
	 * closest bus stop to the user.
	 * 
	 * @param destGeoPoint
	 * @param final_geo
	 */
	private void draw(GeoPoint destGeoPoint, GeoPoint final_geo) {
		dest_station.addOverlay(new OverlayItem(destGeoPoint, "Dest...", " "));
		chosen_station.addOverlay(new OverlayItem(final_geo, "Fin...", " "));
		mapOverlays.add(dest_station);
		mapOverlays.add(chosen_station);
	}

	/**
	 * An asynchronous task class that calculates the closest useful bus stop to
	 * the user, and the useful bus line numbers, showing a progress dialog
	 * while doing the calculations.
	 * 
	 * @author drakuwa
	 * 
	 */
	public class calc_stanica extends
			AsyncTask<String, Void, ArrayList<String>> {
		ProgressDialog dialog;

		/**
		 * On PreExecute, initialize and show the progress dialog.
		 */
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(map.this);
			dialog.setTitle("Пресметуваме...");
			dialog.setMessage("Ве молиме почекајте...");
			dialog.setIndeterminate(true);
			dialog.show();
		}

		/**
		 * what to do in background while showing the progress dialog.
		 */
		protected ArrayList<String> doInBackground(String... vlezni) {

			/**
			 * Get the input parameters.
			 */
			String itemdesc = vlezni[0];
			String final_lon = vlezni[1];
			String final_lat = vlezni[2];

			// Initialize and open the database.
			DataBaseHelper db = new DataBaseHelper(null);
			db = new DataBaseHelper(getApplicationContext());

			try {
				db.createDataBase();
			} catch (IOException ioe) {
				throw new Error("Unable to create database");
			}
			try {
				db.openDataBase();
			} catch (SQLException sqle) {
				throw sqle;
			}

			db.getReadableDatabase();

			// Create some later needed variables.
			double min = 2000;
			GeoPoint g = null;
			ArrayList<String> korisni_linii = new ArrayList<String>();
			double glon = 0;
			double glat = 0;
			boolean b = false;
			boolean d = false;

			// Get information for the clicked bus stop.
			Cursor c = db.getStanica(itemdesc);
			if (c.moveToFirst()) {
				String _id = c.getString(0); // id of the clicked station.

				// Log.d("xxx", "ID na Kliknata Stanica: " + _id);

				// Get the lines that contain the given bus stop.
				Cursor c2 = db.getUsefulLinii(_id);
				if (c2.moveToFirst()) {
					do {
						String id_korisna_linija = c2.getString(0);
						// Log.d("xxx", "ID na Korisni Linie: " +
						// id_korisna_linija);
						int newmin = 3000;

						// Get the sequence number of the bus stop.
						Cursor br = db.getRbr(_id, id_korisna_linija);
						int rbr_finaldest = br.getInt(0);

						// Get all the bus stops of the given useful line, so
						// you can search for the closest one to your location.
						Cursor c3 = db.getUsefulStanici(id_korisna_linija);
						if (c3.moveToFirst()) {
							do {
								String id_korisna_stanica = c3.getString(0);

								Cursor br_2 = db.getRbr(id_korisna_stanica,
										id_korisna_linija);
								int rbr_currentdest = br_2.getInt(0);

								Cursor nas_2 = db.getNasoka(id_korisna_stanica,
										id_korisna_linija);
								String nasoka_currentdest = nas_2.getString(0);

								// Log.d("xxx", "ID na Korisni Stanice: " +
								// id_korisna_stanica);

								// If current location is unknown (should not
								// happen, but just in case) break.
								if (current == null) {
									break;
								}

								// Get the geo coordinates of the current
								// checked bus stop.
								Cursor c4 = db
										.getLocationStanici(id_korisna_stanica);
								// c4 has only one result.
								if (c4.moveToFirst()) {
									glon = c4.getDouble(0);
									glat = c4.getDouble(1);

									int lat = (int) (glat * 1000000);
									int lon = (int) (glon * 1000000);

									GeoPoint stanica = new GeoPoint(lon, lat);

									// turn the geopoints into location
									// variables so we can calculate the
									// distance between them.
									Location currentLoc = new Location(
											"current");

									currentLoc.setLatitude(current
											.getLatitudeE6() / 1E6);
									currentLoc.setLongitude(current
											.getLongitudeE6() / 1E6);

									Location stanicaLoc = new Location(
											"stanica");

									stanicaLoc.setLatitude(stanica
											.getLatitudeE6() / 1E6);
									stanicaLoc.setLongitude(stanica
											.getLongitudeE6() / 1E6);

									// Calculate the distance.
									double rastojanie = currentLoc
											.distanceTo(stanicaLoc);

									// Check if the current stations that is
									// being analyzed is on the right direction.
									if (rbr_finaldest > rbr_currentdest
											&& nasoka_currentdest
													.equalsIgnoreCase("A")) {
										if (min > rastojanie) {
											min = rastojanie;
											g = new GeoPoint(lat, lon);
											d = true;
											// Log.d("xxx"," KOORDINATI NA G: "
											// + glon +" / "+ glat);
										}
									}

									else if (rbr_finaldest < rbr_currentdest
											&& nasoka_currentdest
													.equalsIgnoreCase("B")) {
										if (min > rastojanie) {
											min = rastojanie;
											g = new GeoPoint(lat, lon);
											d = true;
											Log.d("xxx", " KOORDINATI NA G: "
													+ glon + " / " + glat);
										}
									}

									// Check if there is another line passing
									// through the given current closest bus
									// stop to the user.
									if (min == rastojanie) {// najdena e linija
										// shto pominuva na
										// ista stanica
										b = true;
										Log
												.d(
														"xxx",
														" VNATRE VO B rastojanie: "
																+ rastojanie
																+ "  Konachan minimum = "
																+ min);
									}
									Log.d("xxx",
											" RASTOJANIE do sekoja stanica: "
													+ rastojanie
													+ "  Nov minimum:  "
													+ newmin
													+ "  Konachan minimum = "
													+ min);
								}
							} while (c3.moveToNext());
						}

						// Check if there was a new minimum distance found,
						// clear the list with previously found minimal distance
						// bus stops, and add this bus stop to the empty list.
						if (d) {
							// Log.d("xxx",
							// "VLEZENO E VO D!!! - za najden pomal minimum...");
							korisni_linii.clear();
							Cursor cX = db.getLinija(id_korisna_linija);
							String ime_linija = cX.getString(0);
							korisni_linii.add(ime_linija);
							d = false;
							b = false;
						}
						// If there is a same minimum found, it means that
						// another bus line passes through the destionation bus
						// stop, and the closest one until now, so add this
						// station to the list of useful bus lines.
						else if (b) {
							// Log.d("xxx",
							// "VLEZENO E VO B!!! - za najden ist minimum...");
							Cursor cX = db.getLinija(id_korisna_linija);
							String ime_linija = cX.getString(0);
							korisni_linii.add(ime_linija);
							b = false;
						}
					} while (c2.moveToNext());
				}
			}
			db.close();

			// Initialize an array list of strings, to pass the final results to
			// the onPostExecute method.
			ArrayList<String> izlezni = new ArrayList<String>();
			String korlin = korisni_linii.toString();
			String minim = Double.toString(min);

			int lon = 0;
			int lat = 0;
			if (min < 2000) {
				lon = g.getLongitudeE6();
				lat = g.getLatitudeE6();
			}
			String closest_lon = Integer.toString(lon);
			String closest_lat = Integer.toString(lat);

			// add the calculated minimum distance, which will be compared
			// later.
			izlezni.add(minim);

			// add the longitude and latitude of the closest bus stop.
			izlezni.add(closest_lon);
			izlezni.add(closest_lat);

			// add the longitude and latitude of the final (clicked destination)
			// bus stop.
			izlezni.add(final_lon);
			izlezni.add(final_lat);

			// add the list of useful bus lines (ones that can take the user
			// from closest bus stop to destination bus stop).
			izlezni.add(korlin);

			return izlezni;
		}

		/**
		 * What to do after the calculations are finished.
		 */
		@Override
		public void onPostExecute(ArrayList<String> izlezni) {
			// Remove the progress dialog.
			dialog.dismiss();

			// Get the needed GeoPoints.
			String minim = izlezni.get(0);
			double min = Double.parseDouble(minim);

			String closest_lon = izlezni.get(1);
			String closest_lat = izlezni.get(2);

			int c_lon = Integer.parseInt(closest_lon);
			int c_lat = Integer.parseInt(closest_lat);

			GeoPoint g = new GeoPoint(c_lon, c_lat);

			String final_lon = izlezni.get(3);
			String final_lat = izlezni.get(4);

			int f_lon = Integer.parseInt(final_lon);
			int f_lat = Integer.parseInt(final_lat);

			GeoPoint final_geo = new GeoPoint(f_lat, f_lon);

			String korisni_linii = izlezni.get(5);

			/**
			 * If the calculated closest bus stop is closer than 2000m to the
			 * user, show the results, else show a dialog explaining that no
			 * good results were found
			 */
			if (min < 2000) {

				GeoPoint destGeoPoint = g;

				draw(destGeoPoint, final_geo);
				mc.setCenter(current);

				model
						.final_dialog("Појдете до означената постојка со црвено и почекајте автобус со број: "
								+ korisni_linii);
			} else
				model
						.final_dialog("За жал нема постојка во Ваша близина што поминува од овде, побарајте нова станица...");
		}
	}

}
