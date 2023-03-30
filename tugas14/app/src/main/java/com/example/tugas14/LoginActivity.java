package com.example.tugas14;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tugas14.helper.DBHelper;
import com.example.tugas14.helper.LocationState;
import com.example.tugas14.helper.SharedPreferencesHelper;
import com.example.tugas14.models.ResponseModel;
import com.example.tugas14.models.UserModel;
import com.example.tugas14.widget.ProgressBarDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton btnLogin;
    private TextInputLayout inputEmail, inputPassword;
    private DBHelper DB;
    private LocationState locationState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        beforeInit();
        System.out.println("Start");
        inputEmail = findViewById(R.id.edit_email);
        inputEmail.getEditText().setText("qq");
        inputPassword = findViewById(R.id.edit_password);
        inputPassword.getEditText().setText("qq");
        btnLogin = findViewById(R.id.button_login);
        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        System.out.println("AAA");
        DB = DBHelper.sharedInstance(this);
        DB.insertUser("qq", "qq");
        locationState = LocationState.sharedInstance(this);
        init();
    }

    private void beforeInit() {
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance();
        if (sharedPreferencesHelper.isLoggedIn(this)) {
            toMainActivity();
        }
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
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, com.example.tugas14.MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginProcess(final String email, final String password) {
        showDialog("Logging in ...");
        ResponseModel<UserModel> responseModel = DB.login(email, password);
        if (responseModel.Status) {
            Log.i("loginProcess", "Login Success");
            ResponseModel<Integer> responseModel1 = DB.insertSession(responseModel.Data.Id);
            if (responseModel1.Status) {
                Log.i("loginProcess", "Insert Session Success");
                SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance();
                sharedPreferencesHelper.setLoggedIn(this, true);
                sharedPreferencesHelper.setUserDetail(this, responseModel.Data.Id, responseModel.Data.Username);
                sharedPreferencesHelper.setSessionId(this, responseModel1.Data);
                toMainActivity();
            } else {
                Log.e("loginProcess", responseModel1.Message);
                Toast.makeText(getApplicationContext(), "Invalid Email/Username", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        } else {
            Log.e("loginProcess", responseModel.Message);
            Toast.makeText(getApplicationContext(), "Invalid Email/Username", Toast.LENGTH_LONG).show();
            hideDialog();
        }
    }

    private void showDialog(String title) {
        FragmentManager fm = ((AppCompatActivity) LoginActivity.this).getSupportFragmentManager();
        DialogFragment newFragment = ProgressBarDialog.newInstance(title);
        newFragment.show(fm, "dialog");
    }

    private void hideDialog() {
        FragmentManager fm = ((AppCompatActivity) LoginActivity.this).getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("dialog");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }
}
