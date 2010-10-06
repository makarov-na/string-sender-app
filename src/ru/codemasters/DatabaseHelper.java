package ru.codemasters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME="db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "strings_table";
	
	public static final String TEXT="text";
	public static final String DATE="date";
	
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
	
	public void insertData(){
		
	}
	
}
