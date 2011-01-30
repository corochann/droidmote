package cz.mpelant.droidmotepc;

import java.awt.event.KeyEvent;

/**
 * The Class Keys.
 */
public class Keys {

	/** The keys. */
	private Key[] keys;

	/**
	 * The Class Key.
	 */
	class Key {

		/** The key event. */
		private int keyEvent;

		/** The key character. */
		private char key;

		/**
		 * Instantiates a new key.
		 * 
		 * @param keyEvent the key event
		 * @param key the key
		 */
		public Key(int keyEvent, char key) {
			this.key = key;
			this.keyEvent = keyEvent;
		}

		/**
		 * Gets the key.
		 * 
		 * @return the key
		 */
		public char getKey() {
			return key;
		}

		/**
		 * Gets the key event.
		 * 
		 * @return the key event
		 */
		public int getKeyEvent() {
			return keyEvent;
		}

	}

	/**
	 * Instantiates a new keys.
	 */
	public Keys() {
		int i = 0;
		keys = new Key[50];
		keys[i] = new Key(KeyEvent.VK_0, '0');
		i++;
		keys[i] = new Key(KeyEvent.VK_1, '1');
		i++;
		keys[i] = new Key(KeyEvent.VK_2, '2');
		i++;
		keys[i] = new Key(KeyEvent.VK_3, '3');
		i++;
		keys[i] = new Key(KeyEvent.VK_4, '4');
		i++;
		keys[i] = new Key(KeyEvent.VK_5, '5');
		i++;
		keys[i] = new Key(KeyEvent.VK_6, '6');
		i++;
		keys[i] = new Key(KeyEvent.VK_7, '7');
		i++;
		keys[i] = new Key(KeyEvent.VK_8, '8');
		i++;
		keys[i] = new Key(KeyEvent.VK_9, '9');
		i++;
		keys[i] = new Key(KeyEvent.VK_A, 'a');
		i++;
		keys[i] = new Key(KeyEvent.VK_B, 'b');
		i++;
		keys[i] = new Key(KeyEvent.VK_C, 'c');
		i++;
		keys[i] = new Key(KeyEvent.VK_D, 'd');
		i++;
		keys[i] = new Key(KeyEvent.VK_E, 'e');
		i++;
		keys[i] = new Key(KeyEvent.VK_F, 'f');
		i++;
		keys[i] = new Key(KeyEvent.VK_G, 'g');
		i++;
		keys[i] = new Key(KeyEvent.VK_H, 'h');
		i++;
		keys[i] = new Key(KeyEvent.VK_I, 'i');
		i++;
		keys[i] = new Key(KeyEvent.VK_J, 'j');
		i++;
		keys[i] = new Key(KeyEvent.VK_K, 'k');
		i++;
		keys[i] = new Key(KeyEvent.VK_L, 'l');
		i++;
		keys[i] = new Key(KeyEvent.VK_M, 'm');
		i++;
		keys[i] = new Key(KeyEvent.VK_N, 'n');
		i++;
		keys[i] = new Key(KeyEvent.VK_O, 'o');
		i++;
		keys[i] = new Key(KeyEvent.VK_P, 'p');
		i++;
		keys[i] = new Key(KeyEvent.VK_Q, 'q');
		i++;
		keys[i] = new Key(KeyEvent.VK_R, 'r');
		i++;
		keys[i] = new Key(KeyEvent.VK_S, 's');
		i++;
		keys[i] = new Key(KeyEvent.VK_T, 't');
		i++;
		keys[i] = new Key(KeyEvent.VK_U, 'u');
		i++;
		keys[i] = new Key(KeyEvent.VK_V, 'v');
		i++;
		keys[i] = new Key(KeyEvent.VK_W, 'w');
		i++;
		keys[i] = new Key(KeyEvent.VK_X, 'x');
		i++;
		keys[i] = new Key(KeyEvent.VK_Y, 'y');
		i++;
		keys[i] = new Key(KeyEvent.VK_Z, 'z');
		i++;
		keys[i] = new Key(KeyEvent.VK_SEMICOLON, ';');
		i++;
		keys[i] = new Key(KeyEvent.VK_EQUALS, '=');
		i++;
		keys[i] = new Key(KeyEvent.VK_OPEN_BRACKET, '[');
		i++;
		keys[i] = new Key(KeyEvent.VK_BACK_SLASH, '\\');
		i++;
		keys[i] = new Key(KeyEvent.VK_CLOSE_BRACKET, ']');
		i++;
		keys[i] = new Key(KeyEvent.VK_COMMA, ',');
		i++;
		keys[i] = new Key(KeyEvent.VK_MINUS, '-');
		i++;
		keys[i] = new Key(KeyEvent.VK_PERIOD, '.');
		i++;
		keys[i] = new Key(KeyEvent.VK_SLASH, '/');
		i++;
		keys[i] = new Key(KeyEvent.VK_SPACE, ' ');
		i++;
		keys[i] = new Key(KeyEvent.VK_AT, '@');
		i++;
	}

	/**
	 * Gets the key event.
	 * 
	 * @param key the char
	 * @return the key event
	 */
	public int getKeyEvent(char key) {
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].getKey() == key)
				return keys[i].getKeyEvent();
		}
		return -1;
	}

	/**
	 * Gets the key event.
	 * 
	 * @param keyCode the key code String
	 * @return the key event
	 */
	public int getKeyEvent(String keyCode) {
		try {
			return KeyEvent.class.getDeclaredField(keyCode).getInt(null);
		} catch (Exception e) {
			return -1;
		}
	}
}
