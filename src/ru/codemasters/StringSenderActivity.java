package ru.codemasters;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StringSenderActivity extends Activity {

	Button sendToServerButton;
	Button saveLocalButton;
	Button saveServerURLButton;
	EditText inputDataEdit;
	EditText outputDataEdit;
	SQLiteDatabase database;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
	LocalStore localStore;
	ServerStore serverStore;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		saveLocalButton = (Button) findViewById(R.id.saveLocalButton);
		saveLocalButton.setOnClickListener(saveLocalButtonClickListener);
		sendToServerButton = (Button) findViewById(R.id.sendToServerButton);
		sendToServerButton.setOnClickListener(sendToServerButtonClickListener);
		saveServerURLButton = (Button) findViewById(R.id.saveServerURLButton);
		saveServerURLButton.setOnClickListener(saveServerURLButtonClickListener);
		localStore = new LocalStore(this);
		serverStore = new ServerStore();
		updateLocalItemsCount();
	}

	private OnClickListener saveServerURLButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			String serverURL = ((EditText) findViewById(R.id.dataField)).getText().toString();
			localStore.saveServiceURLToLOcalStore(serverURL);
			((EditText) findViewById(R.id.dataField)).setText("");
		}
	};

	private OnClickListener saveLocalButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			Message message = new Message(formatter.format(new Date()), ((EditText) findViewById(R.id.dataField))
					.getText().toString());
			localStore.addMessageToLocalStore(message);
			updateLocalItemsCount();
			((EditText) findViewById(R.id.dataField)).setText("");
		}
	};

	private OnClickListener sendToServerButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				int status = serverStore.addMessagesToServerStore(localStore.getServiceURLFromLocalStore(),
						localStore.getAllMessagesFromLocalStore());
				((EditText) findViewById(R.id.testOutputDataField)).setText(status+"");
				localStore.clearLocalStore();
			} catch (Exception exc) {
				new AlertDialog.Builder(StringSenderActivity.this).setTitle("Error").setMessage(localStore.getServiceURLFromLocalStore()+" "+exc.getClass().getName()+" "+exc.getMessage())
						.setNeutralButton("Close", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dlg, int sumthin) {
							}
						}).show();
			}
			updateLocalItemsCount();
		}
	};

	private void updateLocalItemsCount() {
		((TextView) findViewById(R.id.localItemsCountLabel)).setText(getResources().getText(R.string.local_items_count)
				+ " " + localStore.getAllMessagesFromLocalStore().size());
	}

}