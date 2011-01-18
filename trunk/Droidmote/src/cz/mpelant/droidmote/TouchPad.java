package cz.mpelant.droidmote;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TouchPad extends SuperActivity implements OnTouchListener {

	private LinearLayout touchpad;
	private Button btMouseLeft;
	private Button btMouseRight;
	private ImageButton btShowKeyboard;
	private Point startPoint;
	private long pressedTime1;
	private long releasedTime1;
	private long pressedTime2;
	private long releasedTime2;
	private int buttonPressed = 0;

	private InputMethodManager imgr;

	public static final int TIME_MOUSE_CLICK = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.touchpad);
		titlebar = findViewById(R.id.titlebar);
		startPoint = new Point();
		btMouseLeft = (Button) findViewById(R.id.mouseLeft);
		btMouseRight = (Button) findViewById(R.id.mouseRight);
		btShowKeyboard = (ImageButton) findViewById(R.id.showKeyboard);
		touchpad = (LinearLayout) findViewById(R.id.touchpad);
		touchpad.setOnTouchListener(this);
		imgr = (InputMethodManager) getSystemService(TouchPad.INPUT_METHOD_SERVICE);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		handleEvent(event);
		return true;
	}

	private boolean isInView(Point point, View view) {
		if (point.x > view.getLeft() && point.x < view.getRight() && point.y > view.getTop() && point.y < view.getBottom())
			return true;
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU)
			return super.onKeyDown(keyCode, event);
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.d(TAG, "onKeyDown " + keyCode);
			int utfCode = event.getUnicodeChar();
			Intent i = new Intent();
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
				i.setAction(ProfileEdit.ACTION);
				i.putExtra("type", Commands.COMMAND_MOUSE_PRESS);
				i.putExtra("value", Commands.VALUE_MOUSE_LEFT);
				sendBroadcast(i);
				return true;
			case KeyEvent.KEYCODE_DEL:
				i.setAction(ProfileEdit.ACTION);
				i.putExtra("type", Commands.COMMAND_KEY_EVENT_ID);
				i.putExtra("value", "VK_BACK_SPACE");
				sendBroadcast(i);
				return true;
			case KeyEvent.KEYCODE_ENTER:
				Log.d(TAG, "key Enter");
				i.setAction(ProfileEdit.ACTION);
				i.putExtra("type", Commands.COMMAND_KEY_EVENT_ID);
				i.putExtra("value", "VK_ENTER");
				sendBroadcast(i);
				return true;
			}

			if (utfCode != 0) {
				i.setAction(ProfileEdit.ACTION);
				i.putExtra("type", Commands.COMMAND_UTF8);
				i.putExtra("value", utfCode + "");
				Log.d(TAG, "key UTF8 - " + utfCode);
				sendBroadcast(i);
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			Log.d(TAG, "onKeyUp " + keyCode);
			if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
				Intent i = new Intent();
				i.setAction(ProfileEdit.ACTION);
				i.putExtra("type", Commands.COMMAND_MOUSE_RELEASE);
				i.putExtra("value", Commands.VALUE_MOUSE_LEFT);
				sendBroadcast(i);
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	private void handleEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			Log.d(TAG, "Left, left corner - [" + btMouseLeft.getLeft() + "," + btMouseLeft.getTop() + "]" + "[" + btMouseLeft.getRight() + "," + btMouseLeft.getBottom() + "]");
			if (isInView(new Point((int) event.getX(0), (int) event.getY(0)), btMouseLeft)) {
				buttonPressed = 1;
				btMouseLeft.setBackgroundResource(R.drawable.touchpad_button2);
				Intent i = new Intent();
				i.setAction(ProfileEdit.ACTION);
				i.putExtra("type", Commands.COMMAND_MOUSE_PRESS);
				i.putExtra("value", Commands.VALUE_MOUSE_LEFT);
				sendBroadcast(i);
				Log.d(TAG, "button1 Pressed");
				break;
			}
			if (isInView(new Point((int) event.getX(0), (int) event.getY(0)), btMouseRight)) {
				buttonPressed = 2;
				btMouseRight.setBackgroundResource(R.drawable.touchpad_button2);
				Intent i = new Intent();
				i.setAction(ProfileEdit.ACTION);
				i.putExtra("type", Commands.COMMAND_MOUSE_PRESS);
				i.putExtra("value", Commands.VALUE_MOUSE_RIGHT);
				sendBroadcast(i);
				Log.d(TAG, "button2 Pressed");
				break;
			}
			if (isInView(new Point((int) event.getX(0), (int) event.getY(0)), btShowKeyboard)) {
				buttonPressed = 3;
				Log.d(TAG, "button3 Pressed");
				imgr.toggleSoftInput(0, 0);
				break;
			}
			pressedTime1 = System.currentTimeMillis();
			startPoint.x = (int) event.getX(0);
			startPoint.y = (int) event.getY(0);

			break;
		case MotionEvent.ACTION_MOVE:
			int pointId = 0;
			if (buttonPressed > 0) {
				if (event.getPointerCount() > 1)
					pointId = 1;
				else
					break;
			}
			int x = (int) (event.getX(pointId) - startPoint.x);
			int y = (int) (event.getY(pointId) - startPoint.y);
			startPoint.set((int) event.getX(pointId), (int) event.getY(pointId));
			if (Math.abs(x) + Math.abs(y) / 2 > 100 && pointId > 0)
				break;
			Log.v(TAG, "zmena: x=" + x + ", y=" + y + ";     current position:" + event.getX(0) + ", " + event.getY(0));

			String value = x + ";" + y;
			Intent i = new Intent();
			i.setAction(ProfileEdit.ACTION);
			i.putExtra("type", Commands.COMMAND_MOUSE_MOVE);
			i.putExtra("value", value);
			sendBroadcast(i);
			break;
		case MotionEvent.ACTION_UP:
			btMouseRight.setBackgroundResource(R.drawable.touchpad_button);
			btMouseLeft.setBackgroundResource(R.drawable.touchpad_button);
			if (buttonPressed == 1) {
				Intent i2 = new Intent();
				i2.setAction(ProfileEdit.ACTION);
				i2.putExtra("type", Commands.COMMAND_MOUSE_RELEASE);
				i2.putExtra("value", Commands.VALUE_MOUSE_LEFT);
				sendBroadcast(i2);
				Log.d(TAG, "button1 released");
				buttonPressed = 0;
				break;
			}
			if (buttonPressed == 2) {
				Intent i2 = new Intent();
				i2.setAction(ProfileEdit.ACTION);
				i2.putExtra("type", Commands.COMMAND_MOUSE_RELEASE);
				i2.putExtra("value", Commands.VALUE_MOUSE_RIGHT);
				sendBroadcast(i2);
				Log.d(TAG, "button2 released");
				buttonPressed = 0;
				break;
			}
			if (buttonPressed >= 2) {
				buttonPressed = 0;
				break;
			}

			releasedTime1 = System.currentTimeMillis();
			if (releasedTime1 - pressedTime1 < TIME_MOUSE_CLICK) {
				Intent i2 = new Intent();
				i2.setAction(ProfileEdit.ACTION);
				i2.putExtra("type", Commands.COMMAND_MOUSE_CLICK);
				if (Math.abs(pressedTime1 - pressedTime2) < TIME_MOUSE_CLICK / 2 && Math.abs(releasedTime1 - releasedTime2) < TIME_MOUSE_CLICK / 2)
					i2.putExtra("value", Commands.VALUE_MOUSE_MIDDLE);
				else
					i2.putExtra("value", Commands.VALUE_MOUSE_LEFT);
				sendBroadcast(i2);
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			if (buttonPressed > 0) {
				startPoint.x = (int) event.getX(1);
				startPoint.y = (int) event.getY(1);
			}
			pressedTime2 = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			releasedTime2 = System.currentTimeMillis();

		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		menu.removeItem(R.id.editProfile);
		menu.removeItem(R.id.modeTouchpad);
		menu.removeItem(R.id.deleteProfile);
		return super.onCreateOptionsMenu(menu);
	}

}
