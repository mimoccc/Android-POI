package com.poi.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PoiDBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "poi.db";
    private static PoiDBHelper instance; 
    
	private PoiDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static PoiDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new PoiDBHelper(context);
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(PoiDao.CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PoiDao.TABLE);
		onCreate(db);
	}
}
