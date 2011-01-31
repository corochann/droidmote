package cz.mpelant.droidmotepc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The Class QRWindow.
 */
public class QRWindow extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The jcontent pane. */
	private JPanel jContentPane = null;

	/** The settings panel. */
	private JPanel settings = null;

	/** The QR code panel. */
	private QRPanel QRCode = null; // @jve:decl-index=0:

	/** The QR image. */
	private Image img; // @jve:decl-index=0:

	/** The port. */
	private JTextField port = null;

	/** The address. */
	private JTextField address = null;

	/** The show qr button. */
	private JButton showQR = null;

	/** The save button. */
	private JButton save = null;

	/** The qr generator. */
	private QRGenerator qrGenerator;

	/** The udp checkbox. */
	private JCheckBox cbUDP;

	/** The tcp checkbox. */
	private JCheckBox cbTCP;

	/** The main window. */
	private MainWindow mainWindow;
	
	/** The localization messages. */
	private ResourceBundle messages;

	/**
	 * The Class QRPanel.
	 */
	class QRPanel extends JPanel {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		public void paintComponent(Graphics g) {
			setPreferredSize(new Dimension(img.getWidth(null), img.getWidth(null)));
			setSize(new Dimension(img.getWidth(null), img.getWidth(null)));
			g.drawImage(img, 0, 0, null);
			setPreferredSize(new Dimension(img.getWidth(null), img.getWidth(null)));
			setSize(new Dimension(img.getWidth(null), img.getWidth(null)));
		}
	}

	/**
	 * This is the default constructor.
	 * 
	 * @param mainWindow the main window
	 */
	public QRWindow(MainWindow mainWindow) {
		super();
		messages=Utf8ResourceBundle.getBundle("cz.mpelant.droidmotepc.data.MessagesBundle");
		initialize();
		this.mainWindow = mainWindow;

	}

	/**
	 * This method initializes this.
	 * 
	 * @return void
	 */
	private void initialize() {
		qrGenerator = new QRGenerator(10);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
		this.setResizable(true);

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
			jContentPane.add(getSettings(), BorderLayout.NORTH);
			loadSettings();
			img = qrGenerator.encode(getDataForQR());
			jContentPane.add(getQRCode(), BorderLayout.CENTER);

		}
		return jContentPane;
	}

	/**
	 * Gets the settings.
	 * 
	 * @return the settings
	 */
	private JPanel getSettings() {
		if (settings == null) {
			setProtocolBoxes();
			settings = new JPanel();
			settings.setLayout(new GridBagLayout());
			GridBagConstraints gBC = new GridBagConstraints();
			gBC.fill = GridBagConstraints.HORIZONTAL;

			gBC.ipady = 0;
			gBC.weightx = 0.0;
			gBC.weighty = 0.0;
			gBC.gridwidth = 1;
			gBC.anchor = GridBagConstraints.WEST;
			gBC.insets = new Insets(0, 10, 0, 0);
			gBC.gridx = 0;
			gBC.gridy = 0;
			gBC.ipadx = 5;
			settings.add(new JLabel(messages.getString("port")+":"), gBC);

			gBC.gridx = 1;
			gBC.ipadx = 50;
			settings.add(getTextPort(), gBC);

			gBC.ipadx = 5;
			gBC.gridx = 2;
			gBC.insets = new Insets(0, 10, 0, 0);
			settings.add(new JLabel(messages.getString("udp_address")+":"), gBC);

			gBC.gridx = 3;
			gBC.ipadx = 150;
			settings.add(getTextAddress(), gBC);

			gBC.ipadx = 5;
			gBC.gridx = 0;
			gBC.gridy = 1;
			settings.add(new JLabel(messages.getString("accept_protocols")+":"), gBC);

			gBC.gridx = 1;
			settings.add(cbTCP, gBC);

			gBC.gridx = 2;
			settings.add(cbUDP, gBC);

			gBC.gridx = 3;
			settings.add(new JLabel(messages.getString("current_ip")+": " + ServerTCP.getIP()), gBC);

			gBC.insets = new Insets(0, 0, 0, 0);
			gBC.ipady = 20;
			gBC.ipadx = 0;
			gBC.weightx = 0.0;
			gBC.gridwidth = 2;
			gBC.gridx = 0;
			gBC.gridy = 2;
			settings.add(getButtonQR(), gBC);
			gBC.gridwidth = 2;
			gBC.gridx = 2;
			settings.add(getButtonSave(), gBC);

		}
		return settings;
	}

	/**
	 * Sets the protocol boxes.
	 */
	private void setProtocolBoxes() {
		cbUDP = new JCheckBox("UDP");

		cbUDP.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					address.setEnabled(true);
				} else {
					address.setEnabled(false);
				}

			}
		});

		cbTCP = new JCheckBox("TCP");

	}

	/**
	 * Gets the QR code.
	 * 
	 * @return the QR code
	 */
	private JPanel getQRCode() {
		if (QRCode == null) {
			QRCode = new QRPanel();
			QRCode.setPreferredSize(new Dimension(img.getWidth(null), img.getWidth(null)));
			QRCode.setSize(new Dimension(img.getWidth(null), img.getWidth(null)));
			QRCode.setVisible(false);

		}
		return QRCode;
	}

	/**
	 * This method initializes port textfield.
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTextPort() {
		if (port == null) {
			port = new JTextField();
		}
		return port;
	}

	/**
	 * Show alert dialog.
	 * 
	 * @param text the text
	 */
	private void showAlertDialog(String text) {
		JOptionPane.showMessageDialog(this, text);
	}

	/**
	 * Load settings.
	 */
	private void loadSettings() {
		if (port != null)
			port.setText(SharedPreferences.getInt(SharedPreferences.DATA_PORT, SharedPreferences.DEFAULT_PORT) + "");
		if (address != null)
			address.setText(SharedPreferences.getString(SharedPreferences.DATA_UDP_ADDRESS, SharedPreferences.DEFAULT_UDP_ADDRESS));
		if (cbTCP != null)
			cbTCP.setSelected(SharedPreferences.getBoolean(SharedPreferences.DATA_TCP_LISTENER, SharedPreferences.DEFAULT_TCP_LISTENER));
		if (cbUDP != null)
			cbUDP.setSelected(SharedPreferences.getBoolean(SharedPreferences.DATA_UDP_LISTENER, SharedPreferences.DEFAULT_UDP_LISTENER));
	}

	/**
	 * Save settings.
	 */
	private void saveSettings() {
		int portInt;
		try {
			portInt = Integer.parseInt(port.getText().toString());
		} catch (Exception e1) {
			showAlertDialog(messages.getString("illegal_port"));
			return;
		}
		if (portInt > 65535) {
			showAlertDialog(messages.getString("illegal_port"));
			return;
		}
		SharedPreferences.putInt(SharedPreferences.DATA_PORT, Integer.parseInt(port.getText()));
		SharedPreferences.putString(SharedPreferences.DATA_UDP_ADDRESS, address.getText());
		SharedPreferences.putBoolean(SharedPreferences.DATA_TCP_LISTENER, cbTCP.isSelected());
		SharedPreferences.putBoolean(SharedPreferences.DATA_UDP_LISTENER, cbUDP.isSelected());

	}

	/**
	 * Gets the save button.
	 * 
	 * @return the save button
	 */
	private JButton getButtonSave() {
		if (save == null) {
			save = new JButton();
			save.setText(messages.getString("save"));
			save.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					saveSettings();
					refreshQR();
					mainWindow.restartServers();
				}
			});
		}
		return save;
	}

	/**
	 * Gets the data for qr.
	 * 
	 * @return the data for qr
	 */
	private String getDataForQR() {
		String rtrn = "";
		String separator = "";
		if (cbUDP.isSelected()) {
			rtrn = "UDP";
			separator = "|";
		}
		if (cbTCP.isSelected()) {
			rtrn += separator + "TCP";
		}
		separator = ";";
		rtrn += separator + ServerTCP.getIP();
		rtrn += separator + port.getText();
		rtrn += separator + address.getText();

		return rtrn;
	}

	/**
	 * Refresh qr.
	 */
	private void refreshQR() {
		img = qrGenerator.encode(getDataForQR());
		if (QRCode.isVisible()) {
			QRCode.setVisible(false);
			QRWindow.this.pack();
			Dimension dimFrameWithoutQR = QRWindow.this.getSize();
			QRCode.setVisible(true);
			Dimension dimNewImg = new Dimension(img.getWidth(null) + 10, dimFrameWithoutQR.height + img.getHeight(null));
			if (dimNewImg.width < dimFrameWithoutQR.width)
				dimNewImg.width = dimFrameWithoutQR.width;
			QRWindow.this.setSize(dimNewImg);
			QRCode.repaint();
			QRWindow.this.repaint();
		}
	}

	/**
	 * Gets the refresh qr button.
	 * 
	 * @return the button
	 */
	private JButton getButtonQR() {
		if (showQR == null) {
			showQR = new JButton();
			showQR.setText(messages.getString("show_qr"));
			showQR.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					refreshQR();
					if (!QRCode.isVisible()) {
						QRCode.setVisible(true);
						showQR.setText(messages.getString("refresh_qr"));
						QRWindow.this.pack();
					}

				}
			});
		}
		return showQR;
	}

	/**
	 * Gets the address text field.
	 * 
	 * @return the text field
	 */
	private JTextField getTextAddress() {
		if (address == null) {
			address = new JTextField();
		}
		return address;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
