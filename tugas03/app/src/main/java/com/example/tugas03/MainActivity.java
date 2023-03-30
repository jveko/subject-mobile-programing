package com.example.tugas03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.tugas03.helper.Watcher;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {


    private MaterialButton btnLogout;
    private Watcher watcher;
    private TextView textLoginTime,textLogoutTime,textDurationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        watcher = Watcher.sharedInstance();

        textLoginTime = findViewById(R.id.loginTime);
        textLogoutTime = findViewById(R.id.logoutTime);
        textDurationTime = findViewById(R.id.durationTime);

        if(watcher.getLastLogin() != ""){
            textLoginTime.setText("Last Login Time " + watcher.getLastLogin() );
        } else {
            textLoginTime.setText("You need Login Action");
        }
        if(watcher.getLastLogout() != ""){
            textLogoutTime.setText("Last Logout Time " + watcher.getLastLogout() );
        }else {
            textLogoutTime.setText("You need Logout Action");
        }
        if(watcher.getDuration() != ""){
            textDurationTime.setText("Last Duration Login-Logout Time " + watcher.getDuration() );
        }

        btnLogout = findViewById(R.id.logout);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void logoutUser() {
        watcher.setDateLogout();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}