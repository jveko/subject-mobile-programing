package com.example.tugas10;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tugas10.helper.DBHelper;
import com.example.tugas10.helper.SharedPreferencesHelper;
import com.example.tugas10.models.ResponseModel;
import com.example.tugas10.models.UserModel;
import com.example.tugas10.widget.ProgressBarDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton btnLogin;
    private TextInputLayout inputEmail, inputPassword;
    private DBHelper DB;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance();
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
        DB = new DBHelper(this);
        DB.insertUser("qq", "qq");
        init();
    }

    private void beforeInit() {
        if (sharedPreferencesHelper.isLoggedIn(this)) {
            toMainActivity();
        }
    }

    private void init() {
        // Login button Click Event
        btnLogin.setOnClickListener(view -> {
            String email =
                    Objects.requireNonNull(inputEmail.getEditText()).getText().toString().trim();
            String password =
                    Objects.requireNonNull(inputPassword.getEditText()).getText().toString().trim();

            // Check for empty data in the form
            if (!email.isEmpty() && !password.isEmpty()) {
                loginProcess(email, password);
            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(), "Please enter the credentials!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginProcess(final String email, final String password) {
        showDialog("Logging in ...");
        ResponseModel<UserModel> responseModel = DB.login(email, password);
        if (responseModel.status) {
            Log.i("loginProcess", "Login Success");
            ResponseModel<Integer> responseModel1 = DB.insertSession(responseModel.data.id);
            if (responseModel1.status) {
                Log.i("loginProcess", "Insert Session Success");
                SharedPreferencesHelper sharedPreferencesHelper =
                        SharedPreferencesHelper.getInstance();
                sharedPreferencesHelper.setLoggedIn(this, true);
                sharedPreferencesHelper.setUserDetail(this, responseModel.data.id,
                        responseModel.data.username);
                sharedPreferencesHelper.setSessionId(this, responseModel1.data);
                toMainActivity();
            } else {
                Log.e("loginProcess", responseModel1.message);
                Toast.makeText(getApplicationContext(), "Invalid Email/Username",
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        } else {
            Log.e("loginProcess", responseModel.message);
            Toast.makeText(getApplicationContext(), "Invalid Email/Username", Toast.LENGTH_LONG).show();
            hideDialog();
        }
    }

    private void showDialog(String title) {
        FragmentManager fm = LoginActivity.this.getSupportFragmentManager();
        DialogFragment newFragment = ProgressBarDialog.newInstance(title);
        newFragment.show(fm, "dialog");
    }

    private void hideDialog() {
        FragmentManager fm = LoginActivity.this.getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("dialog");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }
}
