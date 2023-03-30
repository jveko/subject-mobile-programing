package com.example.tugas15.helper;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.tugas15.models.UserModel;


public class SharedPreferencesHelper {
    private static SharedPreferencesHelper instance;
    private static final String PREFS_NAME = "default_preferences";

    public synchronized static SharedPreferencesHelper getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesHelper();
        }
        return instance;
    }

    private SharedPreferencesHelper() {
    }

    public boolean isLoggedIn(@NonNull Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean("LOGGED", false);
    }

    public void setLoggedIn(@NonNull Context context, boolean value) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean("LOGGED", value).apply();
    }

    public void setUserDetail(@NonNull Context context, int id, String username) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putInt("User_Id", id).apply();
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString("User_Username", username).apply();
    }

    public void setSessionId(@NonNull Context context, int id) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putInt("Session_Id", id).apply();
    }

    public UserModel getUserDetail(@NonNull Context context) {
        int id = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getInt("User_Id", 0);
        String username = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString("User_Username", null);
        if(id != 0 && username != null){
            return new UserModel(id, username);
        }
        return null;
    }

    public int getSessionId(@NonNull Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getInt("Session_Id", 0);
    }
}
