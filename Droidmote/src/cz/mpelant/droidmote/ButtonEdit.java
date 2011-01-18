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

public class ButtonEdit extends Activity {
	public static String TAG = "droidmote";
	public static final int NUMBER_OF_PARALELACTIONS = 4;
	public static final String EXTRA_BUTTON_ID = "buttonId";
	public static final String EXTRA_PROFILE_ID = "profileId";
	public static final String EXTRA_POSITION = "position";
	private ArrayList<LinearLayout> parts = new ArrayList<LinearLayout>();
	private EditText buttonName;
	private LinearLayout plusMinus;
	private Keys keys;
	private LinearLayout view;
	private long buttonId;
	private long profileId;
	private int position;

	private DataProvider data;

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

	private void addField() {
		addField("");
	}

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
		SelectAction selectAction = new SelectAction(editText, buttonName);

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

	private boolean isNameChanged() {
		if (getName().equals(buttonName.getText().toString()) || buttonName.getText().toString().equals(""))
			return false;
		return true;
	}

	class SelectAction implements android.view.View.OnClickListener {
		EditText action;

		Key[] data;
		final String[] options = { getResources().getString(R.string.characters), getResources().getString(R.string.arrows), getResources().getString(R.string.special) };

		public SelectAction(EditText action, EditText name) {
			this.action = action;
		}

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
