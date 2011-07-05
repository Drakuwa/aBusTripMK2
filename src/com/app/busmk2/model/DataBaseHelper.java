package com.app.busmk2.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper class which overrides and extends some of the
 * SQLiteOpenHelper class functionalities. This model class contains SQL queries
 * written in class methods which are called where needed.
 * 
 * @author drakuwa
 * 
 */
public class DataBaseHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/com.app.busmk2/databases/";

	// The database name
	private static String DB_NAME = "avtobusi.sqlite";

	/**
	 * Set initial variables that will be used in the queries.
	 */
	public static final String KEY_ROWID = "_id";
	public static final String KEY_IME = "ime_stanica";
	public static final String KEY_LON = "lon";
	public static final String KEY_LAT = "lat";
	public static final String KEY_NASELBA = "naselba";

	private static final String DATABASE_TABLE = "stanica";

	private static final String DATABASE_TABLE_2 = "linija";
	public static final String KEY_ID = "_id";
	public static final String KEY_BROJ = "broj_linija";

	private static final String DATABASE_TABLE_3 = "mreza";
	public static final String KEY_ID_M = "_id";
	private static final String KEY_LINIJA = "linija";
	private static final String KEY_STANICA = "stanica";
	private static final String KEY_RBR = "reden_broj";
	private static final String KEY_NASOKA = "nasoka";

	private SQLiteDatabase db;

	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelper(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are going to be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Method that checks the existence of a local database in the given path
	 * and returns a boolean value.
	 * 
	 * @return
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Method that copies the database from the "assets" folder into the created
	 * empty database in the default system path.
	 * 
	 * @throws IOException
	 */
	private void copyDataBase() throws IOException {

		InputStream myInput = myContext.getAssets().open(DB_NAME);

		String outFileName = DB_PATH + DB_NAME;

		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	/**
	 * This method opens the database for reading.
	 * 
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {

		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	/**
	 * Method that closes the database connection.
	 */
	@Override
	public synchronized void close() {

		if (db != null)
			db.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * SQL query function that returns a cursor showing the bus stations table
	 * 
	 * @return
	 */
	public Cursor getStanici() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_IME,
				KEY_LON, KEY_LAT, KEY_NASELBA }, null, null, null, null, null);
	}

	/**
	 * SQL query function that returns a cursor showing the bus lines table
	 * 
	 * @return
	 */
	public Cursor getLinii() {
		return db.query(DATABASE_TABLE_2, new String[] { KEY_ID, KEY_BROJ },
				null, null, null, null, null);
	}

	/**
	 * SQL query function that returns a cursor showing the bus lines network
	 * table
	 * 
	 * @return
	 */
	public Cursor getMreza() {
		return db.query(DATABASE_TABLE_3, new String[] { KEY_ID_M, KEY_LINIJA,
				KEY_STANICA, KEY_RBR, KEY_NASOKA }, null, null, null, null,
				null);
	}

	/**
	 * SQL query function that returns a cursor showing the bus station with the
	 * given name passed as the parameter "ime".
	 * 
	 * @param ime
	 * @return
	 * @throws SQLException
	 */
	public Cursor getStanica(String ime) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE,
				new String[] { KEY_ROWID }, KEY_IME + " LIKE " + "'" + ime
						+ "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * SQL query function that returns a cursor showing the line numbers that
	 * contain the station with the given id (the passed id parameter).
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Cursor getUsefulLinii(String id) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE_3,
				new String[] { KEY_LINIJA }, KEY_STANICA + " LIKE " + "'" + id
						+ "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * SQL query function that returns a cursor showing all the bus stops of the
	 * given bus line.
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Cursor getUsefulStanici(String id) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE_3,
				new String[] { KEY_STANICA }, KEY_LINIJA + " LIKE " + "'" + id
						+ "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * SQL query function that returns a cursor showing the location (geo
	 * coordinates) of the given bus stop.
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Cursor getLocationStanici(String id) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] { KEY_LON,
				KEY_LAT }, KEY_ROWID + " LIKE " + "'" + id + "'", null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * SQL query function that returns a cursor showing the line number read
	 * from the "linija" table.
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Cursor getLinija(String id) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE_2,
				new String[] { KEY_BROJ }, KEY_ID + " LIKE " + "'" + id + "'",
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * SQL query function that returns a cursor showing the sequence number
	 * (which is the number of its appearance in the sorted list of the bus line
	 * stations).
	 * 
	 * @param id_stanica
	 * @param id_linija
	 * @return
	 * @throws SQLException
	 */
	public Cursor getRbr(String id_stanica, String id_linija)
			throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE_3,
				new String[] { KEY_RBR }, KEY_STANICA + " LIKE " + "'"
						+ id_stanica + "'" + " AND " + KEY_LINIJA + " LIKE "
						+ "'" + id_linija + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * SQL query function that returns a cursor showing the direction of the bus
	 * stop (2 possible values - A or B), is the station facing the end bus
	 * stop, or the first bus stop.
	 * 
	 * @param id_stanica
	 * @param id_linija
	 * @return
	 * @throws SQLException
	 */
	public Cursor getNasoka(String id_stanica, String id_linija)
			throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE_3,
				new String[] { KEY_NASOKA }, KEY_STANICA + " LIKE " + "'"
						+ id_stanica + "'" + " AND " + KEY_LINIJA + " LIKE "
						+ "'" + id_linija + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
