package ru.codemasters;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Xml;
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
	SimpleDateFormat formatter=new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		saveLocalButton = (Button) findViewById(R.id.saveLocalButton);
		saveLocalButton.setOnClickListener(saveLocalButtonClickListener);
		sendToServerButton = (Button) findViewById(R.id.sendToServerButton);
		sendToServerButton.setOnClickListener(sendToServerButtonClickListener);
		database = (new DatabaseHelper(this)).getWritableDatabase();
		updateLocalItemsCount();
	}

	private OnClickListener saveLocalButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			ContentValues cv = new ContentValues();
			cv.put(DatabaseHelper.DATE, formatter.format(new Date()));
			cv.put(DatabaseHelper.TEXT, ((EditText) findViewById(R.id.dataField)).getText().toString());
			database.insert(DatabaseHelper.TABLE_NAME, DatabaseHelper.TEXT, cv);
			updateLocalItemsCount();
			((EditText) findViewById(R.id.dataField)).setText("");
		}
	};

	private OnClickListener sendToServerButtonClickListener = new OnClickListener() {
		public void onClick(View v) {
			String[] columns = { DatabaseHelper.TEXT, DatabaseHelper.DATE };
			Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
			List<Map<String, String>> messages = new ArrayList<Map<String, String>>();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String text = cursor.getString(0);
				String date = cursor.getString(1);
				Map<String, String> row = new HashMap<String, String>();
				row.put(DatabaseHelper.TEXT, text);
				row.put(DatabaseHelper.DATE, date);
				messages.add(row);
				cursor.moveToNext();
			}
			cursor.close();
			((EditText) findViewById(R.id.testOutputDataField)).setText(writeXml(messages));
			database.delete(DatabaseHelper.TABLE_NAME, null, null);
			updateLocalItemsCount();
		}
	};

	private void updateLocalItemsCount() {
		String[] columns = { DatabaseHelper.TEXT, DatabaseHelper.DATE };
		Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
		cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
		((TextView) findViewById(R.id.localItemsCountLabel)).setText(getResources().getText(R.string.local_items_count)
				+ " " + cursor.getCount());
	}

	private String writeXml(List<Map<String, String>> messages) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "messages");
			serializer.attribute("", "number", String.valueOf(messages.size()));
			for (Map<String, String> msg : messages) {
				serializer.startTag("", "message");
				serializer.attribute("", "date", msg.get(DatabaseHelper.DATE));
				serializer.startTag("", "text");
				serializer.text(msg.get(DatabaseHelper.TEXT));
				serializer.endTag("", "text");
				serializer.endTag("", "message");
			}
			serializer.endTag("", "messages");
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}