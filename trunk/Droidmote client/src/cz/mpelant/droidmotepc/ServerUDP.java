package cz.mpelant.droidmotepc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * UDP listener.
 */
public class ServerUDP extends Server {

	/** The socket. */
	private MulticastSocket s;

	/** The UDP group/address. */
	private String address;

	/** The buffer. */
	private byte[] buf = new byte[2048];

	/**
	 * Instantiates a new server udp.
	 * 
	 * @param gui the gui
	 */
	public ServerUDP(MainWindow gui) {
		super(gui);
	}

	/**
	 * Start.
	 * 
	 * @param address the UDp group/address
	 * @param port the port
	 */
	public void start(String address, int port) {
		this.address = address;
		this.port = port;
		gui.println("starting UDP listener");
		start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.mpelant.droidmotepc.Server#doInBackground()
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.mpelant.droidmotepc.Server#stop()
	 */
	@Override
	public void stop() {
		gui.println("stopping UDP listener");
		super.stop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.mpelant.droidmotepc.Server#closeSocket()
	 */
	@Override
	public void closeSocket() {
		s.close();
	}
}
