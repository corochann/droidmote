package cz.mpelant.droidmotepc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class is used to store settings in an .ini file
 */
public class SharedPreferences {

	/** The Constant DATA_TCP_LISTENER for storing boolean whether TCP listener is used. */
	public static final String DATA_TCP_LISTENER = "tcpListener";

	/** The Constant DATA_UDP_LISTENER for storing boolean whether UDP listener is used. */
	public static final String DATA_UDP_LISTENER = "udpListener";

	/** The Constant DATA_PORT for storing the port. */
	public static final String DATA_PORT = "port";

	/** The Constant DATA_UDP_ADDRESS for storing the UDP group/address. */
	public static final String DATA_UDP_ADDRESS = "UDPAddress";

	/** The Constant DEFAULT_TCP_LISTENER. */
	public static final boolean DEFAULT_TCP_LISTENER = true;

	/** The Constant DEFAULT_UDP_LISTENER. */
	public static final boolean DEFAULT_UDP_LISTENER = true;

	/** The Constant DEFAULT_PORT. */
	public static final int DEFAULT_PORT = 44522;

	/** The Constant DEFAULT_UDP_ADDRESS. */
	public static final String DEFAULT_UDP_ADDRESS = "228.36.4.70";

	/** The Constant name of the settings file. */
	public static final String FILENAME = "settings.ini";

	/**
	 * Load properites.
	 * 
	 * @return the properties
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static Properties loadProperites() throws FileNotFoundException, IOException {
		Properties p = new Properties();
		File settings = new File(FILENAME);
		settings.createNewFile();
		p.load(new FileInputStream(settings));
		return p;
	}

	/**
	 * Gets the value.
	 * 
	 * @param name the name
	 * @return the value
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static String getValue(String name) throws FileNotFoundException, IOException {
		Properties p = loadProperites();
		return p.getProperty(name);
	}

	/**
	 * Gets the int.
	 * 
	 * @param name the name
	 * @param def the default value
	 * @return the int
	 */
	public static int getInt(String name, int def) {
		try {
			String value = getValue(name);
			return Integer.parseInt(value);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * Gets the string.
	 * 
	 * @param name the name
	 * @param def the default value
	 * @return the string
	 */
	public static String getString(String name, String def) {
		try {
			String string = getValue(name);
			if (string.equals(""))
				return def;
			return string;
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * Gets the string.
	 * 
	 * @param name the name
	 * @return the string or "" if not found
	 */
	public static String getString(String name) {
		return getString(name, "");
	}

	/**
	 * Gets the boolean.
	 * 
	 * @param name the name
	 * @param def the default value
	 * @return the boolean
	 */
	public static boolean getBoolean(String name, boolean def) {
		try {
			String value = getValue(name);
			if (value.contains("true")) {
				return true;
			}
			if (value.contains("false"))
				return false;
			return def;
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * Put int into the settings file.
	 * 
	 * @param name the name
	 * @param value the value
	 * @return true, if successful
	 */
	public static boolean putInt(String name, int value) {
		try {
			Properties p = loadProperites();
			p.put(name, value + "");
			FileOutputStream out = new FileOutputStream(FILENAME);
			p.store(out, "/* properties updated */");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Put string into the settings file.
	 * 
	 * @param name the name
	 * @param value the value
	 * @return true, if successful
	 */
	public static boolean putString(String name, String value) {
		try {
			Properties p = loadProperites();
			p.put(name, value);
			FileOutputStream out = new FileOutputStream(FILENAME);
			p.store(out, "/* properties updated */");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Put boolean into the settings file.
	 * 
	 * @param name the name
	 * @param value the value
	 * @return true, if successful
	 */
	public static boolean putBoolean(String name, boolean value) {
		try {
			Properties p = loadProperites();
			if (value == true)
				p.put(name, "true");
			else
				p.put(name, "false");
			FileOutputStream out = new FileOutputStream(FILENAME);
			p.store(out, "/* properties updated */");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
