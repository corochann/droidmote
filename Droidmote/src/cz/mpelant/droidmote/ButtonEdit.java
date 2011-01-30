package cz.mpelant.droidmote;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import cz.mpelant.droidmote.Keys.Key;

/**
 * Activity for editing the buttons
 */
public class ButtonEdit extends Activity {
	
	/** TAG for logcat */
	public static String TAG = "droidmote";
	
	/** The Constant for getting button Id from Intent extras */
	public static final String EXTRA_BUTTON_ID = "buttonId";
	
	/** The Constant for getting profile ID from Intent extras */
	public static final String EXTRA_PROFILE_ID = "profileId";
	
	/** The Constant for getting postition from Intent extras */
	public static final String EXTRA_POSITION = "position";
	
	/** Contains all the actions of the button */
	private ArrayList<LinearLayout> parts = new ArrayList<LinearLayout>();
	
	/** The button name. */
	private EditText buttonName;
	
	/** The plus minus button. */
	private LinearLayout plusMinus;
	
	/** The keys. */
	private Keys keys;
	
	/** The view. */
	private LinearLayout view;
	
	/** The button id. */
	private long buttonId;
	
	/** The profile id. */
	private long profileId;
	
	/** The position of the button. */
	private int position;

	/** The data provider. */
	private DataProvider data;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		buttonId = intent.getLongExtra(EXTRA_BUTTON_ID, -1);
		profileId = intent.getLongExtra(EXTRA_PROFILE_ID, -1);
		position = intent.getIntExtra(EXTRA_POSITION, -1);
		Log.d(TAG, "buttonId: " + buttonId);

		keys = new Keys();
		LinearLayout window = (LinearLayout) getLayoutInflater().inflate(R.layout.button_edit, null);
		view = (LinearLayout) window.findViewById(R.id.btLayout);
		setContentView(window);

		buttonName = (EditText) findViewById(R.id.btEditName);

		findViewById(R.id.btNameReset).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonName.setText(getName());

			}
		});
		Button buttonSave = (Button) window.findViewById(R.id.btSave);
		Button buttonCancel = (Button) window.findViewById(R.id.btCancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		buttonSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveInstance();
				setResult(RESULT_OK);
				finish();
			}
		});

		// load data--------------------------------------------------------
		data = new DataProvider(this);
		if (buttonId == -1) {
			addField();
		} else {
			data.open();
			Cursor button = data.fetchButton(buttonId);
			buttonName.setText(button.getString(button.getColumnIndex(DataProvider.BUTTONS_NAME)));
			Cursor actions = data.fetchAllActions(buttonId);
			startManagingCursor(actions);
			int colAction = actions.getColumnIndex(DataProvider.ACTIONS_ACTION);
			if (actions != null) {
				if (actions.moveToFirst()) {
					int i = 0;
					do {
						i++;
						view.removeView(plusMinus);
						addField(actions.getString(colAction));
					} while (actions.moveToNext());
				} else {
					addField();
				}
			}
			data.close();
		}
		// ------------------------------------------------------------------

	}

	/**
	 * Save instance.
	 */
	private void saveInstance() {
		int i = 0;
		data.open();

		String name;
		if (!buttonName.getText().toString().equals(""))
			name = buttonName.getText().toString();
		else
			name = getName();
		if (buttonId == -1) {
			buttonId = data.createButton(name, position, profileId);
		} else {
			data.updateButton(buttonId, name);
		}

		data.deleteActions(buttonId);
		for (LinearLayout part : parts) {
			String tmp = ((EditText) part.findViewById(R.id.btEditAction)).getText().toString();
			if (!tmp.equals("")) {
				i++;
				data.createAction(tmp, buttonId);
			}
		}
		data.close();
		Log.d(TAG, "saved button with buttonId " + buttonId);
	}

	/**
	 * Adds an epmty field.
	 */
	private void addField() {
		addField("");
	}

	/**
	 * Adds a field.
	 *
	 * @param action the concrete action code 
	 */
	private void addField(String action) {
		parts.add((LinearLayout) getLayoutInflater().inflate(R.layout.button_edit_action, null));
		plusMinus = (LinearLayout) getLayoutInflater().inflate(R.layout.button_edit_plus, null);
		view.addView(parts.get(parts.size() - 1));
		view.addView(plusMinus);
		Button buttonSelectAction = (Button) parts.get(parts.size() - 1).findViewById(R.id.btSelectAction);
		Button btPlus = (Button) plusMinus.findViewById(R.id.btPlus);
		Button btMinus = (Button) plusMinus.findViewById(R.id.btMinus);
		if (parts.size() < 2) {
			btMinus.setVisibility(View.GONE);
		}
		EditText editText = (EditText) parts.get(parts.size() - 1).findViewById(R.id.btEditAction);
		editText.setText(action);
		SelectAction selectAction = new SelectAction(editText);

		buttonSelectAction.setOnClickListener(selectAction);

		btPlus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				view.removeView(plusMinus);
				plusMinus.findViewById(R.id.btMinus).setVisibility(View.VISIBLE);
				addField();
			}
		});

		btMinus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (parts.size() <= 2) {
					plusMinus.findViewById(R.id.btMinus).setVisibility(View.GONE);
				}
				view.removeView(parts.get(parts.size() - 1));
				boolean isNameChanged = isNameChanged();
				parts.remove(parts.size() - 1);
				if (!isNameChanged)
					buttonName.setText(getName());
			}
		});
	}

	/**
	 * Gets the name of the button.
	 *
	 * @return the name
	 */
	private String getName() {
		String rtrn = "";
		String append = "";
		for (LinearLayout part : parts) {
			String tmp = ((EditText) part.findViewById(R.id.btEditAction)).getText().toString();

			if (!tmp.equals("")) {

				rtrn += append + keys.getName(tmp);
				append = " + ";
			}

		}

		return rtrn;
	}

	/**
	 * Checks if is name changed.
	 *
	 * @return true, if is name changed
	 */
	private boolean isNameChanged() {
		if (getName().equals(buttonName.getText().toString()) || buttonName.getText().toString().equals(""))
			return false;
		return true;
	}

	/**
	 * SelectAction listener
	 */
	class SelectAction implements android.view.View.OnClickListener {
		
		/** The EditText for displaying the action */
		EditText action;

		/** An array coniaining names for the Dialog */
		Key[] data;
		
		/** The options after the user clicks on Select action. */
		final String[] options = { getResources().getString(R.string.characters), getResources().getString(R.string.arrows), getResources().getString(R.string.special) };

		/**
		 * Instantiates a new select action listener.
		 *
		 * @param action the EditText for displaying the action 
		 */
		public SelectAction(EditText action) {
			this.action = action;
		}

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			Log.d(TAG, "onclick");
			AlertDialog.Builder selectKeyGroup = new AlertDialog.Builder(ButtonEdit.this);
			selectKeyGroup.setTitle(getResources().getString(R.string.btActionSelect) + " " + getResources().getString(R.string.btActionGroup).toLowerCase());
			final AlertDialog.Builder selectKey = new AlertDialog.Builder(ButtonEdit.this);

			selectKeyGroup.setItems(options, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					switch (which) {
					case 0:
						data = keys.getCharacters();
						selectKey.setTitle(getResources().getString(R.string.btActionSelect) + " " + getResources().getString(R.string.characters).toLowerCase());
						break;
					case 1:
						data = keys.getArrows();
						selectKey.setTitle(getResources().getString(R.string.btActionSelect) + " " + getResources().getString(R.string.arrows).toLowerCase());
						break;
					case 2:
						data = keys.getSpecial();
						selectKey.setTitle(getResources().getString(R.string.btActionSelect) + " " + getResources().getString(R.string.special).toLowerCase());
						break;
					}

					selectKey.setItems(Keys.getNames(data), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							boolean isNameChanged = isNameChanged();
							action.setText(data[which].getKeyEvent());
							if (!isNameChanged)
								buttonName.setText(getName());
						}
					});
					selectKey.show();

				}

			});
			selectKeyGroup.show();
		}

	}

	

}
