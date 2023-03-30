package com.example.tugas15;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas15.adapters.AdapterDataLocation;
import com.example.tugas15.helper.DBHelper;
import com.example.tugas15.helper.LocationState;
import com.example.tugas15.helper.SharedPreferencesHelper;
import com.example.tugas15.models.ResponseModel;
import com.example.tugas15.models.UserLocationModel;
import com.example.tugas15.models.UserModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryLocationActivity extends AppCompatActivity {
    private MaterialButton btnBack;
    private DBHelper DB;
    private TextView textWelcome;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AdapterDataLocation adapterDataLocation;
    private List<UserLocationModel> locationModelList;
    private LocationState locationState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_session);
        locationState = LocationState.sharedInstance(this);
        textWelcome = findViewById(R.id.textWelcome);
        recyclerView = findViewById(R.id.rv);
        UserModel userModel = SharedPreferencesHelper.getInstance().getUserDetail(this);
        textWelcome.setText(String.format("History Location For %s", userModel.Username));

        btnBack = findViewById(R.id.back);
        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnBack.setOnClickListener(v -> toMainActivity());
        DB = DBHelper.sharedInstance(this);
        ResponseModel<List<UserLocationModel>> responseModel = DB.getLocations(userModel.Id);
        if(responseModel.Status){
            locationModelList = responseModel.Data.stream().map(r -> addAddress(r)).filter(r -> r != null)
                    .collect(Collectors.toList());
        }else {
            locationModelList = new ArrayList<>();
        }
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterDataLocation = new AdapterDataLocation(this, locationModelList);
        recyclerView.setAdapter(adapterDataLocation);
        adapterDataLocation.notifyDataSetChanged();
    }

    private UserLocationModel addAddress(UserLocationModel userLocationModel){
        ResponseModel<Address> responseModel=  locationState.getAddress(userLocationModel.Latitude, userLocationModel.Longitude);
        if(responseModel.Status){
            userLocationModel.Address = responseModel.Data;
            return userLocationModel;
        }
        userLocationModel.Address = null;
        return userLocationModel;
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, com.example.tugas15.MainActivity.class);
        startActivity(intent);
        finish();
    }

}
