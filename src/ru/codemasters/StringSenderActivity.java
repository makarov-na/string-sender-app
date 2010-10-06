package ru.codemasters;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
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
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
	DatabaseHelper databaseHelper;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		saveLocalButton = (Button) findViewById(R.id.saveLocalButton);
		saveLocalButton.setOnClickListener(saveLocalButtonClickListener);
		sendToServerButton = (Button) findViewById(R.id.sendToServerButton);
		sendToServerButton.setOnClickListener(sendToServerButtonClickListener);
		databaseHelper = new DatabaseHelper(this);
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
				sendDataToServer(createXml(databaseHelper.getAllMessagesFromLocalStore()));
				databaseHelper.clearLocalStorage();
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

	private void sendDataToServer(String xml) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost postMethod = new HttpPost(databaseHelper.getServiceURLFromLocalStore());
		postMethod.setEntity(new StringEntity(xml));
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		//String responseBody = client.execute(postMethod, responseHandler);
		((EditText) findViewById(R.id.testOutputDataField)).setText(client.execute(postMethod).getStatusLine().getStatusCode()+"");
	}

	private String createXml(List<Message> messages) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "messages");
			serializer.attribute("", "number", String.valueOf(messages.size()));
			for (Message msg : messages) {
				serializer.startTag("", "message");
				serializer.attribute("", "date", msg.getDate());
				serializer.startTag("", "text");
				serializer.text(msg.getText());
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