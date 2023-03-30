package com.example.tugas10;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas10.helper.DBHelper;
import com.example.tugas10.helper.SharedPreferencesHelper;
import com.example.tugas10.models.ResponseModel;
import com.example.tugas10.models.UserModel;
import com.example.tugas10.models.UserSessionModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class HistorySessionActivity extends AppCompatActivity {
    private MaterialButton btnBack;
    private DBHelper DB;
    private TextView textWelcome;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AdapterData adapterData;
    private List<UserSessionModel> sessionModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_session);

        textWelcome = findViewById(R.id.textWelcome);
        recyclerView = findViewById(R.id.rv);
        UserModel userModel = SharedPreferencesHelper.getInstance().getUserDetail(this);
        textWelcome.setText(String.format("History Login And Logout For %s", userModel.username));

        btnBack = findViewById(R.id.back);
        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnBack.setOnClickListener(v -> toMainActivity());
        DB = new DBHelper(this);
        ResponseModel<List<UserSessionModel>> responseModel = DB.getSessions(userModel.id);
        if (responseModel.status) {
            sessionModelList = responseModel.data;
        } else {
            sessionModelList = new ArrayList<>();
        }
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterData = new AdapterData(this, sessionModelList);
        recyclerView.setAdapter(adapterData);
        adapterData.notifyDataSetChanged();
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
