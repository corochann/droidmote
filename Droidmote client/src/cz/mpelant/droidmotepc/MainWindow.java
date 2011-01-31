package cz.mpelant.droidmotepc;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The Class MainWindow.
 */
public class MainWindow extends JFrame implements ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The jcontent pane. */
	private JPanel jContentPane = null;

	/** The start stop button. */
	private JButton startStop = null;

	/** The settings button. */
	private JButton buttonSettings = null;

	/** The top panel. */
	private JPanel top = null;

	/** The tcp server. */
	private ServerTCP serverTCP = null;

	/** The udp server. */
	private ServerUDP serverUDP = null;
	
	/** The localization messages. */
	private ResourceBundle messages;

	/**
	 * This is the default constructor.
	 */
	public MainWindow() {
		super();
		messages=Utf8ResourceBundle.getBundle("cz.mpelant.droidmotepc.data.MessagesBundle");
		initialize();
		serverTCP = new ServerTCP(this);
		serverUDP = new ServerUDP(this);
	}

	/**
	 * This method initializes this.
	 * 
	 * @return void
	 */
	private void initialize() {
		setResizable(false);
		setSize(300, getHeight());
		setContentPane(getJContentPane());
		setTitle("Droidmote");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onClose();
			}
		});
		pack();
		setVisible(true);
	}

	/**
	 * This method initializes jContentPane.
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

	/**
	 * Gets the top.
	 * 
	 * @return the top
	 */
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
	 * This method initializes startStop button.
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getStartStop() {
		if (startStop == null) {
			startStop = new JButton();
			startStop.setText(messages.getString("start"));
			startStop.addActionListener(this);

		}
		return startStop;
	}

	/**
	 * Gets the settings button.
	 * 
	 * @return the settings button
	 */
	private JButton getButtonSettings() {
		if (buttonSettings == null) {
			buttonSettings = new JButton();
			buttonSettings.setText(messages.getString("settings"));
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

	/**
	 * Start servers.
	 */
	private void startServers() {
		if (SharedPreferences.getBoolean(SharedPreferences.DATA_TCP_LISTENER, SharedPreferences.DEFAULT_TCP_LISTENER))
			startTCPListener();
		if (SharedPreferences.getBoolean(SharedPreferences.DATA_UDP_LISTENER, SharedPreferences.DEFAULT_UDP_LISTENER))
			startUDPListener();
	}

	/**
	 * Stop servers.
	 */
	private void stopServers() {
		stopTCPListener();
		stopUDPListener();
	}

	/**
	 * Restart servers.
	 */
	public synchronized void restartServers() {
		if (serverUDP.isRunning() || serverTCP.isRunning()) {
			stopServers();
			startServers();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (serverUDP.isRunning() || serverTCP.isRunning()) {
			stopServers();
		} else {
			startServers();
		}

	}

	/**
	 * Stop udp listener.
	 */
	private void stopUDPListener() {
		serverUDP.stop();
	}

	/**
	 * Stop tcp listener.
	 */
	private void stopTCPListener() {
		serverTCP.stop();
	}

	/**
	 * Start udp listener.
	 */
	private void startUDPListener() {
		println("Starting UDP server...");
		serverUDP.start(SharedPreferences.getString(SharedPreferences.DATA_UDP_ADDRESS, SharedPreferences.DEFAULT_UDP_ADDRESS), SharedPreferences.getInt(SharedPreferences.DATA_PORT, SharedPreferences.DEFAULT_PORT));
	}

	/**
	 * Start tcp listener.
	 */
	private void startTCPListener() {
		println("Starting TCP server...");
		serverTCP.start(SharedPreferences.getInt(SharedPreferences.DATA_PORT, SharedPreferences.DEFAULT_PORT));
	}

	/**
	 * Update button text.
	 */
	public synchronized void updateButtonText() {
		if (serverTCP.isRunning() || serverUDP.isRunning())
			startStop.setText(messages.getString("stop"));
		else
			startStop.setText(messages.getString("start"));
	}

	/**
	 * Print the LOG
	 * 
	 * @param text the text to print
	 */
	public synchronized void println(String text) {
		try {
			System.out.println(text);
		} catch (Exception e) {
		}

	}

	/**
	 * This is called when closing the window. Stops the listeners if running.
	 */
	public void onClose() {
		if (serverTCP.isRunning() || serverUDP.isRunning()) {
			switch (JOptionPane.showConfirmDialog(MainWindow.this,messages.getString("exit_dialog_message"), messages.getString("exit_dialog_title"), JOptionPane.WARNING_MESSAGE)) {
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
