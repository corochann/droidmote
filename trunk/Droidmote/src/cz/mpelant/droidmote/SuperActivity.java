package cz.mpelant.droidmote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class SuperActivity extends Activity {
	public static String TAG = "droidmote";
	public static String SCREENLOCK = "screenLock";
	public static final int DIALOG_SETTINGS = 1;
	public static final int DIALOG_ENABLE_WIFI = 2;
	
	public static final String DATA_IP = "serverIP";
	public static final String DATA_PROTOCOL = "protocol";
	public static final String DATA_PORT = "port";
	public static final String DATA_UDP_ADDRESS = "UDPAddress";
	public static final String DATA_LAST_PROFILE = "lastProfile";
	
	public static final int PROTOCOL_UDP = 0;
	public static final int PROTOCOL_TCP = 1;

	public static final String DEFAULT_IP = "192.168.0.1";
	public static final int DEFAULT_PROTOCOL = PROTOCOL_UDP;
	public static final int DEFAULT_PORT = 44522;
	public static final String DEFAULT_UDP_ADDRESS = "228.36.4.70";
	
	public static final String ACTION = "cz.mpelant.droidmote.SEND_TO_PC";
	public static final int EDIT_REQ_CODE = 1;
	public static final int DECODE_QR_REQ_CODE = 2;
	
	public static final String DATA_FULLSCREEN = "fullscreen";
	protected View titlebar;
	protected SettingsDialog settingsDialog;
	protected SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		super.onCreate(savedInstanceState);
	}

	protected void checkWiFi() {
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

		if (wifi != NetworkInfo.State.CONNECTED && wifi != NetworkInfo.State.CONNECTING) {
			showDialog(DIALOG_ENABLE_WIFI);
		}
	}

	protected void setFullscreen() {
		Log.d(TAG, "Fullscreen - " + sp.getBoolean(DATA_FULLSCREEN, false));
		if (sp.getBoolean(DATA_FULLSCREEN, false)) {

			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			titlebar.setVisibility(View.GONE);

		} else {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			titlebar.setVisibility(View.VISIBLE);

		}
	}

	protected void setScreenLock() {
		if (sp.getBoolean(SCREENLOCK, false))
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		else
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SETTINGS:
			settingsDialog = new SettingsDialog(this);
			return settingsDialog.createDialog();
		case DIALOG_ENABLE_WIFI:
			AlertDialog.Builder wifi = new AlertDialog.Builder(this);
			wifi.setTitle(R.string.wifi_dialog_title);
			wifi.setMessage(R.string.wifi_dialog_message);
			wifi.setPositiveButton(R.string.wifi_dialog_ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				}
			});
			wifi.setNegativeButton(android.R.string.cancel, null);
			return wifi.create();
		default:
			return super.onCreateDialog(id);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case DECODE_QR_REQ_CODE:
			if (resultCode == RESULT_OK) {
				settingsDialog.QRActivityResult(data);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		setFullscreen();
		setScreenLock();
		super.onResume();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem mi = menu.findItem(R.id.toggleScreenLock);
		if (mi == null)
			return super.onPrepareOptionsMenu(menu);
		if (sp.getBoolean(SCREENLOCK, false))
			mi.setTitle(R.string.screenLockOn);
		else
			mi.setTitle(R.string.screenLockOff);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case R.id.toggleFullscreen:
			Editor editor = sp.edit();
			if (sp.getBoolean(DATA_FULLSCREEN, false))
				editor.putBoolean(DATA_FULLSCREEN, false);
			else
				editor.putBoolean(DATA_FULLSCREEN, true);
			editor.commit();
			setFullscreen();
			break;
		case R.id.setIP:
			showDialog(DIALOG_SETTINGS);
			break;
		case R.id.profileList:
			i = new Intent(this, ProfileList.class);
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
			break;
		case R.id.toggleScreenLock:
			Editor ed = sp.edit();
			if (sp.getBoolean(SCREENLOCK, false))
				ed.putBoolean(SCREENLOCK, false);
			else
				ed.putBoolean(SCREENLOCK, true);
			ed.commit();
			setScreenLock();
			break;
		case R.id.modeTouchpad:
			i = new Intent(this, TouchPad.class);
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
