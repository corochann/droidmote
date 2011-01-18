package cz.mpelant.droidmote;

import android.view.KeyEvent;

public class Keys {
	private Key[] arrows;
	private Key[] characters;
	private Key[] special;
	class Key{
		private String keyEvent;
		private String name;
		
		public Key(String keyEvent, String name){
			this.name=name;
			this.keyEvent=keyEvent;
		}
		public String getName() {
			return name;
		}
		public String getKeyEvent() {
			return keyEvent;
		}
		@Override
		public String toString() {
			return name;
		}

	}
	public Keys(){
		int i=0;
		arrows=new Key[4];
		characters=new Key[100];
		special=new Key[100];
		
		
		
		
		characters[i]=new Key("VK_0", "0"); i++;
		characters[i]=new Key("VK_1", "1"); i++;
		characters[i]=new Key("VK_2", "2"); i++;
		characters[i]=new Key("VK_3", "3"); i++;
		characters[i]=new Key("VK_4", "4"); i++;
		characters[i]=new Key("VK_5", "5"); i++;
		characters[i]=new Key("VK_6", "6"); i++;
		characters[i]=new Key("VK_7", "7"); i++;
		characters[i]=new Key("VK_8", "8"); i++;
		characters[i]=new Key("VK_9", "9"); i++;
		characters[i]=new Key("VK_A", "a"); i++;
		characters[i]=new Key("VK_B", "b"); i++;
		characters[i]=new Key("VK_C", "c"); i++;
		characters[i]=new Key("VK_D", "d"); i++;
		characters[i]=new Key("VK_E", "e"); i++;
		characters[i]=new Key("VK_F", "f"); i++;
		characters[i]=new Key("VK_G", "g"); i++;
		characters[i]=new Key("VK_H", "h"); i++;
		characters[i]=new Key("VK_I", "i"); i++;
		characters[i]=new Key("VK_J", "j"); i++;
		characters[i]=new Key("VK_K", "k"); i++;
		characters[i]=new Key("VK_L", "l"); i++;
		characters[i]=new Key("VK_M", "m"); i++;
		characters[i]=new Key("VK_N", "n"); i++;
		characters[i]=new Key("VK_O", "o"); i++;
		characters[i]=new Key("VK_P", "p"); i++;
		characters[i]=new Key("VK_Q", "q"); i++;
		characters[i]=new Key("VK_R", "r"); i++;
		characters[i]=new Key("VK_S", "s"); i++;
		characters[i]=new Key("VK_T", "t"); i++;
		characters[i]=new Key("VK_U", "u"); i++;
		characters[i]=new Key("VK_V", "v"); i++;
		characters[i]=new Key("VK_W", "w"); i++;
		characters[i]=new Key("VK_X", "x"); i++;
		characters[i]=new Key("VK_Y", "y"); i++;
		characters[i]=new Key("VK_Z", "z"); i++;
		characters[i]=new Key("VK_SEMICOLON", ";"); i++;
		characters[i]=new Key("VK_EQUALS", "="); i++;
		characters[i]=new Key("VK_OPEN_BRACKET", "["); i++;
		characters[i]=new Key("VK_BACK_SLASH", "\\"); i++;
		characters[i]=new Key("VK_CLOSE_BRACKET", "]"); i++;
		characters[i]=new Key("VK_COMMA", ","); i++;
		characters[i]=new Key("VK_MINUS", "-"); i++;
		characters[i]=new Key("VK_PERIOD", "."); i++;
		characters[i]=new Key("VK_SLASH", "/"); i++;
		characters[i]=new Key("VK_AMPERSAND", "&"); i++;
		characters[i]=new Key("VK_COLON", ":"); i++;
		characters[i]=new Key("VK_AT", "@"); i++;
		i=0;
		arrows[i]=new Key("VK_LEFT", "Left"); i++;
		arrows[i]=new Key("VK_RIGHT", "Right"); i++;
		arrows[i]=new Key("VK_UP", "Up"); i++;
		arrows[i]=new Key("VK_DOWN", "Down"); i++;
		i=0;
		special[i]=new Key("VK_ALT", "Alt"); i++;
		special[i]=new Key("VK_ALT_GRAPH", "Alt Gr"); i++;
		special[i]=new Key("VK_BACK_SPACE", "Backspace"); i++;
		special[i]=new Key("VK_CAPS_LOCK", "Caps Lock"); i++;
		special[i]=new Key("VK_CONTROL", "Ctrl"); i++;
		special[i]=new Key("VK_COPY", "Copy"); i++;
		special[i]=new Key("VK_CUT", "Cut"); i++;
		special[i]=new Key("VK_DELETE", "Delete"); i++;
		special[i]=new Key("VK_END", "End"); i++;
		special[i]=new Key("VK_ENTER", "Enter"); i++;
		special[i]=new Key("VK_ESCAPE", "Esc"); i++;
		special[i]=new Key("VK_F1", "F1"); i++;
		special[i]=new Key("VK_F2", "F2"); i++;
		special[i]=new Key("VK_F3", "F3"); i++;
		special[i]=new Key("VK_F4", "F4"); i++;
		special[i]=new Key("VK_F5", "F5"); i++;
		special[i]=new Key("VK_F6", "F6"); i++;
		special[i]=new Key("VK_F7", "F7"); i++;
		special[i]=new Key("VK_F8", "F8"); i++;
		special[i]=new Key("VK_F9", "F9"); i++;
		special[i]=new Key("VK_F10", "F10"); i++;
		special[i]=new Key("VK_F11", "F11"); i++;
		special[i]=new Key("VK_F12", "F12"); i++;
		special[i]=new Key("VK_F13", "F13"); i++;
		special[i]=new Key("VK_F14", "F14"); i++;
		special[i]=new Key("VK_F15", "F15"); i++;
		special[i]=new Key("VK_F16", "F16"); i++;
		special[i]=new Key("VK_F17", "F17"); i++;
		special[i]=new Key("VK_F18", "F18"); i++;
		special[i]=new Key("VK_F19", "F19"); i++;
		special[i]=new Key("VK_F20", "F20"); i++;
		special[i]=new Key("VK_F21", "F21"); i++;
		special[i]=new Key("VK_F22", "F22"); i++;
		special[i]=new Key("VK_F23", "F23"); i++;
		special[i]=new Key("VK_F24", "F24"); i++;
		special[i]=new Key("VK_HOME", "Home"); i++;
		special[i]=new Key("VK_INSERT", "Insert"); i++;
		special[i]=new Key("VK_NUM_LOCK", "Num Lock"); i++;
		special[i]=new Key("VK_NUMPAD1", "Numpad 1"); i++;
		special[i]=new Key("VK_NUMPAD2", "Numpad 2"); i++;
		special[i]=new Key("VK_NUMPAD3", "Numpad 3"); i++;
		special[i]=new Key("VK_NUMPAD4", "Numpad 4"); i++;
		special[i]=new Key("VK_NUMPAD5", "Numpad 5"); i++;
		special[i]=new Key("VK_NUMPAD6", "Numpad 6"); i++;
		special[i]=new Key("VK_NUMPAD7", "Numpad 7"); i++;
		special[i]=new Key("VK_NUMPAD8", "Numpad 8"); i++;
		special[i]=new Key("VK_NUMPAD9", "Numpad 9"); i++;
		special[i]=new Key("VK_NUMPAD0", "Numpad 0"); i++;
		special[i]=new Key("VK_PAGE_DOWN", "Page Down"); i++;
		special[i]=new Key("VK_PAGE_UP", "Page Up"); i++;
		special[i]=new Key("VK_PASTE", "Paste"); i++;
		special[i]=new Key("VK_PRINTSCREEN", "Print Screen"); i++;
		special[i]=new Key("VK_SCROLL_LOCK", "Scroll Lock"); i++;
		special[i]=new Key("VK_SHIFT", "Shift"); i++;
		special[i]=new Key("VK_SPACE", "Space"); i++;
		special[i]=new Key("VK_TAB", "Tab"); i++;
		special[i]=new Key("VK_WINDOWS", "Win"); i++;
		
	}
	
	public Key[] getArrows(){
		return arrows;
	}
	public Key[] getCharacters(){
		return characters;
	}
	public Key[] getSpecial(){
		return special;
	}

	public static String[] getNames(Key[] keys) {
		int max;
		for (max = 1; max < keys.length; max++) {
			try {
				keys[max - 1].toString();
			} catch (NullPointerException e) {
				max -= 1;
				break;
			}
		}
		String[] rtrn = new String[max];
		for (int i = 0; i < max; i++) {
			rtrn[i] = keys[i].toString();
		}
		return rtrn;
	}
	public String getName(String keyEvent){
		for(int i=0; i<arrows.length; i++){
			try {
				if(arrows[i].getKeyEvent().equals(keyEvent)){
					return arrows[i].getName();
				}
			} catch (Exception e) {
				break;
			}
		}
		for(int i=0; i<characters.length; i++){
			try {
				if(characters[i].getKeyEvent().equals(keyEvent)){
					return characters[i].getName();
				}
			} catch (Exception e) {
				break;
			}
		}
		for(int i=0; i<special.length; i++){
			try {
				if(special[i].getKeyEvent().equals(keyEvent)){
					return special[i].getName();
				}
			} catch (Exception e) {
				break;
			}
		}
		
		return keyEvent;
	}
	

	public int getKeyEvent(String keyCode){
		try {
			return KeyEvent.class.getDeclaredField(keyCode).getInt(null);
		} catch (Exception e) {
			return -1;
		} 
	}
}
