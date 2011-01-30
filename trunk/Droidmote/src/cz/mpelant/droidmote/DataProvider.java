package cz.mpelant.droidmote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * The Class DataProvider.
 */
public class DataProvider {

	/** The TAG for logcat */
	public static final String TAG = "droidmote";

	/** The Constant DATABASE_NAME. */
	private static final String DATABASE_NAME = "data";

	/** The Constant BUTTONS_DB_NAME. */
	public static final String BUTTONS_DB_NAME = "buttons";

	/** The Constant ACTIONS_DB_NAME. */
	public static final String ACTIONS_DB_NAME = "actions";

	/** The Constant PROFILES_DB_NAME. */
	public static final String PROFILES_DB_NAME = "profiles";

	/** The Constant PROFILES_ROWID. */
	public static final String PROFILES_ROWID = "_id";

	/** The Constant PROFILES_NAME. */
	public static final String PROFILES_NAME = "name";

	/** The Constant BUTTONS_ROWID. */
	public static final String BUTTONS_ROWID = "_id";

	/** The Constant BUTTONS_PROFILE_ID. */
	public static final String BUTTONS_PROFILE_ID = "profile_id";

	/** The Constant BUTTONS_POSITION. */
	public static final String BUTTONS_POSITION = "position";

	/** The Constant BUTTONS_NAME. */
	public static final String BUTTONS_NAME = "name";

	/** The Constant ACTIONS_ROWID. */
	public static final String ACTIONS_ROWID = "_id";

	/** The Constant ACTIONS_BUTTON_ID. */
	public static final String ACTIONS_BUTTON_ID = "button_id";

	/** The Constant ACTIONS_ACTION. */
	public static final String ACTIONS_ACTION = "action";

	/** The Constant DATABASE_VERSION. */
	private static final int DATABASE_VERSION = 6;

	/** The Database Helper instance */
	private DatabaseHelper mDbHelper;

	/** The SQLiteDatabase */
	private SQLiteDatabase mDb;

	/** The context for accessing the database */
	private final Context mCtx;

	/**
	 * The Class DatabaseHelper.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		/**
		 * Instantiates a new database helper.
		 * 
		 * @param context the context
		 */
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "oncreate");
			String dbCreate0 = "create table " + PROFILES_DB_NAME + " ( " + PROFILES_ROWID + " integer primary key autoincrement, " + PROFILES_NAME + " text not null);";
			String dbCreate1 = "create table " + BUTTONS_DB_NAME + " ( " + BUTTONS_ROWID + " integer primary key autoincrement, " + BUTTONS_POSITION + " integer not null, " + BUTTONS_PROFILE_ID + " integer not null, " + BUTTONS_NAME + " text not null);";
			String dbCreate2 = "create table " + ACTIONS_DB_NAME + " ( " + ACTIONS_ROWID + " integer primary key autoincrement, " + ACTIONS_BUTTON_ID + " integer not null, " + ACTIONS_ACTION + " text not null);";
			db.execSQL(dbCreate0);
			db.execSQL(dbCreate1);
			db.execSQL(dbCreate2);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + PROFILES_DB_NAME + ";");
			db.execSQL("DROP TABLE IF EXISTS " + BUTTONS_DB_NAME + ";");
			db.execSQL("DROP TABLE IF EXISTS " + ACTIONS_DB_NAME + ";");
			onCreate(db);
		}
	}

	/**
	 * Instantiates a new data provider.
	 * 
	 * @param ctx the context
	 */
	public DataProvider(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open.
	 * 
	 * @return the data provider
	 * @throws SQLException the SQL exception
	 */
	public DataProvider open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close the connection.
	 */
	public void close() {
		mDbHelper.close();
	}

	// ------PROFILES-----------------------------------------
	/**
	 * Creates the profile.
	 * 
	 * @param name the name of the profile
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long createProfile(String name) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(PROFILES_NAME, name);
		Log.d(TAG, "creating profile" + name);
		return mDb.insert(PROFILES_DB_NAME, null, initialValues);
	}

	/**
	 * Delete profile.
	 * 
	 * @param rowId the row Id of the profile
	 * @return true, if successful
	 */
	public boolean deleteProfile(long rowId) {
		Log.d(TAG, "deleting profile" + rowId);
		for (int i = 0; i < 12; i++) {
			Cursor c = fetchButton(rowId, i);
			if (c != null && c.getCount() > 0) {
				long buttonId = (long) c.getInt(c.getColumnIndex(BUTTONS_ROWID));
				deleteButton(buttonId);
			}
		}
		return mDb.delete(PROFILES_DB_NAME, PROFILES_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Update profile.
	 * 
	 * @param rowId the row Id of the profile
	 * @param name the new updated name
	 * @return true, if successful
	 */
	public boolean updateProfile(long rowId, String name) {
		ContentValues args = new ContentValues();
		Log.d(TAG, "updating profile" + rowId + " (" + name + ")");
		args.put(PROFILES_NAME, name);

		return mDb.update(PROFILES_DB_NAME, args, PROFILES_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Fetch all profiles.
	 * 
	 * @return the cursor
	 */
	public Cursor fetchAllProfiles() {
		return mDb.query(PROFILES_DB_NAME, new String[] { PROFILES_ROWID, PROFILES_NAME }, null, null, null, null, PROFILES_NAME);
	}

	/**
	 * Fetch profile.
	 * 
	 * @param rowId the row Id of the profile
	 * @return the cursor
	 * @throws SQLException the sQL exception
	 */
	public Cursor fetchProfile(long rowId) throws SQLException {

		Cursor mCursor = mDb.query(true, PROFILES_DB_NAME, new String[] { PROFILES_ROWID, PROFILES_NAME }, PROFILES_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	// ------BUTTONS-----------------------------------------
	/**
	 * Creates the button.
	 * 
	 * @param name the name of the button
	 * @param pos the position
	 * @param profileId the profile id
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long createButton(String name, int pos, long profileId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(BUTTONS_NAME, name);
		initialValues.put(BUTTONS_POSITION, pos);
		initialValues.put(BUTTONS_PROFILE_ID, profileId);
		Log.d(TAG, "creating button" + name);
		return mDb.insert(BUTTONS_DB_NAME, null, initialValues);
	}

	/**
	 * Delete button.
	 * 
	 * @param rowId the row Id of the button
	 * @return true, if successful
	 */
	public boolean deleteButton(long rowId) {
		Log.d(TAG, "deleting button" + rowId);
		return mDb.delete(BUTTONS_DB_NAME, BUTTONS_ROWID + "=" + rowId, null) + mDb.delete(ACTIONS_DB_NAME, ACTIONS_BUTTON_ID + "=" + rowId, null) > 0;
	}

	/**
	 * Update button.
	 * 
	 * @param rowId the row Id of the button
	 * @param name the new updated name
	 * @return true, if successful
	 */
	public boolean updateButton(long rowId, String name) {
		ContentValues args = new ContentValues();
		Log.d(TAG, "updating button" + rowId + " (" + name + ")");
		args.put(BUTTONS_NAME, name);

		return mDb.update(BUTTONS_DB_NAME, args, BUTTONS_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Fetch all buttons.
	 * 
	 * @return the cursor
	 */
	public Cursor fetchAllButtons() {
		return mDb.query(BUTTONS_DB_NAME, new String[] { BUTTONS_ROWID, BUTTONS_NAME }, null, null, null, null, BUTTONS_ROWID);
	}

	/**
	 * Fetch button.
	 * 
	 * @param rowId the row Id of the button
	 * @return the cursor
	 * @throws SQLException the sQL exception
	 */
	public Cursor fetchButton(long rowId) throws SQLException {

		Cursor mCursor = mDb.query(true, BUTTONS_DB_NAME, new String[] { BUTTONS_ROWID, BUTTONS_NAME, BUTTONS_POSITION, BUTTONS_PROFILE_ID }, BUTTONS_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * Fetch button.
	 * 
	 * @param profileID the profile id
	 * @param pos the position
	 * @return the cursor
	 * @throws SQLException the sQL exception
	 */
	public Cursor fetchButton(long profileID, int pos) throws SQLException {

		Cursor mCursor = mDb.query(true, BUTTONS_DB_NAME, new String[] { BUTTONS_ROWID, BUTTONS_NAME, BUTTONS_POSITION, BUTTONS_PROFILE_ID }, BUTTONS_PROFILE_ID + "=" + profileID + " AND " + BUTTONS_POSITION + "=" + pos, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ------ACTIONS-----------------------------------------

	/**
	 * Creates the action.
	 * 
	 * @param name the name
	 * @param buttonID the button id
	 * @return the long
	 */
	public long createAction(String name, long buttonID) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ACTIONS_ACTION, name);
		initialValues.put(ACTIONS_BUTTON_ID, buttonID);
		Log.d(TAG, "creating action" + name + " - for button " + buttonID);
		return mDb.insert(ACTIONS_DB_NAME, null, initialValues);
	}

	/**
	 * Delete actions.
	 * 
	 * @param buttonId the button id
	 * @return true, if successful
	 */
	public boolean deleteActions(long buttonId) {
		Log.d(TAG, "deleting actions from button " + buttonId);
		return mDb.delete(ACTIONS_DB_NAME, ACTIONS_BUTTON_ID + "=" + buttonId, null) > 0;
	}

	/**
	 * Fetch all actions.
	 * 
	 * @param buttonId the button id
	 * @return the cursor
	 */
	public Cursor fetchAllActions(long buttonId) {
		return mDb.query(true, ACTIONS_DB_NAME, new String[] { ACTIONS_ACTION }, ACTIONS_BUTTON_ID + "=" + buttonId, null, null, null, ACTIONS_ROWID, null);
	}

	/**
	 * Fetch action.
	 * 
	 * @param rowId the row Id of the action
	 * @return the cursor
	 * @throws SQLException the sQL exception
	 */
	public Cursor fetchAction(long rowId) throws SQLException {

		Cursor mCursor = mDb.query(true, ACTIONS_DB_NAME, new String[] { ACTIONS_ACTION }, ACTIONS_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// -------------------------------------------

}
