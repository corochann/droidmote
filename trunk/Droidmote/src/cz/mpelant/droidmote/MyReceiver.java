package cz.mpelant.droidmote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
	public static String ACTION = "cz.mpelant.droidmote.SEND_TO_PC";
	public static String TAG = "droidmote";

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, SendingService.class);
		i.putExtras(intent);
		context.startService(i);
	}

}
