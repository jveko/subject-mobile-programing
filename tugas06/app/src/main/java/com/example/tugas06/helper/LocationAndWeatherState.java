package com.example.tugas06.helper;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.text.SimpleDateFormat;
import java.util.Locale;


public class LocationAndWeatherState {
    private final LocationManager locationManager;
    private LocationListener listener;
    private Location lastLocation;
    private final Activity context;
    private static LocationAndWeatherState sharedInstance;
    private final Geocoder geocoder;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private static String apiKey = "030314b750cc43e7b39e503dfe37150c";
    LocationAndWeatherState(Activity mContext) {
        context = mContext;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(context, Locale.getDefault());
        listener = location -> {
            lastLocation = location;
            System.out.println(lastLocation);
        };
    }

    public static LocationAndWeatherState sharedInstance(Activity mContext) {
        if (sharedInstance == null) {
            LocationAndWeatherState state = new LocationAndWeatherState(mContext);
            sharedInstance = state;
        }
        return sharedInstance;
    }

    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET}, 10);
            }
            return;
        }
    }

    @SuppressLint({"MissingPermission", "NewApi"})
    public void updateLocation(Activity mContext, TextView addressText, TextView countryText, TextView tempText, TextView titleWeatherText, ImageView weatherImage){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        locationManager.getCurrentLocation(
                LocationManager.GPS_PROVIDER,
                null,
                ContextCompat.getMainExecutor(mContext),
                location -> {
                    lastLocation = location;
                    addressText.setText(getAddress());
                    countryText.setText(getCountry());
                    GetWeather getWeather = new GetWeather();
                    getWeather.setParams(lastLocation, apiKey, tempText, titleWeatherText, weatherImage);
                    mContext.runOnUiThread(getWeather);
                });
    }

    public class GetWeather implements Runnable {
        Location location;
        String apiKey;
        TextView txtTemp,txtTitleWeather;
        ImageView imageWeather;
        HttpURLConnection urlConnection = null;
        public void setParams(Location location, String apiKey, TextView txtTemp, TextView txtTitleWeather, ImageView imageWeather){
            this.location = location;
            this.apiKey = apiKey;
            this.txtTemp = txtTemp;
            this.txtTitleWeather = txtTitleWeather;
            this.imageWeather = imageWeather;
        }

        public void run(){
            String weatherUrl = "https://api.weatherbit.io/v2.0/current?" + "lat=" + lastLocation.getLatitude() + "&lon=" + lastLocation.getLongitude() + "&key=" + apiKey;
            try {
                System.out.println(weatherUrl);
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
//                txtTemp =
                txtTemp.setText("Temp saat ini adalah " + objData.getDouble("temp"));;
                txtTitleWeather.setText("Cuaca saat ini adalah " + objData.getJSONObject("weather").getString("description"));
                Picasso.get().load("https://www.weatherbit.io/static/img/icons/"+objData.getJSONObject("weather").getString("icon") + ".png").into(imageWeather);
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        }
    }


    private Address getLocationRaw() {
        if(lastLocation != null){
            try {
                return geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1).get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getAddress() {
        if(getLocationRaw() != null){
            return "Alamat " + getLocationRaw().getAddressLine(0);
        }
        return "";
    }
    public String getCountry() {
        if(getLocationRaw() != null){
            return "Negara " + getLocationRaw().getCountryName();
        }
        return "";
    }


}
