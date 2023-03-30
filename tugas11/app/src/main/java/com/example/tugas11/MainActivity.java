package com.example.tugas11;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tugas11.helper.DBHelper;
import com.example.tugas11.helper.LocationState;
import com.example.tugas11.helper.SharedPreferencesHelper;
import com.example.tugas11.models.ResponseModel;
import com.example.tugas11.models.UserModel;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity  {


    private MaterialButton btnLogout, btnHistorySession, btnHistoryLocation;
    private DBHelper DB;
    private LocationState locationState;
    private TextView textWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textWelcome = findViewById(R.id.textWelcome);
        UserModel userModel = SharedPreferencesHelper.getInstance().getUserDetail(this);
        textWelcome.setText(String.format("Welcome %s", userModel.Username));

        btnLogout = findViewById(R.id.logout);
        btnHistorySession = findViewById(R.id.historySessionBtn);
        btnHistoryLocation = findViewById(R.id.historyLocationBtn);
        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnLogout.setOnClickListener(v -> logoutUser());
        btnHistorySession.setOnClickListener(v -> toHistorySessionActivity());
        btnHistoryLocation.setOnClickListener(v -> toHistoryLocationActivity());
        DB = DBHelper.sharedInstance(this);
        locationState = LocationState.sharedInstance(this);
        locationState.startInterval();
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
    private void toHistoryLocationActivity() {
        Intent intent = new Intent(this, HistoryLocationActivity.class);
        startActivity(intent);
        finish();
    }

    private void logoutUser() {
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance();
        int sessionId =sharedPreferencesHelper.getSessionId(this);
        ResponseModel responseModel = DB.updateSession(sessionId);
        if (!responseModel.Status) {
            Log.e("logoutProcess", responseModel.Message);
            return;
        }
        Log.i("logoutProcess", "Update Session Success");
        locationState.stopInterval();
        sharedPreferencesHelper.setLoggedIn(this, false);
        Log.i("logoutProcess", "Logout Success");
        toLoginActivity();
    }

}