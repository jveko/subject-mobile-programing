package com.example.tugas07;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.content.BroadcastReceiver;
import android.app.PendingIntent;
import android.app.AlarmManager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tugas07.helper.AlarmReceiver;
import com.example.tugas07.helper.LocationAndWeatherState;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {


    private MaterialButton btnLogout;
    private LocationAndWeatherState locationAndWeatherState;
    private TextView txtAddress, txtCountry, txtTemp, txtTitleWeather;
    private ImageView imgWeather;

    private TimePicker alarmTime;
    private PendingIntent pendingIntent;
    private Ringtone ringtone;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationAndWeatherState = LocationAndWeatherState.sharedInstance(this);
        setContentView(R.layout.activity_main);

        txtAddress = findViewById(R.id.address);
        txtCountry = findViewById(R.id.country);

        txtTemp = findViewById(R.id.temp);
        txtTitleWeather = findViewById(R.id.titleWeather);
        imgWeather = findViewById(R.id.imageWeather);

        txtAddress.setText("Alamat Loading...");
        txtCountry.setText("Negara Loading...");
        txtTemp.setText("Temperatur Loading...");
        txtTitleWeather.setText("Weather Loading...");
        locationAndWeatherState.updateLocation(this, txtAddress, txtCountry, txtTemp, txtTitleWeather, imgWeather);

        txtAddress.setText("Address Loading...");
        txtCountry.setText("Country Loading...");

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

        alarmTime = findViewById(R.id.timePicker1);
        ringtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        registerReceiver(broadcastReceiver, new IntentFilter("ALARM_RECEIVER"));
    }

    private void logoutUser() {
        Intent intent = new Intent(MainActivity.this, com.example.tugas07.LoginActivity.class);
        startActivity(intent);
        finish();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ringtone.play();
        }
    };

    public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            Toast.makeText(this, "Alarm on", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + getTime(), pendingIntent);
        } else {
            ringtone.stop();
            pendingIntent.cancel();
        }
    }

    private long getTime() {
        Calendar cal = Calendar.getInstance();
        long currentTimeMillis = cal.getTimeInMillis();
        cal.set(Calendar.HOUR, alarmTime.getCurrentHour());
        cal.set(Calendar.MINUTE, alarmTime.getCurrentMinute());
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long alarmTimeMillis = cal.getTimeInMillis();
        long subtraction = alarmTimeMillis - currentTimeMillis;
        if (subtraction < 0) {
            return 0;
        }
        return subtraction;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}