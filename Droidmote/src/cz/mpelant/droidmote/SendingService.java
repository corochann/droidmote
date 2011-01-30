package cz.mpelant.droidmote;

import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Sends the commad via UDP or TCP to the pc
 */
public class SendingService extends Service {

	/** The TAG. */
	public static String TAG = "droidmote";

	/** The time for measuring the speed. */
	private long time;

	/** The timer for ending the service after 60 seconds since the last command received. */
	private Timer timer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * (non-Javadoc) This is the old onStart method that will be called on the pre-2.0 platform. On 2.0 or later we override onStartCommand() so this method will not be called.
	 * 
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		handleCommand(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleCommand(intent);
		return START_NOT_STICKY;
	}

	/**
	 * Handle the command.
	 * 
	 * @param intent
	 *            the intent
	 */
	private void handleCommand(Intent intent) {
		// measureTime("----------------handle Command");

		String type = intent.getStringExtra("type");
		String value = intent.getStringExtra("value");
		// Log.d(TAG, "type: " + type + ", value: " + value);
		String command = type + ":" + value;
		// Log.d(TAG, "command: " + command);

		int protocol = PreferenceManager.getDefaultSharedPreferences(this).getInt(SuperActivity.DATA_PROTOCOL, SuperActivity.DEFAULT_PROTOCOL);
		int port = PreferenceManager.getDefaultSharedPreferences(this).getInt(SuperActivity.DATA_PORT, SuperActivity.DEFAULT_PORT);
		try {
			if (protocol == SuperActivity.PROTOCOL_TCP) {
				String IP = PreferenceManager.getDefaultSharedPreferences(this).getString(SuperActivity.DATA_IP, SuperActivity.DEFAULT_IP);
				SendCommandTCP sendCommand = new SendCommandTCP(IP, port);
				sendCommand.execute(command);
			} else {
				String address = PreferenceManager.getDefaultSharedPreferences(this).getString(SuperActivity.DATA_UDP_ADDRESS, SuperActivity.DEFAULT_UDP_ADDRESS);
				SendCommandUDP sendCommand = new SendCommandUDP(address, port);
				sendCommand.execute(command);
			}
		} catch (Exception e) {
			Log.e(TAG, "an error occurred");
		}
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				stopSelf();
			}
		}, 1000 * 60);

	}

	/**
	 * Measure time.
	 * 
	 * @param tag
	 *            the tag
	 */
	private void measureTime(String tag) {
		long delta = System.currentTimeMillis() - time;
		time = System.currentTimeMillis();
		Log.v(TAG, delta + " :" + tag);
	}

	/**
	 * The UDP sender
	 */
	class SendCommandUDP extends AsyncTask<String, Void, Void> {

		/** The UDP group/address. */
		private String address;

		/** The port. */
		private int port;

		/**
		 * Instantiates a new send command udp.
		 * 
		 * @param address
		 *            the address
		 * @param port
		 *            the port
		 */
		public SendCommandUDP(String address, int port) {
			this.address = address;
			this.port = port;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(String... params) {
			try {
				MulticastSocket s = new MulticastSocket();
				try {
					// Log.d(TAG, "UDP command, port"+ port);
					String msg = params[0];
					InetAddress group = InetAddress.getByName(address);
					DatagramPacket p = new DatagramPacket(msg.getBytes("UTF-8"), msg.length(), group, port);
					s.send(p);
				} catch (Exception e) {
					Log.e(TAG, "UDP error " + e.toString());
				} finally {
					s.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * The TCP Sender
	 */
	class SendCommandTCP extends AsyncTask<String, Void, Void> {

		/** The port. */
		private int port;

		/** The IP. */
		private String IP;

		/** The socket. */
		Socket s = null;

		/** The output Stream. */
		OutputStream out = null;

		/**
		 * Instantiates a new send command tcp.
		 * 
		 * @param IP
		 *            the IP
		 * @param port
		 *            the port
		 */
		public SendCommandTCP(String IP, int port) {
			this.port = port;
			this.IP = IP;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(String... params) {

			try {
				// Log.d(TAG, "TCP command, port" + port + ", IP:" + IP);
				s = new Socket(IP, port);
				s.setReuseAddress(true);
				s.setTcpNoDelay(true);
				s.setSoTimeout(1);

				out = s.getOutputStream();
				params[0] += "\n";
				out.write(params[0].getBytes());
				// measureTime("----------------written");

			} catch (Exception e) {
				Log.e(TAG, "TCP error " + e.getMessage());
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
				try {
					s.close();
				} catch (Exception e) {
				}
			}

			return null;
		}
	}
}
