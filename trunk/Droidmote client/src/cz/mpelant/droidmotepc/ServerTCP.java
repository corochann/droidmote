package cz.mpelant.droidmotepc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * TCP listener.
 */
public class ServerTCP extends Server {

	/** The socket. */
	private Socket s;

	/** The server socket in. */
	private ServerSocket ssIn;

	/**
	 * Instantiates a new server tcp.
	 * 
	 * @param gui the gui
	 */
	public ServerTCP(MainWindow gui) {
		super(gui);
	}

	/**
	 * Gets the local IP.
	 * 
	 * @return the local IP
	 */
	public static String getIP() {
		String hostname = "";
		try {
			InetAddress addr = InetAddress.getLocalHost(); // Get IP Address
			hostname = addr.getHostAddress();

		} catch (UnknownHostException e) {
		}
		return hostname;

	}

	/**
	 * Start.
	 * 
	 * @param port the port
	 */
	public void start(int port) {
		this.port = port;
		gui.println("starting TCP listener");
		start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.mpelant.droidmotepc.Server#doInBackground()
	 */
	public void doInBackground() throws IOException {
		ssIn = new ServerSocket(port);
		String str = "";
		while (!str.contains("stopListener") && !stop) {
			s = ssIn.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			str = in.readLine();
			if (!stop) {
				InputSimulator.pressKey(str, keys, gui);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.mpelant.droidmotepc.Server#stop()
	 */
	@Override
	public void stop() {
		gui.println("stopping TCP listener");
		super.stop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.mpelant.droidmotepc.Server#closeSocket()
	 */
	@Override
	public void closeSocket() {
		try {
			s.close();
		} catch (Exception e) {
		}
		try {
			ssIn.close();
		} catch (Exception e) {
		}
	}
}
