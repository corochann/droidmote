package cz.mpelant.droidmote;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class SendBroadcastButtonListener implements OnClickListener {
	private long buttonId;
	private String type;
	private String action;
	private DataProvider data;
	private Activity mContext;

	public SendBroadcastButtonListener(long buttonId, DataProvider data, Activity mContext) {
		this.buttonId = buttonId;
		this.data=data;
		this.mContext = mContext;
	}

	private boolean retrieveData() {
		action = "";
		String append = "";
		Cursor actions = data.fetchAllActions(buttonId);
		int colAction = actions.getColumnIndex(DataProvider.ACTIONS_ACTION);
		if (actions != null) {
			if (actions.moveToFirst()) {
				int i = 0;
				do {
					i++;
					String tmp = actions.getString(colAction);
					Log.d(ProfileEdit.TAG, "getting action for button " + buttonId + " action:" + tmp);
					action += append + tmp;
					append = ";";
				} while (actions.moveToNext());
				if (i == 1)
					type = Commands.COMMAND_KEY_EVENT_ID;
				else
					type = Commands.COMMAND_MULTI_KEY_EVENT_ID;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	@Override
	public void onClick(View v) {
		data.open();
		if (retrieveData()) {
			Intent i = new Intent();
			i.setAction(ProfileEdit.ACTION);
			i.putExtra("type", type);
			i.putExtra("value", action);
			Log.d(ProfileEdit.TAG, "sending broadcast for " + action + "; type= " + type);
			mContext.sendBroadcast(i);
		}
		data.close();

	}
}
