package ru.codemasters;

public class Message {
	private String text;
	private String date;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String data) {
		this.date = data;
	}
	public Message(String date,String text) {
		super();
		this.text = text;
		this.date = date;
	}

}
