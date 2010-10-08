package ru.codemasters;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class ServerStore {
	
	public ServerStore(){
		
	}
	
	
	public int addMessagesToServerStore(String serverStoreURL, Collection <Message> messages) throws ClientProtocolException, IOException{
		return sendDataToServer(serverStoreURL,createXml(messages));
	}
	
	private int sendDataToServer(String serverStoreURL,String xml) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost postMethod = new HttpPost(serverStoreURL);
		postMethod.setEntity(new StringEntity(xml));
		return client.execute(postMethod).getStatusLine().getStatusCode();
		
	}
	
	private String createXml(Collection<Message> messages) {
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
