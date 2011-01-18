package cz.mpelant.droidmote;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class SuperActivity extends Activity {
	public static String TAG = "droidmote";
	public static String SCREENLOCK = "screenLock";
	protected View titlebar;
	protected SettingsDialog settingsDialog;
	protected SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		super.onCreate(savedInstanceState);
	}

	protected void setFullscreen() {
		Log.d(TAG, "Fullscreen - " + sp.getBoolean(ProfileEdit.DATA_FULLSCREEN, false));
		if (sp.getBoolean(ProfileEdit.DATA_FULLSCREEN, false)) {

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
		case ProfileEdit.DIALOG_SETTINGS:
			settingsDialog = new SettingsDialog(this);
			return settingsDialog.createDialog();

		default:
			return super.onCreateDialog(id);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ProfileEdit.DECODE_QR_REQ_CODE:
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
		if(mi==null)
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
			if (sp.getBoolean(ProfileEdit.DATA_FULLSCREEN, false))
				editor.putBoolean(ProfileEdit.DATA_FULLSCREEN, false);
			else
				editor.putBoolean(ProfileEdit.DATA_FULLSCREEN, true);
			editor.commit();
			setFullscreen();
			break;
		case R.id.setIP:
			showDialog(ProfileEdit.DIALOG_SETTINGS);
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
