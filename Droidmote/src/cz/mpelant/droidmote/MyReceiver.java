package cz.mpelant.droidmote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * The Class MyReceiver. Receives a broadcasted intent and starts the service that sends the action to the PC.
 */
public class MyReceiver extends BroadcastReceiver {

	/** The ACTION this receiver catches. */
	public static String ACTION = "cz.mpelant.droidmote.SEND_TO_PC";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, SendingService.class);
		i.putExtras(intent);
		context.startService(i);
	}

}
