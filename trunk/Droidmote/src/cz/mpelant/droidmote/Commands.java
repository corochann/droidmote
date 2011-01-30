package cz.mpelant.droidmote;

/**
 * Commands strings
 */
public class Commands {
	
	/** The Constant for sending a single character */
	public static final String COMMAND_CHAR="droidmoteChar";
	
	/** The Constant for sending a single character as an UTF8 integer */
	public static final String COMMAND_UTF8="droidmoteUTF8";
	
	/** The Constant for sending actual KeyEvent ID */
	public static final String COMMAND_KEY_EVENT_ID="droidmoteKeyID";
	
	/** The Constant for sending the whole string */
	public static final String COMMAND_STRING="droidmoteString";
	
	/** The Constant for sending multiple characters at the same time*/
	public static final String COMMAND_MULTICHAR="droidmoteMultiChar";
	
	/** The Constant for sending multiple KeyEvent IDs at the same time (for example key shortcuts) */
	public static final String COMMAND_MULTI_KEY_EVENT_ID="droidmoteMultiKeyID";
	
	/** The Constant for sending Mouse move actions  */
	public static final String COMMAND_MOUSE_MOVE="droidmoteMouseMove";
	
	/** The Constant for sending Mouse press actions  */
	public static final String COMMAND_MOUSE_PRESS="droidmoteMousePress";
	
	/** The Constant for sending Mouse release actions  */
	public static final String COMMAND_MOUSE_RELEASE="droidmoteMouseRelease";
	
	/** The Constant for sending Mouse click actions  */
	public static final String COMMAND_MOUSE_CLICK="droidmoteMouseClick";
	
	/** The value of left mouse button. */
	public static final String VALUE_MOUSE_LEFT="left";
	
	/** The value of middle mouse button. */
	public static final String VALUE_MOUSE_MIDDLE="middle";
	
	/** The value of right mouse button. */
	public static final String VALUE_MOUSE_RIGHT="right";
}
