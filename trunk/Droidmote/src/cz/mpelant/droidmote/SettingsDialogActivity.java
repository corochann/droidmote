package cz.mpelant.droidmote;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.Menu;

public class SettingsDialogActivity extends SuperActivity{
	@Override
	protected void setFullscreen() {
	}
	@Override
	protected void setScreenLock() {
	}
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
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	}
}
