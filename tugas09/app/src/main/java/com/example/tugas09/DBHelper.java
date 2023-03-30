package com.example.tugas09;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE users(name TEXT primary key, phone TEXT)");

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "test");
        contentValues.put("phone", "test");
        DB.insert("users", null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("DROP TABLE IF EXISTS users");
    }

}