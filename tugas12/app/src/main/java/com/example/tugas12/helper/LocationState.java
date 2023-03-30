package com.example.tugas12.helper;


import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.tugas12.models.ResponseModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocationState implements LocationListener {
    private LocationManager locationManager;
    private final Context context;
    private static LocationState sharedInstance;
    private final DBHelper DB;
    private final SharedPreferencesHelper sharedPreferences;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    final static int PERMISSION_ALL = 1;
    private static final int GPS_TIME_INTERVAL = 1000 * 60 * 3; // get gps location every 1 min
    private static final int GPS_DISTANCE = 1000; // set the distance value in meter
    private static final int HANDLER_DELAY = 1000 * 60 * 3;
    private static final int START_HANDLER_DELAY = 0;
    private Handler handler;
    private Runnable runnable;
    String apiKey = "030314b750cc43e7b39e503dfe37150c";
    HttpURLConnection urlConnection = null;
    LocationState(Activity context) {
        this.context = context.getApplicationContext();
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        context.requestPermissions(PERMISSIONS, PERMISSION_ALL);
        DB = DBHelper.sharedInstance(context);
        sharedPreferences = SharedPreferencesHelper.getInstance();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            public void run() {
                requestLocation();
                handler.postDelayed(this, HANDLER_DELAY);
            }
        };
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

    private void requestWeather(double latitude, double longitude){
        String weatherUrl = String.format("https://api.weatherbit.io/v2.0/current?lat=%s&&lon=%s&key=%s", latitude, longitude, apiKey);
        try {
            URL url = new URL(weatherUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            JSONObject obj = new JSONObject(line);
            JSONArray objArrData = obj.getJSONArray("data");
            JSONObject objData = objArrData.getJSONObject(0);
            ResponseModel responseModel = DB.insertWeather(sharedPreferences.getUserDetail(context).Id, objData.getDouble("temp"), objData.getJSONObject("weather").getString("description"));
            if(responseModel.Status){
                Log.i("UpdateWeather", "Update Weather Success");
            }else {
                Log.e("UpdateWeather", responseModel.Message);
            }
        } catch (Exception e) {
            Log.i("UpdateWeather", e.getMessage());
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(sharedPreferences.getUserDetail(context).Id != 0){
            ResponseModel responseModel = DB.insertLocation(sharedPreferences.getUserDetail(context).Id, location.getLatitude(), location.getLongitude());
            if (responseModel.Status) {
                Log.i("UpdateLocation", "Update Location Success");
                requestWeather(location.getLatitude(), location.getLongitude());
            } else {
                Log.e("UpdateLocation", responseModel.Message);
            }
        }
        locationManager.removeUpdates(this);
    }


}
