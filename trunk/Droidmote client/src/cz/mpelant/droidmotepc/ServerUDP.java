package cz.mpelant.droidmotepc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ServerUDP extends Server {
	private MulticastSocket s;
	private String address;
	private byte[] buf = new byte[2048];

	public ServerUDP(MainWindow gui) {
		super(gui);
	}

	public void start(String address, int port) {
		this.address = address;
		this.port = port;
		gui.println("starting UDP listener");
		start();
	}

	public void doInBackground() throws IOException {
		s = new MulticastSocket(port);
		s.joinGroup(InetAddress.getByName(address));
		DatagramPacket recv = new DatagramPacket(buf, buf.length);
		String str = "";
		while (!str.contains("stopListener") && !stop) {
			s.receive(recv);
			str = new String(recv.getData(), 0, recv.getLength(), "UTF-8");
			if (!stop) {
				InputSimulator.pressKey(str, keys, gui);
			}
		}
	}


	@Override
	public void stop() {
		gui.println("stopping UDP listener");
		super.stop();
	}
	@Override
	public void closeSocket() {
		s.close();
	}
}
