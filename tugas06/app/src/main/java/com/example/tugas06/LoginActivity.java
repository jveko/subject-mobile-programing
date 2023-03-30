package com.example.tugas06;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tugas06.helper.LocationAndWeatherState;
import com.example.tugas06.widget.ProgressBarDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    private MaterialButton btnLogin;
    private TextInputLayout inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LocationAndWeatherState locationAndWeatherState = LocationAndWeatherState.sharedInstance(this);
        locationAndWeatherState.checkPermissions();

        inputEmail = findViewById(R.id.edit_email);
        inputPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.button_login);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
    }

    private void init() {
        // Login button Click Event
        btnLogin.setOnClickListener(view -> {
            String email = Objects.requireNonNull(inputEmail.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(inputPassword.getEditText()).getText().toString().trim();

            // Check for empty data in the form
            if (!email.isEmpty() && !password.isEmpty()) {
                loginProcess(email, password);
            } else {
                // Prompt user to enter credentials`
                Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginProcess(final String email, final String password) {
        showDialog();
        System.out.println(email);
        System.out.println(password);
        new android.os.Handler().postDelayed(
                () -> {
                    if(email.equals("qq") && password.equals("qq")){
                        System.out.println("Success");
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Log.e("loginProcess", "Login Error: Invalid Email/Username");
                        Toast.makeText(this, "Invalid Email/Username", Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }, 1000);
    }

    private void showDialog() {
        FragmentManager fm = this.getSupportFragmentManager();
        DialogFragment newFragment = ProgressBarDialog.newInstance("Logging in ...");
        newFragment.show(fm, "dialog");
    }

    private void hideDialog(){
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("dialog");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }
}
