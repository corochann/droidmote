package cz.mpelant.droidmotepc;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTextArea statusText = null;
	private JPanel statusTextPanel = null;
	private JButton startStop = null;
	private JButton buttonSettings = null;
	private JPanel top = null;

	private ServerTCP serverTCP = null;
	private ServerUDP serverUDP = null;
	private Thread threadTCP = null;
	private Thread threadUDP = null;

	/**
	 * This is the default constructor
	 */
	public MainWindow() {
		super();
		initialize();
		try {
			serverTCP = new ServerTCP(SharedPreferences.getInt(SharedPreferences.DATA_PORT, SharedPreferences.DEFAULT_PORT), this);
			serverUDP = new ServerUDP(SharedPreferences.getString(SharedPreferences.DATA_UDP_ADDRESS, SharedPreferences.DEFAULT_UDP_ADDRESS), SharedPreferences.getInt(SharedPreferences.DATA_PORT, SharedPreferences.DEFAULT_PORT), this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onClose();
			}
		});

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getStatusText(), BorderLayout.CENTER);
			jContentPane.add(getTop(), BorderLayout.NORTH);

		}
		return jContentPane;
	}

	/**
	 * This method initializes statusText
	 * 
	 * @return javax.swing.JTextPane
	 */
	private JPanel getStatusText() {
		if (statusText == null) {
			statusText = new JTextArea();
			statusText.setEditable(false);
			JScrollPane scrollingArea = new JScrollPane(statusText);
			statusTextPanel = new JPanel();
			statusTextPanel.setLayout(new BorderLayout());
			statusTextPanel.add(scrollingArea, BorderLayout.CENTER);
			statusTextPanel.setAutoscrolls(true);
			statusTextPanel.repaint();
		}
		return statusTextPanel;
	}

	private JPanel getTop() {
		if (top == null) {
			top = new JPanel();
			top.setLayout(new GridBagLayout());
			GridBagConstraints gBC = new GridBagConstraints();
			gBC.fill = GridBagConstraints.HORIZONTAL;

			gBC.ipady = 0;
			gBC.weightx = 0.5;
			gBC.weighty = 0.0;
			gBC.insets = new Insets(0, 0, 0, 0);
			gBC.gridx = 0;
			gBC.gridy = 0;
			top.add(getStartStop());

			gBC.gridx = 1;
			top.add(getButtonSettings(), gBC);

		}
		return top;
	}

	/**
	 * This method initializes startStop
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getStartStop() {
		if (startStop == null) {
			startStop = new JButton();
			startStop.setText("Start");
			startStop.addActionListener(this);

		}
		return startStop;
	}

	private JButton getButtonSettings() {
		if (buttonSettings == null) {
			buttonSettings = new JButton();
			buttonSettings.setText("Nastaveni");
			buttonSettings.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					QRWindow qr = new QRWindow();
					qr.pack();
					qr.setVisible(true);

				}
			});

		}
		return buttonSettings;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (serverUDP.isRunning() || serverTCP.isRunning()) {
			stopTCPListener();
			stopUDPListener();
			setButtonText("start");
		} else {
			if (SharedPreferences.getBoolean(SharedPreferences.DATA_TCP_LISTENER, SharedPreferences.DEFAULT_TCP_LISTENER))
				startTCPListener();
			if (SharedPreferences.getBoolean(SharedPreferences.DATA_UDP_LISTENER, SharedPreferences.DEFAULT_UDP_LISTENER))
				startUDPListener();
		}

	}

	private void stopUDPListener() {
		try {

			serverUDP.stop = true;
			threadUDP.interrupt();
			serverUDP.closeSocket();

		} catch (Exception e1) {

		}
	}

	private void stopTCPListener() {
		try {

			serverTCP.stop = true;
			threadTCP.interrupt();
			serverTCP.closeSocket();

		} catch (Exception e1) {

		}
	}

	private void startUDPListener() {
		try {

			println("Starting UDP server...");

			serverUDP.connect(SharedPreferences.getString(SharedPreferences.DATA_UDP_ADDRESS, SharedPreferences.DEFAULT_UDP_ADDRESS), SharedPreferences.getInt(SharedPreferences.DATA_PORT, SharedPreferences.DEFAULT_PORT));
			threadUDP = new Thread(serverUDP);
			threadUDP.start();

		} catch (Exception e1) {

		}
	}

	private void startTCPListener() {
		try {

			println("Starting TCP server...");
			serverTCP.connect(SharedPreferences.getInt(SharedPreferences.DATA_PORT, SharedPreferences.DEFAULT_PORT));
			threadTCP = new Thread(serverTCP);
			threadTCP.start();
		} catch (Exception e1) {

		}
	}

	public synchronized void setButtonText(String text) {
		try {
			startStop.setText(text);
		} catch (Exception e) {
		}
	}

	public synchronized void println(String text) {
		try {
			statusText.append(text + '\n');
			statusTextPanel.repaint();
		} catch (Exception e) {
		}

	}

	public void onClose() {
		if (serverTCP.isRunning() && threadTCP.isAlive() || serverUDP.isRunning() && threadUDP.isAlive()) {
			switch (JOptionPane.showConfirmDialog(MainWindow.this, "Opravdu chcete ukoncit program?", "Ukoncovaci dialog", JOptionPane.WARNING_MESSAGE)) {
			case JOptionPane.OK_OPTION:
				stopTCPListener();
				stopUDPListener();
				setButtonText("Start");
				System.exit(0);
				break;
			case JOptionPane.CANCEL_OPTION:

				break;
			}
			// TODO: zavrit UDP
		} else {
			System.exit(0);
		}
	}

}
