package cz.mpelant.droidmote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataProvider {
	public static final String TAG = "droidmote";

	private static final String DATABASE_NAME = "data";

	public static final String BUTTONS_DB_NAME = "buttons";
	public static final String ACTIONS_DB_NAME = "actions";
	public static final String PROFILES_DB_NAME = "profiles";

	public static final String PROFILES_ROWID = "_id";
	public static final String PROFILES_NAME = "name";

	public static final String BUTTONS_ROWID = "_id";
	public static final String BUTTONS_PROFILE_ID = "profile_id";
	public static final String BUTTONS_POSITION = "position";
	public static final String BUTTONS_NAME = "name";

	public static final String ACTIONS_ROWID = "_id";
	public static final String ACTIONS_BUTTON_ID = "button_id";
	public static final String ACTIONS_ACTION = "action";

	private static final int DATABASE_VERSION = 6;

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

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

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + PROFILES_DB_NAME + ";");
			db.execSQL("DROP TABLE IF EXISTS " + BUTTONS_DB_NAME + ";");
			db.execSQL("DROP TABLE IF EXISTS " + ACTIONS_DB_NAME + ";");
			onCreate(db);
		}
	}

	public DataProvider(Context ctx) {
		this.mCtx = ctx;
	}

	public DataProvider open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	// ------PROFILES-----------------------------------------
	public long createProfile(String name) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(PROFILES_NAME, name);
		Log.d(TAG, "creating profile" + name);
		return mDb.insert(PROFILES_DB_NAME, null, initialValues);
	}

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

	public boolean updateProfile(long rowId, String name) {
		ContentValues args = new ContentValues();
		Log.d(TAG, "updating profile" + rowId + " (" + name + ")");
		args.put(PROFILES_NAME, name);

		return mDb.update(PROFILES_DB_NAME, args, PROFILES_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllProfiles() {
		return mDb.query(PROFILES_DB_NAME, new String[] { PROFILES_ROWID, PROFILES_NAME }, null, null, null, null, PROFILES_NAME);
	}

	public Cursor fetchProfile(long rowId) throws SQLException {

		Cursor mCursor = mDb.query(true, PROFILES_DB_NAME, new String[] { PROFILES_ROWID, PROFILES_NAME }, PROFILES_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	// ------BUTTONS-----------------------------------------
	public long createButton(String name, int pos, long profileId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(BUTTONS_NAME, name);
		initialValues.put(BUTTONS_POSITION, pos);
		initialValues.put(BUTTONS_PROFILE_ID, profileId);
		Log.d(TAG, "creating button" + name);
		return mDb.insert(BUTTONS_DB_NAME, null, initialValues);
	}

	public boolean deleteButton(long rowId) {
		Log.d(TAG, "deleting button" + rowId);
		return mDb.delete(BUTTONS_DB_NAME, BUTTONS_ROWID + "=" + rowId, null) + mDb.delete(ACTIONS_DB_NAME, ACTIONS_BUTTON_ID + "=" + rowId, null) > 0;
	}

	public boolean updateButton(long rowId, String name) {
		ContentValues args = new ContentValues();
		Log.d(TAG, "updating button" + rowId + " (" + name + ")");
		args.put(BUTTONS_NAME, name);

		return mDb.update(BUTTONS_DB_NAME, args, BUTTONS_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllButtons() {
		return mDb.query(BUTTONS_DB_NAME, new String[] { BUTTONS_ROWID, BUTTONS_NAME }, null, null, null, null, BUTTONS_ROWID);
	}

	public Cursor fetchButton(long rowId) throws SQLException {

		Cursor mCursor = mDb.query(true, BUTTONS_DB_NAME, new String[] { BUTTONS_ROWID, BUTTONS_NAME, BUTTONS_POSITION, BUTTONS_PROFILE_ID }, BUTTONS_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor fetchButton(long profileID, int pos) throws SQLException {

		Cursor mCursor = mDb.query(true, BUTTONS_DB_NAME, new String[] { BUTTONS_ROWID, BUTTONS_NAME, BUTTONS_POSITION, BUTTONS_PROFILE_ID }, BUTTONS_PROFILE_ID + "=" + profileID + " AND " + BUTTONS_POSITION + "=" + pos, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// ------ACTIONS-----------------------------------------

	public long createAction(String name, long buttonID) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ACTIONS_ACTION, name);
		initialValues.put(ACTIONS_BUTTON_ID, buttonID);
		Log.d(TAG, "creating action" + name + " - for button " + buttonID);
		return mDb.insert(ACTIONS_DB_NAME, null, initialValues);
	}

	public boolean deleteActions(long buttonId) {
		Log.d(TAG, "deleting actions from button " + buttonId);
		return mDb.delete(ACTIONS_DB_NAME, ACTIONS_BUTTON_ID + "=" + buttonId, null) > 0;
	}

	public Cursor fetchAllActions(long buttonId) {
		return mDb.query(true, ACTIONS_DB_NAME, new String[] { ACTIONS_ACTION }, ACTIONS_BUTTON_ID + "=" + buttonId, null, null, null, ACTIONS_ROWID, null);
	}

	public Cursor fetchAction(long rowId) throws SQLException {

		Cursor mCursor = mDb.query(true, ACTIONS_DB_NAME, new String[] { ACTIONS_ACTION }, ACTIONS_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;

	}

	// -------------------------------------------

}
