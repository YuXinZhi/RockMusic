package com.example.rockmusic.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static String TB_FAVORITED_NAME = "TABLE_FAVORITED";
	private static String TABLE_NAME = "DATE_BASE_FAVORITED";
	private String CREATE_TABLE;

	public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		init();
		db.execSQL(CREATE_TABLE);
	}

	private void init() {
		CREATE_TABLE = "CREATE TABLE"
				+ " "
				+ TABLE_NAME
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ "TITLE TEXT,ARTIST TEXT,PATH TEXT,SONG_ID LONG,ALBUM_ID LONG,DURATION LONG)";
	}

	// 数据库升级
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 删除表
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		// 从新创建
		onCreate(db);
	}

}
