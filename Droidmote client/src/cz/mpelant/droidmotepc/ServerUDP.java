package cz.mpelant.droidmotepc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.channels.ClosedByInterruptException;

public class ServerUDP implements Runnable {
	private MulticastSocket s;
	private String address;
	private int port;
	private MainWindow gui;
	private byte[] buf = new byte[2048];
	private boolean started = false;
	public boolean stop = false;

	public ServerUDP(String address, int port, MainWindow gui) throws IOException {
		this.port = port;
		this.address = address;
		this.gui = gui;
	}

	
	public void connect(String address, int port) throws UnknownHostException {
		this.address=address;
		this.port=port;
		connect();
	}
	public void connect() throws UnknownHostException {
		try {
			s = new MulticastSocket(port);
		} catch (IOException e1) {
			gui.println("Cannot open socket to port " + port);
		}
		InetAddress group = InetAddress.getByName(address);
		try {
			s.joinGroup(group);
		} catch (IOException e) {
			gui.println("Cannot join group " + port);
			s.close();
		}
	}

	public void run() {
		stop = false;
		Keys keys = new Keys();
		DatagramPacket recv = new DatagramPacket(buf, buf.length);
		started = true;
		gui.setButtonText("Stop");
		gui.println("listening");
		try {
			String str = "";
			while (!str.contains("stopListener") && !stop) {
				s.receive(recv);
				str = new String(recv.getData(), 0, recv.getLength(), "UTF-8");
				if (!stop) {
					gui.println(str);
					InputSimulator.pressKey(str, keys, gui);
				}
			}
		} catch (ClosedByInterruptException e) {
//			gui.println("UDP Listener interrupted");
			started = false;
		} catch (IOException e) {
			started = false;
//			gui.println("IO exception");
			started = false;
		} catch (Exception e) {
//			gui.println("exception");
		} finally {
			started = false;
//			gui.println("UDP  Listener stopped");

			closeSocket();
		}
	}

	public boolean isRunning() {
		return started;
	}

	public void closeSocket() {
		s.close();
	}
}
