package com.example.tugas14.helper;


import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.tugas14.models.ResponseModel;

import java.util.List;
import java.util.Locale;

public class LocationState implements LocationListener {
    private LocationManager locationManager;
    private Context context;
    private static LocationState sharedInstance;
    private DBHelper DB;
    private SharedPreferencesHelper sharedPreferences;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    final static int PERMISSION_ALL = 1;
    private static final int GPS_TIME_INTERVAL = 1000 * 60 * 3; // get gps location every 1 min
    private static final int GPS_DISTANCE = 1000; // set the distance value in meter
    private static final int HANDLER_DELAY = 1000 * 60 * 3;
    private static final int START_HANDLER_DELAY = 0;
    private Handler handler;
    private Runnable runnable;
    private Geocoder geocoder;
    LocationState(Activity context) {
        this.context = context.getApplicationContext();
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        context.requestPermissions(PERMISSIONS, PERMISSION_ALL);
        DB = DBHelper.sharedInstance(context);
        sharedPreferences = SharedPreferencesHelper.getInstance();
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public synchronized static LocationState sharedInstance(Activity mContext) {
        if (sharedInstance == null) {
            LocationState state = new LocationState(mContext);
            sharedInstance = state;
        }
        return sharedInstance;
    }

    public void startInterval(){
        Log.i("intervalLocation", "Start Interval ...");
        if(handler == null){
            handler = new Handler(Looper.getMainLooper());
        }
        if(runnable == null){
            runnable = new Runnable() {
                public void run() {
                    requestLocation();
                    handler.postDelayed(this, HANDLER_DELAY);
                }
            };
        }
        if(handler.hasCallbacks(runnable)){
            Log.i("intervalLocation", "Runnable Already Post ...");
            return;
        }
        handler.postDelayed(runnable, START_HANDLER_DELAY);
    }

    public void stopInterval(){
        handler.removeCallbacks(runnable);
        Log.i("intervalLocation", "Stop Interval ...");
    }

    private void requestLocation() {
        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        GPS_TIME_INTERVAL, GPS_DISTANCE, this);
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(sharedPreferences.getUserDetail(context).Id != 0){
            ResponseModel responseModel = DB.insertLocation(sharedPreferences.getUserDetail(context).Id, location.getLatitude(), location.getLongitude());
            if (responseModel.Status) {
                Log.i("UpdateLocation", "Update Location Success");
            } else {
                Log.e("UpdateLocation", responseModel.Message);
            }
        }
        locationManager.removeUpdates(this);
    }

    public ResponseModel<Address> getAddress(double latitude, double longitude){
        ResponseModel<Address> responseModel = new ResponseModel<>();
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude,longitude, 1);
            if(addressList != null && !addressList.isEmpty()){
                responseModel.Data = addressList.get(0);
                responseModel.Status = true;
                return responseModel;
            }
            responseModel.Message = "Address Not Found";
            return responseModel;
        }catch (Exception e){
            responseModel.Message = e.getMessage();
            return responseModel;
        }
    }
}
