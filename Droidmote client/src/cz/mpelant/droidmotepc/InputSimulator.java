package cz.mpelant.droidmotepc;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class InputSimulator {
	public static void pressKey(String input, Keys keys, MainWindow gui) {
		String[] parts = input.split(":");
		if (parts[0].equals(Commands.COMMAND_CHAR))
			sendCommand(keys.getKeyEvent(parts[1].charAt(0)), gui);
		else if (parts[0].equals(Commands.COMMAND_UTF8))
			sendUTF8(parts[1]);
		else if (parts[0].equals(Commands.COMMAND_KEY_EVENT_ID))
			sendCommand(keys.getKeyEvent(parts[1]), gui);
		else if (parts[0].equals(Commands.COMMAND_STRING)) {
			for (int i = 0; i < parts[1].length(); i++) {
				sendCommand(keys.getKeyEvent(parts[1].charAt(i)), gui);
			}
		} else if (parts[0].equals(Commands.COMMAND_MULTICHAR)) {
			int[] keyEvents = new int[parts[1].length()];
			for (int i = 0; i < parts[1].length(); i++) {
				keyEvents[i] = keys.getKeyEvent(parts[1].charAt(i));
			}
			sendCommand(keyEvents, gui);
		} else if (parts[0].equals(Commands.COMMAND_MULTI_KEY_EVENT_ID)) {
			String[] keyEventsString = parts[1].split(";");
			int[] keyEvents = new int[keyEventsString.length];
			for (int i = 0; i < keyEventsString.length; i++) {
				keyEvents[i] = keys.getKeyEvent(keyEventsString[i]);
			}
			sendCommand(keyEvents, gui);

		} else if (parts[0].equals(Commands.COMMAND_MOUSE_MOVE)) {
			String[] coordinates = parts[1].split(";");
			moveMouse(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
		} else if (parts[0].equals(Commands.COMMAND_MOUSE_PRESS)) {
			mousePress(parts[1]);
		} else if (parts[0].equals(Commands.COMMAND_MOUSE_RELEASE)) {
			mouseRelease(parts[1]);
		} else if (parts[0].equals(Commands.COMMAND_MOUSE_CLICK)) {
			mousePress(parts[1]);
			mouseRelease(parts[1]);
		}else {
			gui.println("Unknown command " + input);
		}

	}

	private static void mousePress(String value) {

		int mouseButton = getMouseButtonID(value);
		if (mouseButton == -1)
			return;
		try {
			Robot robot = new Robot();
			robot.mousePress(mouseButton);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void mouseRelease(String value) {
		int mouseButton = getMouseButtonID(value);
		if (mouseButton == -1)
			return;
		try {
			Robot robot = new Robot();
			robot.mouseRelease(mouseButton);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static int getMouseButtonID(String value) {
		if (value.equals(Commands.VALUE_MOUSE_LEFT))
			return InputEvent.BUTTON1_MASK;
		if (value.equals(Commands.VALUE_MOUSE_RIGHT))
			return InputEvent.BUTTON3_MASK;
		if (value.equals(Commands.VALUE_MOUSE_MIDDLE))
			return InputEvent.BUTTON2_MASK;
		else
			return -1;
	}

	private static void moveMouse(int x, int y) {
		try {
			Robot robot = new Robot();
			int nX = MouseInfo.getPointerInfo().getLocation().x; // Get location
			int nY = MouseInfo.getPointerInfo().getLocation().y; // Get location
			robot.mouseMove(nX + x, nY + y);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void sendUTF8(String keycode) {
		Robot robot = null;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_ALT);

			for (int i = 0; i < keycode.length(); i++) {
				robot.keyPress(KeyEvent.class.getDeclaredField("VK_NUMPAD" + keycode.charAt(i)).getInt(null));
				robot.keyRelease(KeyEvent.class.getDeclaredField("VK_NUMPAD" + keycode.charAt(i)).getInt(null));

			}
		} catch (Exception e) {
		}
		robot.keyRelease(KeyEvent.VK_ALT);

	}

	private static void sendCommand(int keyEvent, MainWindow gui) {
		try {
			Robot robot = new Robot();
			gui.println("pressing " + KeyEvent.getKeyText(keyEvent) + " (" + keyEvent + ")");
			robot.keyPress(keyEvent);
			robot.keyRelease(keyEvent);
			gui.println("releasing " + KeyEvent.getKeyText(keyEvent) + " (" + keyEvent + ")");
		} catch (Exception e) {
			gui.println("unrecognized KeyEvent");
		}

	}

	private static void sendCommand(int[] keyEvents, MainWindow gui) {
		try {
			Robot robot = new Robot();
			for (int keyEvent : keyEvents) {
				gui.println("pressing " + KeyEvent.getKeyText(keyEvent) + " (" + keyEvent + ")");
				robot.keyPress(keyEvent);
			}
		} catch (Exception e) {
			gui.println("unrecognized KeyEvent");
		} finally {
			try {
				Robot robot = new Robot();
				for (int keyEvent : keyEvents) {
					gui.println("releasing " + KeyEvent.getKeyText(keyEvent) + " (" + keyEvent + ")");
					robot.keyRelease(keyEvent);
				}
			} catch (Exception e) {
				gui.println("unrecognized KeyEvent");
			}
		}
	}

}
