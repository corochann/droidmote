package cz.mpelant.droidmotepc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerTCP extends Server {
	private Socket s;
	private ServerSocket ssIn;

	public ServerTCP(MainWindow gui) {
		super(gui);
	}

	public static String getIP() {
		String hostname = "";
		try {
			InetAddress addr = InetAddress.getLocalHost(); // Get IP Address
			hostname = addr.getHostAddress();

		} catch (UnknownHostException e) {
		}
		return hostname;

	}

	public void start(int port) {
		this.port = port;
		gui.println("starting TCP listener");
		start();
	}

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

	@Override
	public void stop() {
		gui.println("stopping TCP listener");
		super.stop();
	}
	
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
