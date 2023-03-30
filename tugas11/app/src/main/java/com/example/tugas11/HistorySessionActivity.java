package com.example.tugas11;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas11.adapters.AdapterDataSession;
import com.example.tugas11.helper.DBHelper;
import com.example.tugas11.helper.SharedPreferencesHelper;
import com.example.tugas11.models.ResponseModel;
import com.example.tugas11.models.UserModel;
import com.example.tugas11.models.UserSessionModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class HistorySessionActivity extends AppCompatActivity {
    private MaterialButton btnBack;
    private DBHelper DB;
    private TextView textWelcome;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AdapterDataSession adapterDataSession;
    private List<UserSessionModel> sessionModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_session);

        textWelcome = findViewById(R.id.textWelcome);
        recyclerView = findViewById(R.id.rv);
        UserModel userModel = SharedPreferencesHelper.getInstance().getUserDetail(this);
        textWelcome.setText(String.format("History Login And Logout For %s", userModel.Username));

        btnBack = findViewById(R.id.back);
        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnBack.setOnClickListener(v -> toMainActivity());
        DB = new DBHelper(this);
        ResponseModel<List<UserSessionModel>> responseModel = DB.getSessions(userModel.Id);
        if(responseModel.Status){
            sessionModelList = responseModel.Data;
        }else {
            sessionModelList = new ArrayList<>();
        }
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterDataSession = new AdapterDataSession(this, sessionModelList);
        recyclerView.setAdapter(adapterDataSession);
        adapterDataSession.notifyDataSetChanged();
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
