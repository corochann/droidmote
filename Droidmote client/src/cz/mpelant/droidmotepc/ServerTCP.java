package cz.mpelant.droidmotepc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.ClosedByInterruptException;

public class ServerTCP implements Runnable {
	private Socket s;
	private ServerSocket ssIn;
	private int port;
	private MainWindow gui;
	private boolean started = false;
	public boolean stop = false;

	public ServerTCP(int port, MainWindow gui) throws IOException {
		this.port = port;
		this.gui = gui;
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

	
	public void connect(int port) throws UnknownHostException {
		this.port=port;
		connect();
	}
	public void connect() throws UnknownHostException {
		try {
			ssIn = new ServerSocket(port);
		} catch (IOException e1) {
			gui.println("Cannot open socket to port " + port);
		}

	}

	public void run() {
		stop = false;
		Keys keys = new Keys();
		started = true;
		gui.setButtonText("Stop");
		gui.println("listening");
		try {
			String str = "";
			while (!str.contains("stopListener") && !stop) {
				s = ssIn.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				str = in.readLine();
				if (!stop) {
					gui.println(str);
					InputSimulator.pressKey(str, keys, gui);
				}
			}
		} catch (ClosedByInterruptException e) {
			// gui.println("Listener interrupted");
			started = false;
		} catch (IOException e) {
			// gui.println("IO exception");
		} catch (Exception e) {
			// gui.println("exception");
		} finally {
			started = false;
			// gui.println("TCP Listener stopped");
			closeSocket();
		}
	}

	public boolean isRunning() {
		return started;
	}

	public void closeSocket() {
		try {
			s.close();
		} catch (Exception e) {
			// gui.println("IO exception");
		}
		try {
			ssIn.close();
		} catch (Exception e) {
			// gui.println("IO exception");
		}
	}
}
