package cz.mpelant.droidmotepc;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton startStop = null;
	private JButton buttonSettings = null;
	private JPanel top = null;

	private ServerTCP serverTCP = null;
	private ServerUDP serverUDP = null;


	/**
	 * This is the default constructor
	 */
	public MainWindow() {
		super();
		initialize();
		serverTCP = new ServerTCP(this);
		serverUDP = new ServerUDP(this);
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
		this.setResizable(false);
		this.setSize(300, getHeight());
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
			// jContentPane.add(getStatusText(), BorderLayout.CENTER);
			jContentPane.add(getTop(), BorderLayout.NORTH);

		}
		return jContentPane;
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

					QRWindow qr = new QRWindow(MainWindow.this);
					qr.pack();
					qr.setVisible(true);

				}
			});

		}
		return buttonSettings;
	}

	private void startServers(){
		if (SharedPreferences.getBoolean(SharedPreferences.DATA_TCP_LISTENER, SharedPreferences.DEFAULT_TCP_LISTENER))
			startTCPListener();
		if (SharedPreferences.getBoolean(SharedPreferences.DATA_UDP_LISTENER, SharedPreferences.DEFAULT_UDP_LISTENER))
			startUDPListener();
	}
	
	private void stopServers(){
		stopTCPListener();
		stopUDPListener();
	}
	
	public synchronized void restartServers(){
		if (serverUDP.isRunning() || serverTCP.isRunning()) {
			stopServers();
			startServers();
		} 
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (serverUDP.isRunning() || serverTCP.isRunning()) {
			stopServers();
		} else {
			startServers();
		}

	}

	private void stopUDPListener() {
		serverUDP.stop();
	}

	private void stopTCPListener() {
		serverTCP.stop();
	}

	private void startUDPListener() {
		println("Starting UDP server...");
		serverUDP.start(SharedPreferences.getString(SharedPreferences.DATA_UDP_ADDRESS, SharedPreferences.DEFAULT_UDP_ADDRESS), SharedPreferences.getInt(SharedPreferences.DATA_PORT, SharedPreferences.DEFAULT_PORT));
	}

	private void startTCPListener() {
		println("Starting TCP server...");
		serverTCP.start(SharedPreferences.getInt(SharedPreferences.DATA_PORT, SharedPreferences.DEFAULT_PORT));
	}

	public synchronized void updateButtonText(){
		if (serverTCP.isRunning() || serverUDP.isRunning()) 
			startStop.setText("Stop");
		else
			startStop.setText("Start");
	}

	public synchronized void println(String text) {
		try {
			System.out.println(text);
		} catch (Exception e) {
		}

	}

	public void onClose() {
		if (serverTCP.isRunning() || serverUDP.isRunning()) {
			switch (JOptionPane.showConfirmDialog(MainWindow.this, "Opravdu chcete ukoncit program?", "Ukoncovaci dialog", JOptionPane.WARNING_MESSAGE)) {
			case JOptionPane.OK_OPTION:
				stopTCPListener();
				stopUDPListener();
				System.exit(0);
				break;
			case JOptionPane.CANCEL_OPTION:

				break;
			}
		} else {
			System.exit(0);
		}
	}

}
