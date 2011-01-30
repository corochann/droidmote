package cz.mpelant.droidmote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Displays the profile.
 */
public class ProfileView extends SuperActivity {
	
	/** The buttons. */
	private Button[] buttons = new Button[12];
	
	/** The data provider. */
	private DataProvider data;
	
	/** The profile id. */
	private long profileId = -1;
	
	/** The delete dialog. */
	private AlertDialog deleteDialog;

	/* (non-Javadoc)
	 * @see cz.mpelant.droidmote.SuperActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data = new DataProvider(this);
		Intent intent = getIntent();
		if (intent != null) {
			profileId = intent.getLongExtra(ProfileEdit.EXTRA_PROFILE_ID, -1);
		}
		if (profileId == -1) {
			profileId = sp.getLong(DATA_LAST_PROFILE, -1);
		}
		if (profileId == -1) {
			Intent i = new Intent(this, ProfileList.class);
			startActivity(i);
			finish();
		}

		Editor editor = sp.edit();
		editor.putLong(DATA_LAST_PROFILE, profileId);
		editor.commit();

		setContentView(R.layout.button_view);
		titlebar = findViewById(R.id.titlebar);
		buttons[0] = (Button) findViewById(R.id.button1);
		buttons[1] = (Button) findViewById(R.id.button2);
		buttons[2] = (Button) findViewById(R.id.button3);
		buttons[3] = (Button) findViewById(R.id.button4);
		buttons[4] = (Button) findViewById(R.id.button5);
		buttons[5] = (Button) findViewById(R.id.button6);
		buttons[6] = (Button) findViewById(R.id.button7);
		buttons[7] = (Button) findViewById(R.id.button8);
		buttons[8] = (Button) findViewById(R.id.button9);
		buttons[9] = (Button) findViewById(R.id.button10);
		buttons[10] = (Button) findViewById(R.id.button11);
		buttons[11] = (Button) findViewById(R.id.button12);
		loadData();
	}

	/**
	 * Load data.
	 */
	private void loadData() {
		data.open();
		for (int i = 0; i < 12; i++) {
			Cursor cursor = data.fetchButton(profileId, i);
			String text = null;
			if (cursor == null || cursor.getCount() < 1) {
				buttons[i].setVisibility(View.INVISIBLE);
			} else {
				buttons[i].setVisibility(View.VISIBLE);
				long buttonId = (long) cursor.getInt(cursor.getColumnIndex(DataProvider.BUTTONS_ROWID));
				SendBroadcastButtonListener clickButtonListener = new SendBroadcastButtonListener(buttonId, data, this);
				text = cursor.getString(cursor.getColumnIndex(DataProvider.BUTTONS_NAME));
				buttons[i].setOnClickListener(clickButtonListener);
				buttons[i].setText(text);
				// buttons[i].setBackgroundResource(R.drawable.button);
			}

		}
		data.close();
	}

	/* (non-Javadoc)
	 * @see cz.mpelant.droidmote.SuperActivity#onResume()
	 */
	@Override
	protected void onResume() {
		data.open();
		Cursor cursor = data.fetchProfile(profileId);
		if (cursor == null || cursor.getCount() < 1) {
			Editor editor = sp.edit();
			editor.remove(DATA_LAST_PROFILE);
			editor.commit();
			data.close();
			finish();
		}
		data.close();
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see cz.mpelant.droidmote.SuperActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case EDIT_REQ_CODE:
			loadData();
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see cz.mpelant.droidmote.SuperActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case R.id.editProfile:
			i = new Intent(this, ProfileEdit.class);
			i.putExtra(ProfileEdit.EXTRA_PROFILE_ID, profileId);
			startActivityForResult(i, EDIT_REQ_CODE);
			break;
		case R.id.deleteProfile:
			AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(this);
			deleteBuilder.setMessage(R.string.deleteMessage);
			deleteBuilder.setTitle(R.string.deleteTitle);
			deleteBuilder.setPositiveButton(getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					data.open();
					data.deleteProfile(profileId);
					data.close();
					Editor editor = sp.edit();
					editor.remove(DATA_LAST_PROFILE);
					editor.commit();
					Intent i = new Intent(ProfileView.this, ProfileList.class);
					i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(i);
					finish();
				}
			});

			deleteBuilder.setNegativeButton(getResources().getString(android.R.string.no), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteDialog.dismiss();
				}
			});
			deleteDialog = deleteBuilder.create();
			deleteDialog.show();
			break;

		}

		return super.onOptionsItemSelected(item);
	}

}
