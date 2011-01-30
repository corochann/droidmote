package cz.mpelant.droidmote;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * This listener is used for sending the commands after user clicks on a button
 * 
 * The listener interface for receiving sendBroadcastButton events.
 * The class that is interested in processing a sendBroadcastButton
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSendBroadcastButtonListener<code> method. When
 * the sendBroadcastButton event occurs, that object's appropriate
 * method is invoked.
 *
 * @see SendBroadcastButtonEvent
 */
public class SendBroadcastButtonListener implements OnClickListener {
	
	/** The button id. */
	private long buttonId;
	
	/** The type of the command. */
	private String type;
	
	/** The action. */
	private String action;
	
	/** The data provider. */
	private DataProvider data;
	
	/** The context. */
	private Activity mContext;

	/**
	 * Instantiates a new send broadcast button listener.
	 *
	 * @param buttonId the button id
	 * @param data the data provider
	 * @param mContext the context
	 */
	public SendBroadcastButtonListener(long buttonId, DataProvider data, Activity mContext) {
		this.buttonId = buttonId;
		this.data=data;
		this.mContext = mContext;
	}

	/**
	 * Retrieve the button data.
	 *
	 * @return true, if successful
	 */
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
					Log.d(SuperActivity.TAG, "getting action for button " + buttonId + " action:" + tmp);
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

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		data.open();
		if (retrieveData()) {
			Intent i = new Intent();
			i.setAction(SuperActivity.ACTION);
			i.putExtra("type", type);
			i.putExtra("value", action);
			Log.d(SuperActivity.TAG, "sending broadcast for " + action + "; type= " + type);
			mContext.sendBroadcast(i);
		}
		data.close();

	}
}
