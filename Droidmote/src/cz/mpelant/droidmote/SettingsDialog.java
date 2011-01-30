package cz.mpelant.droidmote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * The config dialog
 */
public class SettingsDialog {

	/** The dialog. */
	public Dialog dialog;

	/** The TCP IP edittext. */
	private EditText textIP;

	/** The UDP group edittext. */
	private EditText textGroup;

	/** The protocol spinner. */
	private Spinner spProtocol;

	/** The port edittext. */
	private EditText port;

	/** The context. */
	private Activity mContext;

	/**
	 * Instantiates a new settings dialog.
	 * 
	 * @param mContext
	 *            the context
	 */
	public SettingsDialog(Activity mContext) {
		this.mContext = mContext;
	}

	/**
	 * Creates the dialog.
	 * 
	 * @return the dialog
	 */
	public Dialog createDialog() {
		ArrayAdapter<String> items = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, new String[] { mContext.getResources().getString(R.string.udp), mContext.getResources().getString(R.string.tcp) });
		items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.settings_popup);

		spProtocol = (Spinner) dialog.findViewById(R.id.spProtocol);
		spProtocol.setAdapter(items);
		spProtocol.setSelection(PreferenceManager.getDefaultSharedPreferences(mContext).getInt(SuperActivity.DATA_PROTOCOL, SuperActivity.DEFAULT_PROTOCOL));
		spProtocol.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position == SuperActivity.PROTOCOL_TCP) {
					dialog.findViewById(R.id.tvIP).setVisibility(View.VISIBLE);
					textIP.setVisibility(View.VISIBLE);

					dialog.findViewById(R.id.tvGroup).setVisibility(View.GONE);
					textGroup.setVisibility(View.GONE);
				} else {
					dialog.findViewById(R.id.tvIP).setVisibility(View.GONE);
					textIP.setVisibility(View.GONE);

					dialog.findViewById(R.id.tvGroup).setVisibility(View.VISIBLE);
					textGroup.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		textIP = (EditText) dialog.findViewById(R.id.edIp);
		textIP.setText(PreferenceManager.getDefaultSharedPreferences(mContext).getString(SuperActivity.DATA_IP, SuperActivity.DEFAULT_IP));

		textGroup = (EditText) dialog.findViewById(R.id.edGroup);
		textGroup.setText(PreferenceManager.getDefaultSharedPreferences(mContext).getString(SuperActivity.DATA_UDP_ADDRESS, SuperActivity.DEFAULT_UDP_ADDRESS));

		port = (EditText) dialog.findViewById(R.id.edPort);
		port.setText(PreferenceManager.getDefaultSharedPreferences(mContext).getInt(SuperActivity.DATA_PORT, SuperActivity.DEFAULT_PORT) + "");

		Button save = (Button) dialog.findViewById(R.id.ok);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int portInt = SuperActivity.DEFAULT_PORT;
				try {
					portInt = Integer.parseInt(port.getText().toString());
				} catch (Exception e) {
					Toast.makeText(mContext, R.string.invalidPort, Toast.LENGTH_SHORT).show();
					return;
				}
				if (portInt > 65535) {
					Toast.makeText(mContext, R.string.invalidPort, Toast.LENGTH_SHORT).show();
					return;
				}
				Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
				editor.putString(SuperActivity.DATA_IP, textIP.getText().toString());
				editor.putString(SuperActivity.DATA_UDP_ADDRESS, textGroup.getText().toString());
				editor.putInt(SuperActivity.DATA_PORT, portInt);
				editor.putInt(SuperActivity.DATA_PROTOCOL, (int) spProtocol.getSelectedItemId());

				editor.commit();
				mContext.removeDialog(SuperActivity.DIALOG_SETTINGS);

			}
		});

		Button scanQR = (Button) dialog.findViewById(R.id.btScanQR);
		scanQR.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				try {
					mContext.startActivityForResult(intent, SuperActivity.DECODE_QR_REQ_CODE);
				} catch (ActivityNotFoundException e) {
					AlertDialog.Builder d = new AlertDialog.Builder(mContext);
					d.setTitle(R.string.qr_not_found_title);
					d.setMessage(R.string.qr_not_found_message);
					d.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:com.google.zxing.client.android"));
							mContext.startActivity(intent);
						}
					});
					d.setNegativeButton(android.R.string.no, null);
					d.show();
				}

			}
		});
		Button cancel = (Button) dialog.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mContext.removeDialog(SuperActivity.DIALOG_SETTINGS);
			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				mContext.removeDialog(SuperActivity.DIALOG_SETTINGS);

			}
		});

		return dialog;
	}

	/**
	 * Process the result from QR scanner.
	 * 
	 * @param data
	 *            the result intent
	 */
	public void QRActivityResult(Intent data) {
		String contents = data.getStringExtra("SCAN_RESULT");
		String[] parts = contents.split(";");

		if (parts[0].contains("TCP")) {
			spProtocol.setSelection(SuperActivity.PROTOCOL_TCP);
		} else if (parts[0].contains("UDP")) {
			spProtocol.setSelection(SuperActivity.PROTOCOL_UDP);
		}
		if (textIP != null) {
			textIP.setText(parts[1]);
		}
		if (port != null) {
			port.setText(parts[2]);
		}

		if (textGroup != null) {
			textGroup.setText(parts[3]);
		}
	}

}
