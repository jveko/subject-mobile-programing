package com.example.tugas05;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tugas05.helper.LocationState;
import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {


    private MaterialButton btnLogout;
    private LocationState locationState;
    private TextView textAddress,txtCountry;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationState = LocationState.sharedInstance(this);
        locationState.updateLocation();
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                () -> {
                    setContentView(R.layout.activity_main);

                    textAddress = findViewById(R.id.address);
                    txtCountry = findViewById(R.id.country);

                    textAddress.setText(locationState.getAddress());
                    txtCountry.setText(locationState.getCountry());

                    btnLogout = findViewById(R.id.logout);

                    // Hide Keyboard
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    btnLogout.setOnClickListener(v -> logoutUser());
                    CalendarView calendarView = findViewById(R.id.calendarView);
                    if (calendarView != null) {
                        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                                // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
                                String msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year;
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                },
                1000);
    }

    private void logoutUser() {
        Intent intent = new Intent(MainActivity.this, com.example.tugas05.LoginActivity.class);
        startActivity(intent);
        finish();
    }

}