package com.example.tugas10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.tugas10.helper.DBHelper;
import com.example.tugas10.helper.SharedPreferencesHelper;
import com.example.tugas10.models.ResponseModel;
import com.example.tugas10.models.UserModel;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {


    private MaterialButton btnLogout, btnHistory;
    private DBHelper DB;
    private TextView textWelcome;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textWelcome = findViewById(R.id.textWelcome);
        UserModel userModel = SharedPreferencesHelper.getInstance().getUserDetail(this);
        textWelcome.setText(String.format("Welcome %s", userModel.username));

        btnLogout = findViewById(R.id.logout);
        btnHistory = findViewById(R.id.historyBtn);
        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnLogout.setOnClickListener(v -> logoutUser());
        btnHistory.setOnClickListener(v -> toHistorySessionActivity());
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance();
        DB = new DBHelper(this);
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toHistorySessionActivity() {
        Intent intent = new Intent(this, HistorySessionActivity.class);
        startActivity(intent);
        finish();
    }

    private void logoutUser() {
        int sessionId = sharedPreferencesHelper.getSessionId(this);
        ResponseModel responseModel = DB.updateSession(sessionId);
        if (!responseModel.status) {
            Log.e("logoutProcess", responseModel.message);
            return;
        }
        Log.i("logoutProcess", "Update Session Success");
        sharedPreferencesHelper.setLoggedIn(this, false);
        Log.i("logoutProcess", "Logout Success");
        toLoginActivity();
    }
}