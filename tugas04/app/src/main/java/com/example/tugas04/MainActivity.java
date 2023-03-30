package com.example.tugas04;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tugas04.helper.LocationState;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {


    private MaterialButton btnLogout;
    private LocationState locationState;
    private TextView textTime,textAddress,txtCountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationState = LocationState.sharedInstance(this);
        locationState.updateLocation();

        textTime = findViewById(R.id.time);
        textAddress = findViewById(R.id.address);
        txtCountry = findViewById(R.id.country);

        textTime.setText(locationState.getTime());
        textAddress.setText(locationState.getAddress());
        txtCountry.setText(locationState.getCountry());

        btnLogout = findViewById(R.id.logout);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void logoutUser() {
        Intent intent = new Intent(MainActivity.this, com.example.tugas04.LoginActivity.class);
        startActivity(intent);
        finish();
    }
}