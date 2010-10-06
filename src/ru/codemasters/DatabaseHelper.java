package ru.codemasters;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME="db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "strings_table";
	
	private static final String TEXT="text";
	private static final String DATE="date";
	
	private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + TEXT
			+ " TEXT, " + DATE + " TEXT);";

	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	public void addMessageToLocalStore(Message message){
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.DATE, message.getDate());
		cv.put(DatabaseHelper.TEXT, message.getText());
		this.getWritableDatabase().insert(DatabaseHelper.TABLE_NAME, DatabaseHelper.TEXT, cv);
	}
	
	public List<Message> getAllMessagesFromLocalStore(){
		String[] columns = { DatabaseHelper.TEXT, DatabaseHelper.DATE };
		Cursor cursor = this.getWritableDatabase().query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
		List<Message> messages = new ArrayList<Message>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			messages.add(new Message (cursor.getString(1),cursor.getString(0)));
			cursor.moveToNext();
		}
		cursor.close();
		return messages;
	}
	
	public void clearLocalStorage(){
		this.getWritableDatabase().delete(DatabaseHelper.TABLE_NAME, null, null);
	}
	
	public String getServiceURLFromLocalStore(){
		String url = "http://10.0.2.2:8080/emarket/admin/index.html";
		url = "http://93.158.134.3";
		url = "http://ya.ru";
		return url;
	}
	
	public void saveServiceURLToLOcalStore(String url){
		
	}
}
