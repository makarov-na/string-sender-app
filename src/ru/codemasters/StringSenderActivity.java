package ru.codemasters;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
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
	EditText inputDataEdit;
	EditText outputDataEdit;
	SQLiteDatabase database;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
	LocalStore databaseHelper;
	ServerStore serverStore;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		saveLocalButton = (Button) findViewById(R.id.saveLocalButton);
		saveLocalButton.setOnClickListener(saveLocalButtonClickListener);
		sendToServerButton = (Button) findViewById(R.id.sendToServerButton);
		sendToServerButton.setOnClickListener(sendToServerButtonClickListener);
		databaseHelper = new LocalStore(this);
		serverStore = new ServerStore();
		updateLocalItemsCount();
	}

	private OnClickListener saveLocalButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			Message message = new Message(formatter.format(new Date()), ((EditText) findViewById(R.id.dataField))
					.getText().toString());
			databaseHelper.addMessageToLocalStore(message);
			updateLocalItemsCount();
			((EditText) findViewById(R.id.dataField)).setText("");
		}
	};

	private OnClickListener sendToServerButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				int status=serverStore.addMessagesToServerStore(databaseHelper.getServiceURLFromLocalStore(),databaseHelper.getAllMessagesFromLocalStore());
				((EditText) findViewById(R.id.testOutputDataField)).setText(status+"");
				databaseHelper.clearLocalStore();
			}
			catch (Exception exc){
				//TODO: Alert dialog need here
				String str="";
			}
			updateLocalItemsCount();
		}
	};

	private void updateLocalItemsCount() {
		((TextView) findViewById(R.id.localItemsCountLabel)).setText(getResources().getText(R.string.local_items_count)
				+ " " + databaseHelper.getAllMessagesFromLocalStore().size());
	}

	

	

}