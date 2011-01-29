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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class QRWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel settings = null;
	private QRPanel QRCode = null; // @jve:decl-index=0:
	private Image img; // @jve:decl-index=0:
	private JTextField port = null;
	private JTextField address = null;
	private JButton showQR = null;
	private JButton save = null;
	private QRGenerator qrGenerator;
	private JCheckBox jrbUDP;
	private JCheckBox jrbTCP;
	private MainWindow mainWindow;

	class QRPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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
	 * This is the default constructor
	 */
	public QRWindow(MainWindow mainWindow) {
		super();
		initialize();
		this.mainWindow=mainWindow;

	}

	/**
	 * This method initializes this
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
	 * This method initializes jContentPane
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
			settings.add(new JLabel("Port:"), gBC);

			gBC.gridx = 1;
			gBC.ipadx = 50;
			settings.add(getTextPort(), gBC);

			gBC.ipadx = 5;
			gBC.gridx = 2;
			gBC.insets = new Insets(0, 10, 0, 0);
			settings.add(new JLabel("UDP address:"), gBC);

			gBC.gridx = 3;
			gBC.ipadx = 150;
			settings.add(getTextAddress(), gBC);

			gBC.ipadx = 5;
			gBC.gridx = 0;
			gBC.gridy = 1;
			settings.add(new JLabel("Accept protocols:"), gBC);

			gBC.gridx = 1;
			settings.add(jrbTCP, gBC);

			gBC.gridx = 2;
			settings.add(jrbUDP, gBC);

			gBC.gridx = 3;
			settings.add(new JLabel("Your current IP is: " + ServerTCP.getIP()), gBC);

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

	private void setProtocolBoxes() {
		jrbUDP = new JCheckBox("UDP");

		jrbUDP.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					address.setEnabled(true);
				} else {
					address.setEnabled(false);
				}

			}
		});

		jrbTCP = new JCheckBox("TCP");

	}

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
	 * This method initializes port
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTextPort() {
		if (port == null) {
			port = new JTextField();
		}
		return port;
	}

	private void showAlertDialog(String text) {
		JOptionPane.showMessageDialog(this, text);
	}

	private void loadSettings() {
		if (port != null)
			port.setText(SharedPreferences.getInt(SharedPreferences.DATA_PORT, SharedPreferences.DEFAULT_PORT) + "");
		if (address != null)
			address.setText(SharedPreferences.getString(SharedPreferences.DATA_UDP_ADDRESS, SharedPreferences.DEFAULT_UDP_ADDRESS));
		if (jrbTCP != null)
			jrbTCP.setSelected(SharedPreferences.getBoolean(SharedPreferences.DATA_TCP_LISTENER, SharedPreferences.DEFAULT_TCP_LISTENER));
		if (jrbUDP != null)
			jrbUDP.setSelected(SharedPreferences.getBoolean(SharedPreferences.DATA_UDP_LISTENER, SharedPreferences.DEFAULT_UDP_LISTENER));
	}

	private void saveSettings() {
		int portInt;
		try {
			portInt = Integer.parseInt(port.getText().toString());
		} catch (Exception e1) {
			showAlertDialog("Zadali jste špatně port. Musí být v rozmezí 0-65535");
			return;
		}
		if (portInt > 65535) {
			showAlertDialog("Zadali jste špatně port. Musí být v rozmezí 0-65535");
			return;
		}
		SharedPreferences.putInt(SharedPreferences.DATA_PORT, Integer.parseInt(port.getText()));
		SharedPreferences.putString(SharedPreferences.DATA_UDP_ADDRESS, address.getText());
		SharedPreferences.putBoolean(SharedPreferences.DATA_TCP_LISTENER, jrbTCP.isSelected());
		SharedPreferences.putBoolean(SharedPreferences.DATA_UDP_LISTENER, jrbUDP.isSelected());

	}

	private JButton getButtonSave() {
		if (save == null) {
			save = new JButton();
			save.setText("Ulozit");
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

	private String getDataForQR() {
		String rtrn = "";
		String separator = "";
		if (jrbUDP.isSelected()) {
			rtrn = "UDP";
			separator = "|";
		}
		if (jrbTCP.isSelected()) {
			rtrn += separator + "TCP";
		}
		separator = ";";
		rtrn += separator + ServerTCP.getIP();
		rtrn += separator + port.getText();
		rtrn += separator + address.getText();

		return rtrn;
	}

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

	private JButton getButtonQR() {
		if (showQR == null) {
			showQR = new JButton();
			showQR.setText("Zobrazit QR");
			showQR.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					refreshQR();
					if (!QRCode.isVisible()) {
						QRCode.setVisible(true);
						showQR.setText("Refreshnout QR");
						QRWindow.this.pack();
					}

				}
			});
		}
		return showQR;
	}

	private JTextField getTextAddress() {
		if (address == null) {
			address = new JTextField();
		}
		return address;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
