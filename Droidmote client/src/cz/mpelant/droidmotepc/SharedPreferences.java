package cz.mpelant.droidmotepc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SharedPreferences {
	public static final String DATA_TCP_LISTENER = "tcpListener";
	public static final String DATA_UDP_LISTENER = "udpListener";
	public static final String DATA_PORT = "port";
	public static final String DATA_UDP_ADDRESS = "UDPAddress";

	public static final boolean DEFAULT_TCP_LISTENER = true;
	public static final boolean DEFAULT_UDP_LISTENER = true;
	public static final int DEFAULT_PORT = 44522;
	public static final String DEFAULT_UDP_ADDRESS = "228.36.4.70";

	public static final String FILENAME = "settings.ini";

	private static Properties loadProperites() throws FileNotFoundException, IOException {
		Properties p = new Properties();
		File settings = new File(FILENAME);
		settings.createNewFile();
		p.load(new FileInputStream(settings));
		return p;
	}

	private static String getValue(String name) throws FileNotFoundException, IOException {
		Properties p = loadProperites();
		return p.getProperty(name);
	}

	public static int getInt(String name, int def) {
		try {
			String value = getValue(name);
			return Integer.parseInt(value);
		} catch (Exception e) {
			return def;
		}
	}

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

	public static String getString(String name) {
		return getString(name, "");
	}

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
