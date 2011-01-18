package cz.mpelant.droidmote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ProfileEdit extends Activity {
	public static final String ACTION = "cz.mpelant.droidmote.SEND_TO_PC";
	public static final int EDIT_REQ_CODE = 1;
	public static final int DECODE_QR_REQ_CODE = 2;
	public static String TAG = "droidmote";

	public static final String DATA_IP = "serverIP";
	public static final String DATA_PROTOCOL = "protocol";
	public static final String DATA_PORT = "port";
	public static final String DATA_UDP_ADDRESS = "UDPAddress";
	public static final String DATA_LAST_PROFILE = "lastProfile";
	public static final String DATA_FULLSCREEN = "fullscreen";

	public static final int PROTOCOL_UDP = 0;
	public static final int PROTOCOL_TCP = 1;

	public static final String DEFAULT_IP = "192.168.0.1";
	public static final int DEFAULT_PROTOCOL = PROTOCOL_UDP;
	public static final int DEFAULT_PORT = 44522;
	public static final String DEFAULT_UDP_ADDRESS = "228.36.4.70";

	public static final String DEFAULT_PROFILE_NAME = "New Profile";

	public static final String EXTRA_PROFILE_ID = "profileId";

	public static final int DIALOG_SETTINGS = 1;

	private Button[] buttons = new Button[12];
	private DataProvider data;
	private long profileId;
	private SettingsDialog settingsDialog;
	private LinearLayout view;
	private EditText edProfileName;
	private AlertDialog deleteDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data = new DataProvider(this);
		Intent intent = getIntent();
		profileId = intent.getLongExtra(EXTRA_PROFILE_ID, -1);
		if (profileId == -1) {
			data.open();
			profileId = data.createProfile(DEFAULT_PROFILE_NAME);
			data.close();
		}

		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putLong(DATA_LAST_PROFILE, profileId);
		editor.commit();

		setContentView(R.layout.profile_edit);
		LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
		edProfileName = (EditText) findViewById(R.id.edProfileName);
		Button save = (Button) findViewById(R.id.btSave);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				data.open();
				data.updateProfile(profileId, edProfileName.getText().toString());
				data.close();
				setResult(Activity.RESULT_OK);
				finish();
			}
		});

		view = (LinearLayout) getLayoutInflater().inflate(R.layout.button_view, null);
		buttonLayout.addView(view);

		buttons[0] = (Button) view.findViewById(R.id.button1);
		buttons[1] = (Button) view.findViewById(R.id.button2);
		buttons[2] = (Button) view.findViewById(R.id.button3);
		buttons[3] = (Button) view.findViewById(R.id.button4);
		buttons[4] = (Button) view.findViewById(R.id.button5);
		buttons[5] = (Button) view.findViewById(R.id.button6);
		buttons[6] = (Button) view.findViewById(R.id.button7);
		buttons[7] = (Button) view.findViewById(R.id.button8);
		buttons[8] = (Button) view.findViewById(R.id.button9);
		buttons[9] = (Button) view.findViewById(R.id.button10);
		buttons[10] = (Button) view.findViewById(R.id.button11);
		buttons[11] = (Button) view.findViewById(R.id.button12);

		loadData();

	}

	class EditButtonListener implements OnLongClickListener {
		private long buttonId;
		private int pos;

		public EditButtonListener(long buttonId, int pos) {
			this.buttonId = buttonId;
			this.pos = pos;
		}

		public EditButtonListener(int pos) {
			this.buttonId = -1;
			this.pos = pos;
		}

		private void startEditActivity() {
			Intent i = new Intent(ProfileEdit.this, ButtonEdit.class);
			i.putExtra(ButtonEdit.EXTRA_BUTTON_ID, buttonId);
			i.putExtra(ButtonEdit.EXTRA_PROFILE_ID, profileId);
			i.putExtra(ButtonEdit.EXTRA_POSITION, pos);
			startActivityForResult(i, EDIT_REQ_CODE);
		}

		@Override
		public boolean onLongClick(View v) {
			if (buttonId == -1)
				startEditActivity();
			else {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileEdit.this);
				alertDialog.setPositiveButton(getResources().getString(R.string.edit), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						startEditActivity();
					}
				});

				alertDialog.setNegativeButton(getResources().getString(R.string.clear), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						data.open();
						data.deleteButton(buttonId);
						data.close();
						loadData();
					}
				});
				alertDialog.show();
			}
			return true;
		}

	}

	private void loadData() {
		data.open();
		Cursor c = data.fetchProfile(profileId);
		if (c != null && c.getCount() > 0) {
			int colIndex = c.getColumnIndex(DataProvider.PROFILES_NAME);
			String text = c.getString(colIndex);
			Log.d(TAG, "name loaded " + text);
			edProfileName.setText(text);
		} else {
			Log.d(TAG, "name NOT loaded");
		}
		for (int i = 0; i < 12; i++) {
			EditButtonListener editButtonListener = null;
			Cursor cursor = data.fetchButton(profileId, i);
			String text = null;
			if (cursor == null || cursor.getCount() < 1) {
				editButtonListener = new EditButtonListener(i);
				text = "-";
			} else {
				long buttonId = (long) cursor.getInt(cursor.getColumnIndex(DataProvider.BUTTONS_ROWID));
				SendBroadcastButtonListener clickButtonListener = new SendBroadcastButtonListener(buttonId, data, this);
				editButtonListener = new EditButtonListener(buttonId, i);
				text = cursor.getString(cursor.getColumnIndex(DataProvider.BUTTONS_NAME));
				buttons[i].setOnClickListener(clickButtonListener);
			}
			buttons[i].setOnLongClickListener(editButtonListener);
			buttons[i].setText(text);
//			buttons[i].setBackgroundResource(android.R.drawable.btn_default);
		}
		data.close();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SETTINGS:
			settingsDialog = new SettingsDialog(this);
			return settingsDialog.createDialog();

		default:
			return super.onCreateDialog(id);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDIT_REQ_CODE && resultCode == RESULT_OK) {
			Log.d(TAG, "refreshing view");
			loadData();
		}
		if (requestCode == DECODE_QR_REQ_CODE) {
			if (resultCode == RESULT_OK) {
				settingsDialog.QRActivityResult(data);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		menu.removeItem(R.id.profileList);
		menu.removeItem(R.id.editProfile);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.setIP:
			showDialog(DIALOG_SETTINGS);
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
					Editor editor = PreferenceManager.getDefaultSharedPreferences(ProfileEdit.this).edit();
					editor.remove(ProfileEdit.DATA_LAST_PROFILE);
					editor.commit();
					Intent i = new Intent(ProfileEdit.this, ProfileList.class);
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
