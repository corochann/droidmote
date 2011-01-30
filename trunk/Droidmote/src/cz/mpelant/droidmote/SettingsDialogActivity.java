package cz.mpelant.droidmote;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.Menu;

/**
 * The Class SettingsDialogActivity. Used for launching the config dialog from external apk using cz.mpelant.droidmote.CONFIG intent. This activity is transparent so only the
 * dialog appears over the previous activity.
 */
public class SettingsDialogActivity extends SuperActivity {

	/*
	 * (non-Javadoc) disables the fullscreen setter
	 * 
	 * @see cz.mpelant.droidmote.SuperActivity#setFullscreen()
	 */
	@Override
	protected void setFullscreen() {
	}

	/*
	 * (non-Javadoc) disables the screenlock setter
	 * 
	 * @see cz.mpelant.droidmote.SuperActivity#setScreenLock()
	 */
	@Override
	protected void setScreenLock() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.mpelant.droidmote.SuperActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showDialog(DIALOG_SETTINGS);
		settingsDialog.dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				finish();
			}
		});
	}

	/*
	 * (non-Javadoc) No menu wanted here
	 * 
	 * @see cz.mpelant.droidmote.SuperActivity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	}
}
