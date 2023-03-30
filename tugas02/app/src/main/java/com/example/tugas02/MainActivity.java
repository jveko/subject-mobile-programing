package com.example.tugas02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date date = new Date();
        TextView txtDate = findViewById(R.id.txtDate);
        TextView txtTime = findViewById(R.id.txtTime);
        TextView txtZone = findViewById(R.id.txtZone);

        txtDate.setText("Hari ini, Tanggal " + sdf1.format(date).toString());
        txtTime.setText("Waktu Sekarang adalah " + sdf2.format(date).toString());
        txtZone.setText("Zona Waktu yang dipakai adalah " + sdf1.getTimeZone().getDisplayName());
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Date date = new Date();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // do UI updates here
                        txtDate.setText("Hari ini, Tanggal " + sdf1.format(date).toString());
                        txtTime.setText("Waktu Sekarang adalah " + sdf2.format(date).toString());
                        txtZone.setText("Zona Waktu yang dipakai adalah " + sdf1.getTimeZone().getDisplayName());

                    }
                });
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
}