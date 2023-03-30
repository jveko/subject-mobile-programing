package com.example.tugas10.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tugas10.models.ResponseModel;
import com.example.tugas10.models.UserModel;
import com.example.tugas10.models.UserSessionModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "tugas10.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT " +
                "NULL UNIQUE, password TEXT NOT NULL)");
        DB.execSQL("CREATE TABLE user_sessions(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id " +
                "INTEGER NOT NULL, login INTEGER NOT NULL, logout INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("DROP TABLE IF EXISTS users");
        DB.execSQL("DROP TABLE IF EXISTS user_sessions");
    }

    public ResponseModel insertUser(String username, String password) {
        ResponseModel responseModel = new ResponseModel();
        try {
            SQLiteDatabase DB = this.getWritableDatabase();
            Cursor cursorUser = DB.rawQuery("SELECT * FROM users WHERE username = ?",
                    new String[]{username});
            if (cursorUser.getCount() > 0) {
                responseModel.message = "User Exist";
                return responseModel;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", username);
            contentValues.put("password", password);
            long result = DB.insertOrThrow("users", null, contentValues);
            if (result == -1) {
                responseModel.message = "Error While Insert User";
                return responseModel;
            }
            responseModel.status = true;
            return responseModel;
        } catch (Exception e) {
            responseModel.message = e.getMessage();
            return responseModel;
        }

    }

    public ResponseModel<UserModel> login(String username, String password) {
        ResponseModel<UserModel> responseModel = new ResponseModel<>();
        try {
            SQLiteDatabase DB = this.getWritableDatabase();
            Cursor cursorUser = DB.rawQuery("SELECT * FROM users WHERE username = ?",
                    new String[]{username});
            if (cursorUser.getCount() > 0) {
                if (cursorUser.moveToFirst()) {
                    if (cursorUser.getString(2).equals(password)) {
                        UserModel user = new UserModel(cursorUser.getInt(0),
                                cursorUser.getString(1));
                        responseModel.status = true;
                        responseModel.data = user;
                        return responseModel;
                    }
                }
            }
            responseModel.message = "User doesn't exist";
            return responseModel;
        } catch (Exception e) {
            responseModel.message = e.getMessage();
            return responseModel;
        }
    }

    public ResponseModel<Integer> insertSession(int userId) {
        ResponseModel<Integer> responseModel = new ResponseModel<>();
        try {
            SQLiteDatabase DB = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("user_id", userId);
            contentValues.put("login", System.currentTimeMillis());
            long result = DB.insert("user_sessions", null, contentValues);
            if (result == -1) {
                responseModel.message = "Error Insert Session";
                return responseModel;
            }
            Cursor cursorSession = DB.rawQuery("SELECT * FROM user_sessions WHERE id = (SELECT " +
                            "MAX(id) FROM user_sessions WHERE user_id = ?)",
                    new String[]{String.valueOf(userId)});
            if (cursorSession.getCount() > 0 && cursorSession.moveToFirst()) {
                responseModel.status = true;
                responseModel.data = cursorSession.getInt(0);
                return responseModel;
            }
            responseModel.message = "Error Get Session After Insert";
            return responseModel;
        } catch (Exception e) {
            responseModel.message = e.getMessage();
            return responseModel;
        }
    }

    public ResponseModel updateSession(int id) {
        ResponseModel responseModel = new ResponseModel();
        try {
            SQLiteDatabase DB = this.getWritableDatabase();
            Cursor cursorSession = DB.rawQuery("SELECT * FROM user_sessions WHERE id = ?",
                    new String[]{String.valueOf(id)});
            if (cursorSession.getCount() == 0) {
                responseModel.message = "Id Doesn't Exist";
                return responseModel;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("logout", System.currentTimeMillis());
            long result = DB.update("user_sessions", contentValues, "id=?",
                    new String[]{String.valueOf(id)});
            if (result == -1) {
                responseModel.message = "Error While Update Table";
                return responseModel;
            }
            responseModel.status = true;
            return responseModel;
        } catch (Exception e) {
            responseModel.message = e.getMessage();
            return responseModel;
        }
    }

    public ResponseModel<List<UserSessionModel>> getSessions(int userId) {
        ResponseModel<List<UserSessionModel>> responseModel = new ResponseModel<>();
        try {
            SQLiteDatabase DB = this.getWritableDatabase();
            Cursor cursorSession = DB.rawQuery("SELECT * FROM user_sessions WHERE user_id = ?",
                    new String[]{String.valueOf(userId)});
            responseModel.data = new ArrayList<>();
            while (cursorSession.moveToNext()) {
                Date dateLogin = cursorSession.getLong(2) != 0 ?
                        new Date(cursorSession.getLong(2)) : null;
                Date dateLogout = cursorSession.getLong(3) != 0 ?
                        new Date(cursorSession.getLong(3)) : null;
                responseModel.data.add(new UserSessionModel(cursorSession.getInt(0), dateLogin,
                        dateLogout));
            }
            responseModel.status = true;
            return responseModel;
        } catch (Exception e) {
            responseModel.message = e.getMessage();
            return responseModel;
        }
    }
}