package com.example.tugas07.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Ringing", Toast.LENGTH_LONG).show();
        context.sendBroadcast(new Intent("ALARM_RECEIVER"));
    }
}
