package cz.mpelant.droidmote;

import android.view.KeyEvent;

/**
 * The Class AndroidKeys.
 */
public class AndroidKeys {

	/**
	 * Convert android to pc key event code.
	 * 
	 * @param keyEvent the key event
	 * @return the key event code
	 */
	public static String convertToPCEvent(int keyEvent) {
		switch (keyEvent) {
		case KeyEvent.KEYCODE_0:
			return "VK_0";
		case KeyEvent.KEYCODE_1:
			return "VK_1";
		case KeyEvent.KEYCODE_2:
			return "VK_2";
		case KeyEvent.KEYCODE_3:
			return "VK_3";
		case KeyEvent.KEYCODE_4:
			return "VK_4";
		case KeyEvent.KEYCODE_5:
			return "VK_5";
		case KeyEvent.KEYCODE_6:
			return "VK_6";
		case KeyEvent.KEYCODE_7:
			return "VK_7";
		case KeyEvent.KEYCODE_8:
			return "VK_8";
		case KeyEvent.KEYCODE_9:
			return "VK_9";
		case KeyEvent.KEYCODE_A:
			return "VK_A";
		case KeyEvent.KEYCODE_B:
			return "VK_B";
		case KeyEvent.KEYCODE_C:
			return "VK_C";
		case KeyEvent.KEYCODE_D:
			return "VK_D";
		case KeyEvent.KEYCODE_E:
			return "VK_E";
		case KeyEvent.KEYCODE_F:
			return "VK_F";
		case KeyEvent.KEYCODE_G:
			return "VK_G";
		case KeyEvent.KEYCODE_H:
			return "VK_H";
		case KeyEvent.KEYCODE_I:
			return "VK_I";
		case KeyEvent.KEYCODE_J:
			return "VK_J";
		case KeyEvent.KEYCODE_K:
			return "VK_K";
		case KeyEvent.KEYCODE_L:
			return "VK_L";
		case KeyEvent.KEYCODE_M:
			return "VK_M";
		case KeyEvent.KEYCODE_N:
			return "VK_N";
		case KeyEvent.KEYCODE_O:
			return "VK_O";
		case KeyEvent.KEYCODE_P:
			return "VK_P";
		case KeyEvent.KEYCODE_Q:
			return "VK_Q";
		case KeyEvent.KEYCODE_R:
			return "VK_R";
		case KeyEvent.KEYCODE_S:
			return "VK_S";
		case KeyEvent.KEYCODE_T:
			return "VK_T";
		case KeyEvent.KEYCODE_U:
			return "VK_U";
		case KeyEvent.KEYCODE_V:
			return "VK_V";
		case KeyEvent.KEYCODE_W:
			return "VK_W";
		case KeyEvent.KEYCODE_X:
			return "VK_X";
		case KeyEvent.KEYCODE_Y:
			return "VK_Y";
		case KeyEvent.KEYCODE_Z:
			return "VK_Z";
		case KeyEvent.KEYCODE_SPACE:
			return "VK_SPACE";
		case KeyEvent.KEYCODE_PERIOD:
			return "VK_PERIOD";
		case KeyEvent.KEYCODE_SEMICOLON:
			return "VK_SEMICOLON";
		case KeyEvent.KEYCODE_EQUALS:
			return "VK_EQUALS";
		case KeyEvent.KEYCODE_BACKSLASH:
			return "VK_BACK_SLASH";
		case KeyEvent.KEYCODE_COMMA:
			return "VK_COMMA";
		case KeyEvent.KEYCODE_MINUS:
			return "VK_MINUS";
		case KeyEvent.KEYCODE_SLASH:
			return "VK_SLASH";
		case KeyEvent.KEYCODE_AT:
			return "VK_AT";

		}
		return null;
	}
}
